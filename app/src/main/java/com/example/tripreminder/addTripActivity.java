package com.example.tripreminder;


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
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProviders;

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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
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
    private Spinner repeatingSpinner;
    private Spinner tripTypeSpinner;
    private Calendar myCurrentCalendar;
    private Calendar myCalendar;
    private TripsViewModel viewModel;
    private Boolean editMode;
    private int tripID;
    private Trips editObj;
    private String strStartPoint="";
    private String strEndPoint="";


    private static int AUTOCOMPLETE_REQUEST_CODE_START = 1;
    private static int AUTOCOMPLETE_REQUEST_CODE_END = 2;
    public static String TRIP_ID = "Edit_Trip";
    public static String TRIP_OBJ = "Trip_OBJ";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);
        setSupportActionBar(toolBar);


        if (!Places.isInitialized()) {
            Places.initialize(getApplicationContext(), "AIzaSyALHrPpUa1o9Hc-6zTue0nt_CgM0tJa_pc", Locale.getDefault());
        }


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
        repeatingSpinner=(Spinner)findViewById(R.id.repeating_spinner);
        tripTypeSpinner=(Spinner)findViewById(R.id.trip_type);
        toolBar=(Toolbar) findViewById(R.id.toolbar);

        toolBar.setTitle("Add Trip");

        viewModel= ViewModelProviders.of(this).get(TripsViewModel.class);

        Intent intentTrip =getIntent();
        if(intentTrip.hasExtra(TRIP_ID)){
            toolBar.setTitle("Edit Trip");
             editMode=true;
             editObj= (Trips) getIntent().getSerializableExtra(TRIP_OBJ);

             strStartPoint=editObj.getStartLoc().toString();
             strEndPoint=editObj.getEndLoc().toString();

             tripID=intentTrip.getIntExtra(TRIP_ID,-1);
             tripName.setText(editObj.getName().toString());
             startPoint.setText(editObj.getStartPoint().toString());
             endPoint.setText(editObj.getEndPoint().toString());
             timeText.setText(editObj.getTime().toString());
             dateText.setText(editObj.getDate().toString());
             //repeatingSpinner.getSelectedItem().toString();
             //tripTypeSpinner.getSelectedItem().toString();

        }
        else{
            editMode=false;
            toolBar.setTitle("Add Trip");
        }

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String dateStr = dayOfMonth+"/"+(monthOfYear+1)+"/"+year;
                SimpleDateFormat requiredFormant =new SimpleDateFormat("yyyy-MM-dd");
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                Date selectedDate = null;
                Date currentDate = null;
                //To get current Date
                try {
                    currentDate = requiredFormant.parse(requiredFormant.format(myCurrentCalendar.getTime()));
                    selectedDate = requiredFormant.parse(requiredFormant.format(myCalendar.getTime()));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                if(currentDate.before(selectedDate)||currentDate.equals(selectedDate)){
                    dateText.setText(dateStr);
                }
                else
                    Toast.makeText(addTripActivity.this, "Set valid date", Toast.LENGTH_LONG).show();
            }
        };
        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnDate.setColorFilter(getResources().getColor(R.color.textColor), PorterDuff.Mode.SRC_ATOP);
                DatePickerDialog datePickerDialog= new DatePickerDialog(addTripActivity.this, android.R.style.Theme_Holo_Light_Dialog_MinWidth, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH));
                datePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                datePickerDialog.setTitle("Select Date");
                datePickerDialog.show();
            }
        });
        btnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnTime.setColorFilter(getResources().getColor(R.color.textColor), PorterDuff.Mode.SRC_ATOP);
                //  Calendar mcurrentTime = Calendar.getInstance();
                int hour = myCalendar.get(Calendar.HOUR_OF_DAY);
                int minute = myCalendar.get(Calendar.MINUTE);
                myCalendar.set(Calendar.SECOND,0);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(addTripActivity.this,android.R.style.Theme_Holo_Light_Dialog_MinWidth, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        myCalendar.set(Calendar.HOUR_OF_DAY,selectedHour);
                        myCalendar.set(Calendar.MINUTE,selectedMinute);
                      /*  Calendar temp=Calendar.getInstance();
                        temp.set(Calendar.HOUR_OF_DAY,selectedHour);
                        temp.set(Calendar.MINUTE,selectedMinute);

                        if(temp.after(GregorianCalendar.getInstance())){
                            Toast.makeText(addTripActivity.this, "Cannot select a future time"+   temp.after(GregorianCalendar.getInstance()), Toast.LENGTH_SHORT).show();
                        } else {
                            timeText.setText( selectedHour + ":" + selectedMinute);
                        }*/

                        // Date currentTime = Calendar.getInstance().getTime();
                        //Toast.makeText(addTripActivity.this, "future time"+currentTime, Toast.LENGTH_LONG).show();
                        timeText.setText( selectedHour + ":" + selectedMinute);

                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                mTimePicker.show();

            }
        });
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    if(!Settings.canDrawOverlays(addTripActivity.this)) {
                        errorWarningForNotGivingDrawOverAppsPermissions();
                    }
                }
                Log.e("trip", DateFormat.getDateTimeInstance().format(myCalendar.getTime()));
                Boolean valid=validateInput();
                if(valid) {
                    String name = tripName.getText().toString();
                   // String start = startPoint.getText().toString();
                   // String end = endPoint.getText().toString();
                    String time = timeText.getText().toString();
                    String date = dateText.getText().toString();
                    String repeat = repeatingSpinner.getSelectedItem().toString();
                    String type = tripTypeSpinner.getSelectedItem().toString();

                    if(editMode){

                        Trips tripObj=new Trips(name, strStartPoint, strEndPoint, 0, type, repeat, time, date,editObj.allNotes(editObj.getNotesList()));
                        tripObj.setId(tripID);
                        viewModel.update(tripObj);
                        finish();
                    }
                    else{
                        viewModel.insert(new Trips(name, strStartPoint, strEndPoint, 0, type, repeat, time, date,""));
                        clearText();
                    }

                    Log.e("trip", DateFormat.getDateTimeInstance().format(myCalendar.getTime()));
                    setAlarm(myCalendar);

                }

            }
        });

      /* startPoint.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }


            @Override
            public void afterTextChanged(Editable s) {

            }
        });
*/
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

    }
    private boolean validateInput() {
        if (tripName.getText().toString().trim().length() ==0)
            Toast.makeText(addTripActivity.this, "Trip Name Is Empty", Toast.LENGTH_SHORT).show();
        else if (startPoint.getText().toString().trim().length() == 0)
            Toast.makeText(addTripActivity.this, "Start Point Not Specified", Toast.LENGTH_SHORT).show();
        else if (endPoint.getText().toString().trim().length() == 0)
            Toast.makeText(addTripActivity.this, "End Point Not Specified", Toast.LENGTH_SHORT).show();
        else if (timeText.getText().toString().trim().length() ==0)
            Toast.makeText(addTripActivity.this, "Time Not Specified", Toast.LENGTH_SHORT).show();
        else if (dateText.getText().toString().trim().length() ==0)
            Toast.makeText(addTripActivity.this, "Date Not Specified", Toast.LENGTH_SHORT).show();
        else
            return true;

        return false;
    }
    private void clearText(){
        startPoint.setText("");
        endPoint.setText("");
        tripName.setText("");
        dateText.setText("");
        timeText.setText("");
        repeatingSpinner.setSelection(0);
        repeatingSpinner.setSelection(0);
        btnTime.setColorFilter(getResources().getColor(R.color.gray), PorterDuff.Mode.SRC_ATOP);
        btnDate.setColorFilter(getResources().getColor(R.color.gray), PorterDuff.Mode.SRC_ATOP);
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

    private void setAlarm(Calendar calendar) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent broadcastIntent= new Intent(addTripActivity.this,NotificationReceiver.class);
        broadcastIntent.putExtra(notificationIntentKey,"say goodbye to your data");
        PendingIntent pendingBroadcastIntent=PendingIntent.getBroadcast(addTripActivity.this,7,
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
}