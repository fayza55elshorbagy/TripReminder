package com.example.tripreminder;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.example.tripreminder.beans.Trips;
import com.example.tripreminder.roomDB.TripsViewModel;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.example.tripreminder.DialogActivity.notificationIntentKey;

public class addTripActivity extends AppCompatActivity  {
    private Toolbar toolBar;
    private ImageView btnDate;
    private TextView dateText;
    private ImageView btnTime;
    private TextView timeText;
    private EditText tripName;
    private EditText startPoint;
    private EditText endPoint;
    private Button addBtn;
    private TextView dateBackText;
    private TextView timeBackText;
    private ImageView btnTimeBack;
    private ImageView btnDateBack;

    private LinearLayout round_details;
    private LinearLayout trip_type;
    private RadioButton single;
    private RadioButton round;

    private TripsViewModel viewModel;
    private Boolean editMode;
    private int tripID;
    private Trips editObj;
    private String strStartPoint="";
    private String strEndPoint="";

    private Calendar backCalendar;
    private Calendar myCalendar;
    private Calendar  myCurrentCalendar;
    private TimePickerDialog mTimePicker;
    private TimePickerDialog mTimePickerBack;

    private Date selectedDate = null;
    private Date currentDate = null;
    private Date selectedBackDate = null;
    private Boolean isDateSelect=false;
    private Boolean isTimeSelect=false;
    private SimpleDateFormat dateFormant;
    private SimpleDateFormat tDateFormant;
    private Handler insertHandler;
    private  Handler handlerInsertRound;
    private long id;
    private static int AUTOCOMPLETE_REQUEST_CODE_START = 1;
    private static int AUTOCOMPLETE_REQUEST_CODE_END = 2;
    public static String TRIP_ID = "Edit_Trip";
    public static String TRIP_OBJ = "Trip_OBJ";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);
        setSupportActionBar(toolBar);
        initialize();
        setDate();
        setTime();
        setBackTime();
        setBackDate();

        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), "AIzaSyALHrPpUa1o9Hc-6zTue0nt_CgM0tJa_pc", Locale.getDefault());
        }

        Intent intentTrip =getIntent();
        if(intentTrip.hasExtra(TRIP_ID)){
             toolBar.setTitle("Edit");
             addBtn.setText("Save");
             editMode=true;
             editObj= (Trips) getIntent().getSerializableExtra(TRIP_OBJ);

             strStartPoint=editObj.getStartPoint().toString();
             strEndPoint=editObj.getEndPoint().toString();

             tripID=intentTrip.getIntExtra(TRIP_ID,-1);
             tripName.setText(editObj.getName().toString());
             startPoint.setText(editObj.getStartLoc().toString());
             endPoint.setText(editObj.getEndLoc().toString());
             timeText.setText(editObj.getTime().toString());
             dateText.setText(editObj.getDate().toString());
             trip_type.setVisibility(View.GONE);
            btnTime.setColorFilter(getResources().getColor(R.color.purbleApp), PorterDuff.Mode.SRC_ATOP);
            btnDate.setColorFilter(getResources().getColor(R.color.purbleApp), PorterDuff.Mode.SRC_ATOP);
            }
        else{
                editMode=false;
                toolBar.setTitle("Add");
        }

        round.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                round_details.setVisibility(View.VISIBLE);
            }
        });
        single.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                round_details.setVisibility(View.GONE);
            }
        });
        startPoint.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME,Place.Field.LAT_LNG);
                // Start the autocomplete intent.
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                        .build(addTripActivity.this);
                startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE_START);

            }
        });
        endPoint.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME,Place.Field.LAT_LNG);
                // Start the autocomplete intent.
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                        .build(addTripActivity.this);
                startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE_END);

            }
        });
        insertHandler=new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                finish();
                Log.i("ola","koo"+myCalendar);
                setAlarm(myCalendar,id);


            }
        };
        handlerInsertRound=new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                finish();
                setAlarm(backCalendar,id);


            }
        };
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if(!Settings.canDrawOverlays(addTripActivity.this)) {
                        errorWarningForNotGivingDrawOverAppsPermissions();
                    }
                }
                Boolean valid=validateInput();
                if(valid) {
                    String name = tripName.getText().toString();
                    String time = timeText.getText().toString();
                    String date = dateText.getText().toString();
                    String timeBack,dateBack;
                    if(editMode){
                        Trips tripObj=new Trips(name, strStartPoint, strEndPoint, 0, 0, time, date,editObj.allNotes(editObj.getNotesList()));
                        tripObj.setId(tripID);
                        viewModel.update(tripObj);
                        cancelAlarm(tripObj.getId());
                        setAlarm(tripObj.getCalender(),tripObj.getId());
                        finish();
                    }
                    else{
                        if (single.isChecked()){
                                new Thread()
                                {
                                    public void run() {
                                        Trips trip=new Trips(name, strStartPoint, strEndPoint, 0, 0, time, date,"");
                                        id= viewModel.insert(trip);
                                        insertHandler.sendEmptyMessage(0);
                                    }
                                }.start();
                        }
                        else if(round.isChecked()) {
                            if (validateBackTime()) {
                                timeBack = timeBackText.getText().toString();
                                dateBack = dateBackText.getText().toString();
                                new Thread()
                                {
                                    public void run() {
                                        id=viewModel.insert(new Trips(name, strStartPoint, strEndPoint, 0, 1, time, date, ""));
                                        insertHandler.sendEmptyMessage(0);
                                    }
                                }.start();
                                new Thread()
                                {
                                    public void run() {
                                        id=viewModel.insert(new Trips(name, strEndPoint, strStartPoint, 0, 1, timeBack, dateBack, ""));
                                        handlerInsertRound.sendEmptyMessage(0);
                                    }
                                }.start();
                            }
                            else
                                Toast.makeText(addTripActivity.this, "set valid back time", Toast.LENGTH_LONG).show();
                        }
                    }
                }
            }
        });



    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE_START) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                LatLng queriedLocation = place.getLatLng();
                strStartPoint=place.getName()+"#"+queriedLocation.latitude+"#"+queriedLocation.longitude;
                //startPoint.setText(place.getName()+"#"+queriedLocation.latitude+"#"+queriedLocation.longitude);
                startPoint.setText(place.getName());
                Log.i("loc", "Place: " + place.getName() + ", " + place.getId());
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i("loc", status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
            return;
        }
        else if(requestCode == AUTOCOMPLETE_REQUEST_CODE_END){
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
                LatLng queriedLocation = place.getLatLng();
                strEndPoint=place.getName()+"#"+queriedLocation.latitude+"#"+queriedLocation.longitude;
                //endPoint.setText(place.getName()+"#"+queriedLocation.latitude+"#"+queriedLocation.longitude);
                endPoint.setText(place.getName());
                Log.i("loc", "Place: " + place.getName() + ", " + place.getId());
            } else if (resultCode == AutocompleteActivity.RESULT_ERROR) {
                // TODO: Handle the error.
                Status status = Autocomplete.getStatusFromIntent(data);
                Log.i("loc", status.getStatusMessage());
            } else if (resultCode == RESULT_CANCELED) {
                // The user canceled the operation.
            }
            return;
        }

    }

    private boolean validateInput() {
        boolean isTime=validateTime();
        if (tripName.getText().toString().trim().length() ==0)
            Toast.makeText(addTripActivity.this, "Trip Name Is Empty", Toast.LENGTH_SHORT).show();
        else if (strStartPoint == "")
            Toast.makeText(addTripActivity.this, "Start Point Not Specified", Toast.LENGTH_SHORT).show();
        else if (strEndPoint == "")
            Toast.makeText(addTripActivity.this, "End Point Not Specified", Toast.LENGTH_SHORT).show();
        else if(!isTime)
            Toast.makeText(addTripActivity.this, "Set valid time", Toast.LENGTH_LONG).show();
        else if (timeText.getText().toString().trim().length() ==0)
            Toast.makeText(addTripActivity.this, "Time Not Specified", Toast.LENGTH_SHORT).show();
        else if (dateText.getText().toString().trim().length() ==0)
            Toast.makeText(addTripActivity.this, "Date Not Specified", Toast.LENGTH_SHORT).show();
        else if (round.isChecked()){
            if (timeBackText.getText().toString().trim().length() ==0)
                Toast.makeText(addTripActivity.this, "Round Time  Not Specified", Toast.LENGTH_SHORT).show();
            else if (dateBackText.getText().toString().trim().length() ==0)
                Toast.makeText(addTripActivity.this, "Round Date Not Specified", Toast.LENGTH_SHORT).show();
            else
                return true;
        }
        else
            return true;

        return false;
    }
    private void setAlarm(Calendar calendar,long id) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent broadcastIntent= new Intent(addTripActivity.this,NotificationReceiver.class);
        broadcastIntent.putExtra(notificationIntentKey,"say goodbye to your data");
        broadcastIntent.putExtra("id",id);
        PendingIntent pendingBroadcastIntent=PendingIntent.getBroadcast(addTripActivity.this, (int) id,
                broadcastIntent,0);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP,calendar.getTimeInMillis(),pendingBroadcastIntent);


    }
    private void errorWarningForNotGivingDrawOverAppsPermissions(){
        new AlertDialog.Builder(this).setTitle("Warning").setCancelable(false).setMessage("Unfortunately the display over other apps permission" +
                " is not granted so the application might not behave properly \nTo enable this permission kindly restart the application" )
                .setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                }).show();
    }
    private void cancelAlarm(int requestCode) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent broadcastIntent= new Intent(addTripActivity.this,NotificationReceiver.class);
        broadcastIntent.putExtra(notificationIntentKey,"say goodbye to your data");
        PendingIntent pendingBroadcastIntent=PendingIntent.getBroadcast(addTripActivity.this,requestCode,
                broadcastIntent,0);
        alarmManager.cancel(pendingBroadcastIntent);
        pendingBroadcastIntent.cancel();

    }

    private void initialize(){
        tDateFormant =new SimpleDateFormat("yyyy-MM-dd HH:mm");
        dateFormant =new SimpleDateFormat("dd-MM-yyyy");
        backCalendar = Calendar.getInstance();
        myCalendar = Calendar.getInstance();
        myCurrentCalendar= Calendar.getInstance();
        btnDate= (ImageView) findViewById(R.id.calender_btn);
        btnTime= (ImageView) findViewById(R.id.alarm_btn);
        dateText= (TextView)findViewById(R.id.dateText);
        timeText= (TextView)findViewById(R.id.alarmTextView);
        tripName=(EditText)findViewById(R.id.tripName);
        startPoint=(EditText)findViewById(R.id.startPoint);
        endPoint=(EditText)findViewById(R.id.endPoint);
        addBtn=(Button)findViewById(R.id.btn_add);
        single = findViewById(R.id.single_trip);
        round = findViewById(R.id.round_trip);
        dateBackText= (TextView)findViewById(R.id.date_back);
        timeBackText= (TextView)findViewById(R.id.alarm_back);
        btnDateBack= (ImageView) findViewById(R.id.calender_btn_back);
        btnTimeBack= (ImageView) findViewById(R.id.alarm_btn_back);
        round_details=(LinearLayout) findViewById(R.id.round_layout);
        trip_type=(LinearLayout) findViewById(R.id.tripTypeLayout);
        toolBar=(Toolbar) findViewById(R.id.toolbar);

        viewModel= ViewModelProviders.of(this).get(TripsViewModel.class);
    }
    private void setTime(){
        btnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myCalendar.set(Calendar.SECOND,0);
                mTimePicker.setTitle("Select Time");
                mTimePicker.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                mTimePicker.show();
            }
        });
        mTimePicker = new TimePickerDialog(addTripActivity.this,android.R.style.Theme_Holo_Light_Dialog_MinWidth, new TimePickerDialog.OnTimeSetListener() {
        @Override
        public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
            myCalendar.set(Calendar.HOUR_OF_DAY,selectedHour);
            myCalendar.set(Calendar.MINUTE,selectedMinute);
            isTimeSelect=true;
            String chosenTime = DateFormat.getTimeInstance().format(myCalendar.getTime());
            timeText.setText(chosenTime);
            btnTime.setColorFilter(getResources().getColor(R.color.purbleApp), PorterDuff.Mode.SRC_ATOP);

        }
    }, myCurrentCalendar.get(Calendar.HOUR_OF_DAY),  myCurrentCalendar.get(Calendar.MINUTE), android.text.format.DateFormat.is24HourFormat(getApplicationContext()));//Yes 24 hour time
       }
    private void setBackTime(){
        btnTimeBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isTimeSelect) {
                    mTimePickerBack.setTitle("Select Time");
                    mTimePickerBack.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    mTimePickerBack.show();
                }
                else
                    Toast.makeText(addTripActivity.this, "set the start Time ", Toast.LENGTH_LONG).show();
            }
        });
        mTimePickerBack = new TimePickerDialog(addTripActivity.this,android.R.style.Theme_Holo_Light_Dialog_MinWidth, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                backCalendar.set(Calendar.HOUR_OF_DAY,selectedHour);
                backCalendar.set(Calendar.MINUTE,selectedMinute);
                backCalendar.set(Calendar.SECOND,0);
                String chosenTime = DateFormat.getTimeInstance().format(backCalendar.getTime());
                timeBackText.setText(chosenTime);
                btnTimeBack.setColorFilter(getResources().getColor(R.color.purbleApp), PorterDuff.Mode.SRC_ATOP);

            }
        }, myCurrentCalendar.get(Calendar.HOUR_OF_DAY),  myCurrentCalendar.get(Calendar.MINUTE), android.text.format.DateFormat.is24HourFormat(getApplicationContext()));//Yes 24 hour time
      }
    private boolean validateTime(){
        Calendar myCurrentCalendar=Calendar.getInstance();
        try {
            currentDate = tDateFormant.parse(tDateFormant.format(myCurrentCalendar.getTime()));
            selectedDate = tDateFormant.parse(tDateFormant.format(myCalendar.getTime()));

        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (currentDate.after(selectedDate)) {
            timeText.setText("");
            return false;
        }
        else
            return true;
    }
    private boolean validateBackTime(){
        try {
            currentDate = tDateFormant.parse(tDateFormant.format(backCalendar.getTime()));
            selectedDate = tDateFormant.parse(tDateFormant.format(myCalendar.getTime()));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        if (currentDate.before(selectedDate)||currentDate.equals(selectedDate)) {
            Toast.makeText(addTripActivity.this, "Set valid back time..", Toast.LENGTH_LONG).show();
            timeBackText.setText("");
            return false;
        }
        else
            return true;

    }
    private void setDate(){
        DatePickerDialog.OnDateSetListener datePicker = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                dateBackText.setText("");
                btnDateBack.setColorFilter(getResources().getColor(R.color.gray), PorterDuff.Mode.SRC_ATOP);
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                isDateSelect=true;
                try {
                    currentDate = dateFormant.parse(dateFormant.format(myCurrentCalendar.getTime()));
                    selectedDate = dateFormant.parse(dateFormant.format(myCalendar.getTime()));
                    if (currentDate.before(selectedDate) || currentDate.equals(selectedDate)) {
                        btnDate.setColorFilter(getResources().getColor(R.color.purbleApp), PorterDuff.Mode.SRC_ATOP);
                        dateText.setText( dateFormant.format(myCalendar.getTime()));
                    } else
                        Toast.makeText(addTripActivity.this, "Set valid date", Toast.LENGTH_LONG).show();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
        };
        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePickerDialog datePickerDialog= new DatePickerDialog(addTripActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, datePicker, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.setTitle("Select Date");
                datePickerDialog.show();

            }
        });
    }
    private void setBackDate(){
        DatePickerDialog.OnDateSetListener dateBackPicker = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                backCalendar.set(Calendar.YEAR, year);
                backCalendar.set(Calendar.MONTH, monthOfYear);
                backCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                try {
                    selectedBackDate = dateFormant.parse(dateFormant.format(backCalendar.getTime()));
                    selectedDate = dateFormant.parse(dateFormant.format(myCalendar.getTime()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if (selectedDate.before(selectedBackDate) || selectedDate.equals(selectedBackDate)) {
                    btnDateBack.setColorFilter(getResources().getColor(R.color.purbleApp), PorterDuff.Mode.SRC_ATOP);
                    dateBackText.setText(dateFormant.format(backCalendar.getTime()));
                } else
                    Toast.makeText(addTripActivity.this, "Set valid date", Toast.LENGTH_LONG).show();
            }
        };
        btnDateBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isDateSelect) {
                    DatePickerDialog datePickerDialog = new DatePickerDialog(addTripActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, dateBackPicker, backCalendar
                            .get(Calendar.YEAR), backCalendar.get(Calendar.MONTH),
                            backCalendar.get(Calendar.DAY_OF_MONTH));
                    datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                    datePickerDialog.setTitle("Select Date");
                    datePickerDialog.show();
                }
                else
                    Toast.makeText(addTripActivity.this, "set the start date ", Toast.LENGTH_LONG).show();
            }
        });
    }

}