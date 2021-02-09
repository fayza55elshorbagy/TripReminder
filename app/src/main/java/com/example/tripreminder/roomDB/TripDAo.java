package com.example.tripreminder.roomDB;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.example.tripreminder.beans.Trips;

import java.util.List;

@Dao
public interface TripDAo {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<Trips> trips);

    @Insert
    long insert( Trips trips);
    @Update
    Void update( Trips trips);
    @Delete
    Void delete( Trips trips);

    @Query("DELETE From Trips")
    void deleteAll();

    @Query("select * From Trips")
    LiveData<List<Trips>> getAllTrips();

    @Query("select * From Trips")
    List<Trips> getAll();

    @Query("select * From Trips where status=0")
    List<Trips> getUpComing();

    @Query("select * From Trips where status =2")
    List<Trips> getDoneTrips();

    @Query("select * From Trips where Id =:tId")
    Trips getTripById(long tId);




}

