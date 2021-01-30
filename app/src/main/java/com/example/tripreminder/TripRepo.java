package com.example.tripreminder;

import android.app.Application;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

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
        //getallwords
        public LiveData<List<Trips>> getAllTrips()
        {
            return getAllTrips;
        }
        //delete all words
        public void deleteAllTrips()
        {
            new DeleteAllTripsAsyncTask(mTripDao).execute();
        }
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

}