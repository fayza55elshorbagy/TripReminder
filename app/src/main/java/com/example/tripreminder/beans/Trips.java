package com.example.tripreminder.beans;

import android.util.Log;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.ListIterator;

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
    // 0-single 1-round
    private int type;
    private String time;
    private String date;
    private String notes;



    @Ignore
    public Trips(){}


    public Trips(int id, String name, String startPoint, String endPoint, int status, int type, String time, String date, String notes) {
        Id = id;
        this.name = name;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.status = status;
        this.type = type;
        this.time = time;
        this.date = date;
        this.notes = notes;
    }

    public Trips(String name, String startPoint, String endPoint, int status, int type, String time, String date, String notes) {
        this.name = name;
        this.startPoint = startPoint;
        this.endPoint = endPoint;
        this.status = status;
        this.type = type;
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

    public void setNotesList(List<String> notes) {
        String allnotes="";
        for (String v:notes) {
            allnotes+=v+"#";
        }
        this.notes = allnotes;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStartLoc() {
        String [] split=startPoint.split("#");
        return split[0];
    }
    public String getEndLoc() {
        String [] split1=endPoint.split("#");
        return split1[0];
    }
    public String getStartLat() {
        String [] split=startPoint.split("#");
        return split[1];
    }

    public String getStartLng() {
        String [] split=startPoint.split("#");
        return split[2];
    }

    public String getEndLat() {
        String [] split=endPoint.split("#");
        return split[1];
    }

    public String getEndLng() {
        String [] split=endPoint.split("#");
        return split[2];
    }


    public String getStartPoint() {
        return startPoint;
    }

    public void setStartPoint(String startPoint) {
        this.startPoint = startPoint;
    }


    public String getEndPoint() {
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

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
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
                ", time='" + time + '\'' +
                ", date='" + date + '\'' +
                ", notes='" + notes + '\'' +
                '}';
    }

    public void setId(int id) {
        Id = id;
    }
    public Calendar getCalender(){
        String [] mdate=date.split("-");
        String [] mtime=time.split(":");
        Log.i("ola","ttttime:"+time+"  "+ mtime[0]);
        Calendar myCalendar = Calendar.getInstance();
        myCalendar.set(Calendar.YEAR, Integer.parseInt(mdate[2]));
        myCalendar.set(Calendar.MONTH, Integer.parseInt(mdate[1]));
        myCalendar.set(Calendar.DAY_OF_MONTH, Integer.parseInt(mdate[0]));
        myCalendar.set(Calendar.HOUR_OF_DAY,Integer.parseInt(mtime[0]));
        myCalendar.set(Calendar.MINUTE,Integer.parseInt(mtime[1]));
        myCalendar.set(Calendar.SECOND,0);
        return myCalendar;
    }

}
