package com.example.tripreminder.adapters;

import com.example.tripreminder.beans.Trips;

public interface TripListener {
    void edit(Trips trip);
    void delete(Trips trip);
    void startNav(Trips trip);
    void cancel(Trips trip);
    void showNote(Trips trip);


}
