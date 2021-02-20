package com.example.tripreminder.adapters;

import com.example.tripreminder.beans.Trips;

public interface HistoryListener {
    void delete(Trips trip);
    void showNote(Trips trip);
}
