package com.example.tripreminder.beans;

public interface HistoryListener {
    void delete(Trips trip);
    void showNote(Trips trip);
}
