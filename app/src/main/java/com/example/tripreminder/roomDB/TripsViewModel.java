package com.example.tripreminder.roomDB;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.example.tripreminder.beans.Trips;

import java.util.List;
import java.util.concurrent.ExecutionException;


public class TripsViewModel extends AndroidViewModel {
    private TripRepo mRepository;
    private LiveData<List<Trips>> mAllTrips;
    public TripsViewModel(@NonNull Application application) {
        super(application);
        mRepository = new TripRepo(application);
        mAllTrips = mRepository.getAllTrips();
    }
    public long insert(Trips trips) {
      return  mRepository.insert(trips);
    }
    public void insertAll(List<Trips> trips) {
         mRepository.insertAll(trips);
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
    public List<Trips> getAll() throws ExecutionException, InterruptedException { return mRepository.getAll(); }
    public Trips getTripById(long id) throws ExecutionException, InterruptedException { return mRepository.getTripById(id); }
}