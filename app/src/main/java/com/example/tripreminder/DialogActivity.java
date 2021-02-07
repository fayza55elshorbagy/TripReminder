package com.example.tripreminder;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;


public class DialogActivity extends AppCompatActivity {
    NotificationManagerCompat notificationManagerCompat;
    public static final String notificationIntentKey="notificationIntentKey";
    public static final String channel1ID="chan1";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dialog);
        notificationManagerCompat= NotificationManagerCompat.from(this);


        Log.e("NOTWORKING", "dialog activity");
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(DialogActivity.this);
        /*View view = LayoutInflater.from(DialogActivity.this).inflate(R.layout.activity_hacked,null);
        alertDialog.setView(view);
        alertDialog.create().show();*/
        final MediaPlayer mp = MediaPlayer.create(DialogActivity.this, R.raw.police);
        mp.start();
        mp.setLooping(true);
        alertDialog.setCancelable(false);
        alertDialog.setTitle("HOLA AMIGO").setMessage("QUE TAL?").setPositiveButton("start", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(DialogActivity.this, "you've clicked start", Toast.LENGTH_SHORT).show();
                finish();
                mp.stop();
            }
        }).setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(DialogActivity.this, "you've clicked cancel", Toast.LENGTH_SHORT).show();
                finish();
                mp.stop();
            }
        }).setNeutralButton("snooze", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(DialogActivity.this, "you've clicked snooze", Toast.LENGTH_SHORT).show();
                finish();
                mp.stop();
                showNotification();
            }
        });
        alertDialog.show();
    }
    private void showNotification(){
        if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.O){
            NotificationChannel channel1=new NotificationChannel(channel1ID,
                    "HITLER", NotificationManager.IMPORTANCE_HIGH);
            channel1.setDescription("YOU HAVE BEEN HACKED");
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel1);
        }
        Intent hackedIntent = new Intent(this, DialogActivity.class);
        hackedIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        PendingIntent pendingHackedIntent = PendingIntent.getActivity(this,1,
                hackedIntent,0);
        Intent broadcastIntent= new Intent(this, NotificationReceiver.class);
        broadcastIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        broadcastIntent.putExtra(notificationIntentKey,"say goodbye to your data");
        PendingIntent pendingBroadcastIntent=PendingIntent.getBroadcast(this,1,
                broadcastIntent,PendingIntent.FLAG_UPDATE_CURRENT);
        Notification notification = new NotificationCompat.Builder(this, channel1ID)
                .setSmallIcon(R.drawable.ic_warning)
                .setContentTitle("HITLER").setContentText("YOU HAVE BEEN HACKED")
                .setPriority(NotificationManager.IMPORTANCE_HIGH)
                .setContentIntent(pendingHackedIntent)
                .addAction(R.drawable.ic_warning,"TOAST",pendingBroadcastIntent)
                .setAutoCancel(true)
                .setOngoing(true)
                .build();
        notificationManagerCompat.notify(1,notification);
    }
}