package com.example.tripreminder.roomDB;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.tripreminder.beans.Trips;

@Database(entities= Trips.class,version=2,exportSchema = false)
public abstract class TripRoomDb extends RoomDatabase {
    private static TripRoomDb instance;
    public abstract TripDAo infoDAo();

    public static synchronized TripRoomDb getInstance(Context context) {
        if(instance==null){
            instance=Room.databaseBuilder(context.getApplicationContext(),
                TripRoomDb.class, "Trips-DB") .build();
        }
        return  instance;
    }


/*
.fallbackToDestructiveMigration().addCallback(rommCallBack)
    private static RoomDatabase.Callback rommCallBack=new RoomDatabase.Callback(){
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDataAsyncTask(instance).execute();
        }

        @Override
        public void onOpen(@NonNull SupportSQLiteDatabase db) {
            super.onOpen(db);
        }
    };
    private static class PopulateDataAsyncTask extends AsyncTask<Void,Void, Void>
    {
        private TripDAo mTripDao;
        PopulateDataAsyncTask(TripRoomDb db)
        {
            mTripDao = db.infoDAo();
        }
        @Override
        protected Void doInBackground(Void... voids) {
           mTripDao.insert(new Trips("ola", "0123","cairo","ooo","re","ii","890","jii","klklk/jkn"));
            return null;
        }
    }*/
}
