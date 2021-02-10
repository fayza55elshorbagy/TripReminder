package com.example.tripreminder;

import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.provider.Settings;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;
import androidx.lifecycle.ViewModelProviders;

import com.example.tripreminder.beans.Trips;
import com.example.tripreminder.roomDB.TripsViewModel;

import java.text.DateFormat;
import java.util.List;
import java.util.concurrent.ExecutionException;
import static java.lang.Double.parseDouble;


public class DialogActivity extends AppCompatActivity {
    NotificationManagerCompat notificationManagerCompat;
    public static final String notificationIntentKey="notificationIntentKey";
    public static final String channel1ID="chan1";
    private TripsViewModel viewModel;

    double endLatitude;
    double endLongitude;
    private int MY_PERMISSION = 100;
    long TrripId;
    Trips l;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
        notificationManagerCompat= NotificationManagerCompat.from(this);


        Log.e("NOTWORKING", "dialog activity");
         Intent intent= getIntent();
         TrripId=  intent.getLongExtra("mid",-1);
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(DialogActivity.this);
        /*View view = LayoutInflater.from(DialogActivity.this).inflate(R.layout.activity_hacked,null);
        alertDialog.setView(view);
        alertDialog.create().show();*/
        viewModel= ViewModelProviders.of(this).get(TripsViewModel.class);
        try {
            l=viewModel.getTripById(TrripId);
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        final MediaPlayer mp = MediaPlayer.create(DialogActivity.this, R.raw.police);
        mp.start();
        mp.setLooping(true);
        alertDialog.setCancelable(false);
        Handler cancelHandler=new Handler(){
            @Override
            public void handleMessage(@NonNull Message msg) {
                super.handleMessage(msg);
                finish();
                mp.stop();

            }
        };
        alertDialog.setTitle("Reminder").setMessage("Do You want to start your trip titled: "+l.getName()).setPositiveButton("start", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                    endLatitude = parseDouble(l.getEndLat());
                    endLongitude = parseDouble(l.getEndLng());
                    Log.i("click",endLatitude+"+++++++"+endLongitude);
                    new Thread()
                    {   public void run() {
                        Trips trip=new Trips(l.getName(),l.getStartPoint(),l.getEndPoint(),2,l.getType(),l.getTime(),l.getDate(),l.getNotes());
                        trip.setId((int) TrripId);
                        viewModel.update(trip);
                        Log.i("insert","alarm1"+l.getId()+"name"+l.getName()+"status"+l.getStatus());
                        cancelHandler.sendEmptyMessage(0);
                    }
                    }.start();

                    Thread splash = new Thread()
                    {
                        @Override
                        public void run() {
                            super.run();
                            try {
                                sleep(1000);
                                checkBubblePermission(l);
                                finish();
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                        }
                    };
                    splash.start();



                startGoogleActivityFromdialog();
                //Toast.makeText(DialogActivity.this, "you've clicked start", Toast.LENGTH_SHORT).show();
            }
        }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               try {
                   Trips l=viewModel.getTripById(TrripId);
                           new Thread()
                           {       public void run() {
                                   Trips trip=new Trips(l.getName(),l.getStartPoint(),l.getEndPoint(),1,l.getType(),l.getTime(),l.getDate(),l.getNotes());
                                   trip.setId((int) TrripId);
                                   viewModel.update(trip);
                                   Log.i("insert","alarm1"+l.getId()+"name"+l.getName()+"status"+l.getStatus());
                                   cancelHandler.sendEmptyMessage(0);
                               }
                           }.start();

                } catch (ExecutionException e) {
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                //Toast.makeText(DialogActivity.this, "you've clicked cancel", Toast.LENGTH_SHORT).show();
            }
        }).setNeutralButton("snooze", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
               // Toast.makeText(DialogActivity.this, "you've clicked snooze", Toast.LENGTH_SHORT).show();
                finish();
                mp.stop();
                showNotification();
            }
        });
        alertDialog.show();
    }

    private void startGoogleActivityFromdialog() {
        //Log.i("click",endLatitude+"++FromGoogle+++"+endLongitude);
        //Log.i("click",MainActivity.latitude+"+++++++"+MainActivity.longitude);
        String url = "http://maps.google.com/maps?saddr="
                + MainActivity.latitude + "," + MainActivity.longitude+ "&daddr=" + endLatitude+ "," + endLongitude;
            Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(url));
            intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
            startActivity(intent);
    }
    public void checkBubblePermission(Trips trip) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !Settings.canDrawOverlays(this)) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                    Uri.parse("package:" + this.getPackageName()));
            startActivityForResult(intent, MY_PERMISSION);
        } else {
            Log.i("click","**********TripFromCheckPremission");
            showBubbles(trip);
        }

    }

    private void showBubbles(Trips trip) {
        Log.i("click","**********Trip from sending service");
        Intent i = new Intent(this,bubbleService.class);
        i.setAction(bubbleService.ACTION_START);
        i.putStringArrayListExtra("Intent",trip.getNotesList());
        startService(i);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == MY_PERMISSION) {
            if (resultCode == RESULT_OK) {
                //showBubbles(trip);
            }
        }


    }


    private void showNotification(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel channel1=new NotificationChannel(channel1ID,
                    "Reminder", NotificationManager.IMPORTANCE_HIGH);
            channel1.setDescription("your trip is now");
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel1);
        }
        Intent hackedIntent = new Intent(this, DialogActivity.class);
        hackedIntent.putExtra("mid",TrripId);
        hackedIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingHackedIntent = PendingIntent.getActivity(this, (int) TrripId,
                hackedIntent,0);
        Intent broadcastIntent= new Intent(this, NotificationReceiver.class);
        broadcastIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        broadcastIntent.putExtra(notificationIntentKey,"say goodbye to your data");
        PendingIntent pendingBroadcastIntent=PendingIntent.getBroadcast(this,1,
                broadcastIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new NotificationCompat.Builder(this, channel1ID)
                .setSmallIcon(R.drawable.ic_warning)
                .setContentTitle("Reminder").setContentText("start your trip")
                .setPriority(NotificationManager.IMPORTANCE_HIGH)
                .setContentIntent(pendingHackedIntent)
                .setAutoCancel(true)
                .setOngoing(true)
                .build();
        notificationManagerCompat.notify(1,notification);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //stopService(new Intent(this,bubbleService.class));
    }
}