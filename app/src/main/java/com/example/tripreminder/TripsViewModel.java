package com.example.tripreminder;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.List;


public class TripsViewModel extends AndroidViewModel {
    private TripRepo mRepository;
    private LiveData<List<Trips>> mAllTrips;
    public TripsViewModel(@NonNull Application application) {
        super(application);
        mRepository = new TripRepo(application);
        mAllTrips = mRepository.getAllTrips();
    }
    public void insert(Trips trips) {
        mRepository.insert(trips);
    }
    public void delete(Trips trips) {
        mRepository.delete(trips);
    }
    public void update(Trips trips) {
        mRepository.update(trips);
    }
    public void deleteAllTrips() {
        mRepository.deleteAllTrips();
    }
    public LiveData<List<Trips>> getAllTrips() {
        return mAllTrips;
    }
}