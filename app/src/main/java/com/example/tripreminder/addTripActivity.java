package com.example.tripreminder;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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

import com.google.android.gms.common.api.Status;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.widget.Autocomplete;
import com.google.android.libraries.places.widget.AutocompleteActivity;
import com.google.android.libraries.places.widget.model.AutocompleteActivityMode;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class addTripActivity extends AppCompatActivity {
    Toolbar toolBar;
    ImageView btnDate;
    TextView dateText;
    ImageView btnTime;
    TextView timeText;
    EditText tripName;
    EditText startPoint;
    EditText endPoint;
    Button addBtn;
    Spinner repeatingSpinner;
    Spinner tripTypeSpinner;
    Calendar myCurrentCalendar;
    Calendar myCalendar;
    TripsViewModel viewModel;
    DatabaseReference rootRef;
    private static int AUTOCOMPLETE_REQUEST_CODE = 1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);
        setSupportActionBar(toolBar);

        if (!Places.isInitialized()) {

            Places.initialize(getApplicationContext(), "AIzaSyALHrPpUa1o9Hc-6zTue0nt_CgM0tJa_pc");
        }
        // Set the fields to specify which types of place data to
        // return after the user has made a selection.

        // Create a new token for the autocomplete session. Pass this to FindAutocompletePredictionsRequest,
        // and once again when the user makes a selection (for example when calling fetchPlace()).
        // AutocompleteSessionToken token = AutocompleteSessionToken.newInstance();

      /*  // Create a RectangularBounds object.
        RectangularBounds bounds = RectangularBounds.newInstance(
                new LatLng(-33.880490, 151.184363),
                new LatLng(-33.858754, 151.229596));
        // Use the builder to create a FindAutocompletePredictionsRequest.
        FindAutocompletePredictionsRequest request = FindAutocompletePredictionsRequest.builder()
                // Call either setLocationBias() OR setLocationRestriction().
                .setLocationBias(bounds)
                //.setLocationRestriction(bounds)
                .setOrigin(new LatLng(-33.8749937,151.2041382))
                .setCountries("AU", "NZ")
                .setTypeFilter(TypeFilter.ADDRESS)
                .setSessionToken(token)
                .setQuery("query")
                .build();
        PlacesClient placesClient = Places.createClient(this);
        placesClient.findAutocompletePredictions(request).addOnSuccessListener((response) -> {
            for (AutocompletePrediction prediction : response.getAutocompletePredictions()) {
                Log.i("loc", prediction.getPlaceId());
                Log.i("loc", prediction.getPrimaryText(null).toString());
            }
        }).addOnFailureListener((exception) -> {
            if (exception instanceof ApiException) {
                ApiException apiException = (ApiException) exception;
                Log.e("loc", "Place not found: " + apiException.getStatusCode());
            }
        });*/


/*

        // Initialize the AutocompleteSupportFragment.
        AutocompleteSupportFragment autocompleteFragment = (AutocompleteSupportFragment)
                getSupportFragmentManager().findFragmentById(R.id.place_autocomplete_fragment);

        // Specify the types of place data to return.
        autocompleteFragment.setPlaceFields(Arrays.asList(Place.Field.ID, Place.Field.NAME));

        // Set up a PlaceSelectionListener to handle the response.
        autocompleteFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NotNull Place place) {
                startPoint.setText(place.getName());
            }
            @Override
            public void onError(Status status) {
                startPoint.setText(status.toString());
            }
        });*/

        myCalendar = Calendar.getInstance();
        myCurrentCalendar= Calendar.getInstance();
        toolBar=(Toolbar) findViewById(R.id.toolbar);
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

        // FirebaseApp.initializeApp(MainActivity.this);
        rootRef = FirebaseDatabase.getInstance().getReference("Trips");
        //DatabaseReference arrayTicketRef = rootRef.child();
        // arrayTicketRef.child("Trip");


        viewModel= ViewModelProviders.of(this).get(TripsViewModel.class);
        viewModel.getAllTrips().observe(this, new Observer<List<Trips>>() {
            @Override
            public void onChanged(List<Trips> trips) {
                for (Trips i:trips) {
                    Log.i("ola",i.getName());
                    // Log.i("ola",infos.get(1).getMsg());
                    Log.i("ola",i.toString());
                    // Log.i("ola",i.getType());
                }
                Toast.makeText(addTripActivity.this, "Added Successfully", Toast.LENGTH_SHORT).show();
            }
        });

        DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                String dateStr = dayOfMonth+"/"+(monthOfYear+1)+"/"+year;
                SimpleDateFormat requiredFormant =new SimpleDateFormat("yyyy-MM-dd");
                Date selectedDate = null;
                Date currentDate = null;
                //To get current Date
                try {
                    currentDate = requiredFormant.parse(requiredFormant.format(myCurrentCalendar.getTime()));
                    Toast.makeText(addTripActivity.this, "Set"+currentDate, Toast.LENGTH_SHORT).show();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                myCalendar.set(Calendar.YEAR, year);
                myCalendar.set(Calendar.MONTH, monthOfYear);
                myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
                try {
                    selectedDate = requiredFormant.parse(requiredFormant.format(myCalendar.getTime()));
                    // Toast.makeText(MainActivity.this, "Setjj"+selectedDate, Toast.LENGTH_SHORT).show();
                } catch (ParseException e) {
                    e.printStackTrace();
                }

                if(currentDate.before(selectedDate)||currentDate.equals(selectedDate)){
                    dateText.setText(dateStr);
                    Toast.makeText(addTripActivity.this, "Set"+currentDate, Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(addTripActivity.this, "Set valid date", Toast.LENGTH_SHORT).show();


            }
        };
        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnDate.setColorFilter(getResources().getColor(R.color.textColor),
                        PorterDuff.Mode.SRC_ATOP);
                // TODO Auto-generated method stub
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
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(addTripActivity.this,android.R.style.Theme_Holo_Light_Dialog_MinWidth, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                      /*  Calendar temp=Calendar.getInstance();
                        temp.set(Calendar.HOUR_OF_DAY,selectedHour);
                        temp.set(Calendar.MINUTE,selectedMinute);

                        if(temp.after(GregorianCalendar.getInstance())){
                            Toast.makeText(MainActivity.this, "Cannot select a future time", Toast.LENGTH_SHORT).show();
                        } else {
                            timeText.setText( selectedHour + ":" + selectedMinute);
                        }*/
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
                Boolean valid=validateInput();
                if(valid) {
                    String name = tripName.getText().toString();
                    String start = startPoint.getText().toString();
                    String end = endPoint.getText().toString();
                    String time = timeText.getText().toString();
                    String date = dateText.getText().toString();
                    String repeat = repeatingSpinner.getSelectedItem().toString();
                    String type = tripTypeSpinner.getSelectedItem().toString();
                    viewModel.insert(new Trips(name, start, end, "coming", type, repeat, time, date,""));
                }
                clearText();
            }
        });

       /* startPoint.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                List<Place.Field> locList=Arrays.asList(Place.Field.ADDRESS,Place.Field.LAT_LNG,Place.Field.NAME);
                Intent intent=new Autocomplete.IntentBuilder(AutocompleteActivityMode.OVERLAY,locList)
                        .build(MainActivity.this);
                startActivityForResult(intent,100);
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });*/

        startPoint.setOnFocusChangeListener((v, hasFocus) -> {
            if (hasFocus) {
                List<Place.Field> fields = Arrays.asList(Place.Field.ID, Place.Field.NAME);
                // Start the autocomplete intent.
                Intent intent = new Autocomplete.IntentBuilder(AutocompleteActivityMode.FULLSCREEN, fields)
                        .build(this);
                startActivityForResult(intent, AUTOCOMPLETE_REQUEST_CODE);

            }
        });

    }
    private boolean validateInput() {
        if (tripName.getText().toString().trim().length() ==0)
            Toast.makeText(addTripActivity.this, "Trip Name Is Empty", Toast.LENGTH_SHORT).show();
        else if (startPoint.getText().toString().trim().length() == 0)
            Toast.makeText(addTripActivity.this, "Start Point Not Specified", Toast.LENGTH_SHORT).show();
        else if (endPoint.getText().toString().trim().length() == 0)
            Toast.makeText(addTripActivity.this, "end Point Not Specified", Toast.LENGTH_SHORT).show();
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
    }

    @Override
    protected void onResume() {
        super.onResume();
        List<Trips> trips= viewModel.getAllTrips().getValue();
        rootRef.push().setValue(Arrays.asList(trips));
        //  LiveData<List<Trips>>trips= viewModel.getAllTrips();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == AUTOCOMPLETE_REQUEST_CODE) {
            if (resultCode == RESULT_OK) {
                Place place = Autocomplete.getPlaceFromIntent(data);
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
        super.onActivityResult(requestCode, resultCode, data);
    }




}