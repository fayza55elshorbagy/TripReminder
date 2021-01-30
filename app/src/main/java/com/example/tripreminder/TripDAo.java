package com.example.tripreminder;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface TripDAo {
    @Insert
    Void insert( Trips trips);
    @Update
    Void update( Trips trips);
    @Delete
    Void delete( Trips trips);

    @Query("DELETE From Trips")
    void deleteAll();

    @Query("select * From Trips")
    LiveData<List<Trips>> getAllTrips();


}

