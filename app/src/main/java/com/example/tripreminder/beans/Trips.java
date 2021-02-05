package com.example.tripreminder.beans;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@Entity (tableName = "Trips")
public class Trips implements Serializable {
    @PrimaryKey(autoGenerate = true)
    private int Id;
    @ColumnInfo(name = "TripName")
    private String name;
    private String startPoint;
    private String endPoint;

    //0-upcoming . 1- cancel . 2-done
    private int status;
    private String type;
    private String repeating;
    private String time;
    private String date;
    private String notes;

    public Trips(String name, String startPoint, String endPoint, int status, String type, String repeating, String time, String date,String notes) {
        this.name = name;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.status = status;
        this.type = type;
        this.repeating = repeating;
        this.time = time;
        this.date = date;
        this.notes=notes;
    }
    public String allNotes(List<String> notes) {
        String allnotes="";
        for (String v:notes) {
            allnotes+=v+"#";
        }
      return allnotes;
    }

    public ArrayList<String> getNotesList() {
        String[] note = notes.split("#");
        ArrayList<String> notes=new ArrayList<>();
        for (String n:note) {
            if(n!="")
            notes.add(n);
        }

        return notes;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public void setNotesList(List<String> notes) {
        String allnotes="";
        for (String v:notes) {
            allnotes+=v+"#";
        }
        this.notes = allnotes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartPoint() {
        String [] split=startPoint.split("#");
        return split[0];
    }

    public String getStartLoc() {
        return startPoint;
    }
    public void setStartPoint(String startPoint) {
        this.startPoint = startPoint;
    }

    public String getEndPoint() {
        String [] split1=endPoint.split("#");
        return split1[0];
    }
    public String getEndLoc() {
        return endPoint;
    }

    public void setEndPoint(String endPoint) {
        this.endPoint = endPoint;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getRepeating() {
        return repeating;
    }

    public void setRepeating(String repeating) {
        this.repeating = repeating;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getId() {
        return Id;
    }

    @Override
    public String toString() {
        return "Trips{" +
                "Id=" + Id +
                ", name='" + name + '\'' +
                ", startPoint='" + startPoint + '\'' +
                ", endPoint='" + endPoint + '\'' +
                ", status='" + status + '\'' +
                ", type='" + type + '\'' +
                ", repeating='" + repeating + '\'' +
                ", time='" + time + '\'' +
                ", date='" + date + '\'' +
                ", notes='" + notes + '\'' +
                '}';
    }

    public void setId(int id) {
        Id = id;
    }

}
