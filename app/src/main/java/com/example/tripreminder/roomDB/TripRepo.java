package com.example.tripreminder.roomDB;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import com.example.tripreminder.beans.Trips;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class TripRepo {

        private TripDAo mTripDao;
        private LiveData<List<Trips>> getAllTrips;

        public TripRepo(Application app)
        {
            TripRoomDb db = TripRoomDb.getInstance(app);
            mTripDao = db.infoDAo();
            getAllTrips = mTripDao.getAllTrips();

        }
        //operation
        //insert
        public void insert(Trips trips)
        {
            new InsertAsyncTask(mTripDao).execute(trips);
        }
        //delete
        public void delete(Trips trips)
        {
            new DeleteAsyncTask(mTripDao).execute(trips);
        }
        //update
        public void update(Trips trips)
        {
            new UpdateAsyncTask(mTripDao).execute(trips);
        }
        //getalltrips
        public LiveData<List<Trips>> getAllTrips()
        {
            return getAllTrips;
        }
        //delete all trips
        public void deleteAllTrips()
        {
            new DeleteAllTripsAsyncTask(mTripDao).execute();
        }
        //get All
        public List<Trips> getAll()throws ExecutionException, InterruptedException { return new GetAllsAsyncTask(mTripDao).execute().get(); }


    private static class InsertAsyncTask extends AsyncTask<Trips, Void, Void> {
    private TripDAo minfoDao;
    public InsertAsyncTask(TripDAo tripDao)
    {
        minfoDao = tripDao;
    }
    @Override
    protected Void doInBackground(Trips... trips) {
        minfoDao.insert(trips[0]);
        return null;
    }
}
    private static class DeleteAsyncTask extends AsyncTask<Trips, Void, Void>{
    private TripDAo minfoDao;
    public DeleteAsyncTask(TripDAo tripDao)
    {
        minfoDao = tripDao;
    }
    @Override
    protected Void doInBackground(Trips... trips) {
        minfoDao.delete(trips[0]);
        return null;
    }
}
    private static class UpdateAsyncTask extends AsyncTask<Trips, Void, Void>{
    private TripDAo minfoDao;
    public UpdateAsyncTask(TripDAo tripDao)
    {
        minfoDao = tripDao;
    }
    @Override
    protected Void doInBackground(Trips... trips) {
        minfoDao.update(trips[0]);
        return null;
    }
}
    private static class DeleteAllTripsAsyncTask extends AsyncTask<Void, Void, Void>{
    private TripDAo mTripsDao;
    public DeleteAllTripsAsyncTask(TripDAo wordsDao)
    {
        mTripsDao = wordsDao;
    }
    @Override
    protected Void doInBackground(Void... voids) {
        mTripsDao.deleteAll();
        return null;
    }
}
    private class GetAllsAsyncTask extends AsyncTask<Void, Void,List<Trips>> {
        private TripDAo mAsyncTaskDao;

        GetAllsAsyncTask(TripDAo dao) {
            mAsyncTaskDao = dao;
        }

        @Override
        protected List<Trips> doInBackground(Void... voids) {
            return mAsyncTaskDao.getAll();
        }
    }
}



