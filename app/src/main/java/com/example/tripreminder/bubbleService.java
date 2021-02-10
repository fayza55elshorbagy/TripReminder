package com.example.tripreminder;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.app.Service;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.graphics.drawable.ColorDrawable;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.tripreminder.adapters.notesAdapter;
import com.example.tripreminder.beans.HistoryListener;
import com.example.tripreminder.beans.Trips;
import com.example.tripreminder.fragments.Upcoming;
import com.txusballesteros.bubbles.BubbleLayout;

import java.util.ArrayList;

public class bubbleService extends Service {
    private BubbleLayout bubbleLayout;
    private WindowManager windowManager;
    private GestureDetector gestureDetector;
    ArrayList<String> noteList = new ArrayList<>();
    String[] Notes;
    Boolean[] ch ;
    public static final String ACTION_START = "com.floatingwidgetchathead_demo.SampleService.ACTION_START";
    public bubbleService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @SuppressLint("LogConditional")
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        String action = intent.getAction();
        //NoteList = intent.getStringArrayListExtra("NoteList");
        //System.out.println("ACTION: "+action);
        switch (action){
            case ACTION_START:
                Log.i("click", "onStartCommand: "+action);
                startingService(intent.getStringArrayListExtra("Intent"));
                break;
        }
        return START_STICKY;

    }
    @Override
    public void onCreate() {
        super.onCreate();
    }

    public void startingService(ArrayList<String> value){
       // Log.i("click",value.get(0));

        Notes = ArrayListTOArray(value);

        gestureDetector = new GestureDetector(this, new SingleTapConfirm());
        bubbleLayout = (BubbleLayout) LayoutInflater.from(this).inflate(R.layout.bubble_layout,null);
        WindowManager.LayoutParams params = new WindowManager.LayoutParams(
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.WRAP_CONTENT,
                WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY,
                WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE,
                PixelFormat.TRANSLUCENT);
        params.gravity = Gravity.TOP | Gravity.LEFT;
        params.x = 0;
        params.y = 100;

        windowManager = (WindowManager) getSystemService(WINDOW_SERVICE);
        windowManager.addView(bubbleLayout,params);
        ImageView img = bubbleLayout.findViewById(R.id.bubble_img);
        ImageView canncel = bubbleLayout.findViewById(R.id.bubble_cancel);
        img.setOnTouchListener(new View.OnTouchListener() {
            private  int initialX;
            private  int initialY;
            private  float touchX;
            private  float touchY;
            private int lastAction;

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (gestureDetector.onTouchEvent(motionEvent)) {
                    //Toast.makeText(bubbleService.this, "ddddddddddialog", Toast.LENGTH_SHORT).show();
                    showDialog();
                    return true;
                } else {
                    Log.i("click","Moving");
                    if(motionEvent.getAction() == MotionEvent.ACTION_DOWN)
                    {
                        initialX = params.x;
                        initialY = params.y;

                        touchX = motionEvent.getRawX();
                        touchY = motionEvent.getRawY();

                        lastAction = motionEvent.getAction();
                        return true;

                    }
                    if(motionEvent.getAction() == MotionEvent.ACTION_UP)
                    {
                        if(lastAction == MotionEvent.ACTION_DOWN)
                        {
                            Log.i("click","down");

                        }
                        lastAction = motionEvent.getAction();
                    }
                    if(motionEvent.getAction() == MotionEvent.ACTION_MOVE)
                    {
                        params.x = initialX + (int) (motionEvent.getRawX()-touchX);
                        params.y = initialY + (int) (motionEvent.getRawY()-touchY);

                        windowManager.updateViewLayout(bubbleLayout,params);
                        Log.i("click","moving");
                        lastAction = motionEvent.getAction();
                        return true;
                    }
                }

                return false;
            }
        });
        canncel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                stopSelf();
                bubbleLayout.setVisibility(View.INVISIBLE);
            }
        });
    }

   public void showDialog(){
       final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this, R.style.Theme_AppCompat_Dialog);
       dialogBuilder.setTitle("Notes");
       // Add a checkbox list
       dialogBuilder.setMultiChoiceItems(Notes,null, new DialogInterface.OnMultiChoiceClickListener() {
           @Override
           public void onClick(DialogInterface dialog, int which, boolean isChecked) {
               // The user checked or unchecked a box
           }
       });
       dialogBuilder.setNegativeButton("cancel",
               new DialogInterface.OnClickListener() {
                   @Override
                   public void onClick(DialogInterface dialog, int which) {
                       dialog.dismiss();
                   }
               }
       );

       final AlertDialog dialog = dialogBuilder.create();
       final Window dialogWindow = dialog.getWindow();
       final WindowManager.LayoutParams dialogWindowAttributes = dialogWindow.getAttributes();

       // Set fixed width (280dp) and WRAP_CONTENT height
       final WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
       lp.copyFrom(dialogWindowAttributes);
       lp.width = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 280, getResources().getDisplayMetrics());
       lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
       dialogWindow.setAttributes(lp);

      // Set to TYPE_SYSTEM_ALERT so that the Service can display it
       dialogWindow.setType(WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY);
       dialogWindowAttributes.windowAnimations = R.style.Widget_AppCompat_ListPopupWindow;
       dialog.show();
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        //windowManager.removeView(bubbleLayout);
       // bubbleLayout.setVisibility(View.INVISIBLE);
    }

    public String[] ArrayListTOArray (ArrayList<String> arr){
            /*ArrayList to Array Conversion */
            String array[] = new String[arr.size()];
            for(int j =0;j<arr.size();j++){
                array[j] = arr.get(j);
            }

            /*Displaying Array elements*/
            for(String k: array)
            {
                System.out.println(k);
            }
            return array;
        }

    private class SingleTapConfirm extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapUp(MotionEvent event) {
            return true;
        }
    }

}