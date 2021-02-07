package com.example.tripreminder;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.Toast;

import static com.example.tripreminder.DialogActivity.notificationIntentKey;

public class NotificationReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        String msg=intent.getStringExtra(DialogActivity.notificationIntentKey);
        Toast.makeText(context.getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
        long id=intent.getLongExtra("id",-1);
        Intent dialogIntent = new Intent(context,DialogActivity.class);
        dialogIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK|Intent.FLAG_ACTIVITY_CLEAR_TASK);
        dialogIntent.putExtra("mid",id);
        context.startActivity(dialogIntent);
        Log.e("RECEIVER","WORKING");
    }

}
