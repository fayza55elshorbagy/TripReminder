package com.example.tripreminder;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import com.example.tripreminder.beans.Trips;
import com.example.tripreminder.roomDB.TripsViewModel;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.maps.DirectionsApi;
import com.google.maps.DirectionsApiRequest;
import com.google.maps.GeoApiContext;
import com.google.maps.model.DirectionsLeg;
import com.google.maps.model.DirectionsResult;
import com.google.maps.model.DirectionsRoute;
import com.google.maps.model.DirectionsStep;
import com.google.maps.model.EncodedPolyline;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ExecutionException;

public class MapActivity extends AppCompatActivity implements OnMapReadyCallback {
    GoogleMap map;
    MarkerOptions place1,place2,place3,place4;
    private String TAG = "so47492459";
    TripsViewModel viewModel;
    List<Trips> doneTrips;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        /*place1=new MarkerOptions().position(new LatLng(30.585810,31.503500)).title("Zagazig uni");
        place2=new MarkerOptions().position(new LatLng(30.593820,32.269950)).title("Ismailia uni");
        place3=new MarkerOptions().position(new LatLng(30.044420,31.235712)).title("Cairo");
        place4=new MarkerOptions().position(new LatLng(31.197730,29.892540)).title("Alex");*/
        viewModel= ViewModelProviders.of(this).get(TripsViewModel.class);
        try {
            doneTrips=viewModel.getDoneTrips();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        map=googleMap;
        for(Trips trip : doneTrips){
            place1=new MarkerOptions().position(new LatLng(Double.parseDouble(trip.getStartLat()),Double.parseDouble(trip.getStartLng()))).title(trip.getStartLoc());
            place2=new MarkerOptions().position(new LatLng(Double.parseDouble(trip.getEndLat()),Double.parseDouble(trip.getEndLng()))).title(trip.getEndLoc());
            drawOnMap(place1,place2);
        }


    }
    private void drawOnMap(MarkerOptions place1,MarkerOptions place2){
        map.addMarker(place1);
        map.addMarker(place2);

        //Define list to get all latlng for the route
        List<LatLng> path = new ArrayList();

        //Execute Directions API request
        GeoApiContext context = new GeoApiContext.Builder()
                .apiKey("AIzaSyALHrPpUa1o9Hc-6zTue0nt_CgM0tJa_pc")
                .build();
        DirectionsApiRequest req = DirectionsApi.getDirections(context, ""+place1.getPosition().latitude+","+place1.getPosition().longitude, ""+place2.getPosition().latitude+","+place2.getPosition().longitude);

        try {
            DirectionsResult res = req.await();

            //Loop through legs and steps to get encoded polylines of each step
            if (res.routes != null && res.routes.length > 0) {
                DirectionsRoute route = res.routes[0];

                if (route.legs !=null) {
                    for(int i=0; i<route.legs.length; i++) {
                        DirectionsLeg leg = route.legs[i];
                        if (leg.steps != null) {
                            for (int j=0; j<leg.steps.length;j++){
                                DirectionsStep step = leg.steps[j];
                                if (step.steps != null && step.steps.length >0) {
                                    for (int k=0; k<step.steps.length;k++){
                                        DirectionsStep step1 = step.steps[k];
                                        EncodedPolyline points1 = step1.polyline;
                                        if (points1 != null) {
                                            //Decode polyline and add points to list of route coordinates
                                            List<com.google.maps.model.LatLng> coords1 = points1.decodePath();
                                            for (com.google.maps.model.LatLng coord1 : coords1) {
                                                path.add(new LatLng(coord1.lat, coord1.lng));
                                            }
                                        }
                                    }
                                } else {
                                    EncodedPolyline points = step.polyline;
                                    if (points != null) {
                                        //Decode polyline and add points to list of route coordinates
                                        List<com.google.maps.model.LatLng> coords = points.decodePath();
                                        for (com.google.maps.model.LatLng coord : coords) {
                                            path.add(new LatLng(coord.lat, coord.lng));
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch(Exception ex) {

        }

        //Draw the polyline
        if (path.size() > 0) {
            Random rnd = new Random();
            int randomColor = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
            Log.e("COLOR",""+randomColor);
            PolylineOptions opts = new PolylineOptions().addAll(path).color(randomColor).width(5);
            map.addPolyline(opts);
        }
        
        map.getUiSettings().setZoomControlsEnabled(true);
        LatLng latLng = new LatLng( 24.09082,34.89005);
        Location l = new Location("L");
        l.setLatitude(24.09082);
        l.setLongitude(34.89005);
        CameraPosition cameraPosition = new CameraPosition.Builder().target(latLng).zoom(5).bearing(l.getBearing()).tilt(45).build();
        CameraUpdate cameraUpdate = CameraUpdateFactory.newCameraPosition(cameraPosition);
        map.animateCamera(cameraUpdate);

    }
}