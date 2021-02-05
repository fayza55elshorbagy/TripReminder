package com.example.tripreminder;

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
    HistoryListener notesListener;

    public bubbleService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        noteList=new ArrayList<>();
        initNoteListener();

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
        img.setOnTouchListener(new View.OnTouchListener() {
            private  int initialX;
            private  int initialY;
            private  float touchX;
            private  float touchY;
            private int lastAction;

            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (gestureDetector.onTouchEvent(motionEvent)) {
                    Toast.makeText(bubbleService.this, "ddddddddddialog", Toast.LENGTH_SHORT).show();
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
    }

   public void showDialog(){

       final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this, R.style.Theme_AppCompat_Dialog);
       dialogBuilder.setTitle("Notes");
       // Add a checkbox list
       String[] animals = {"Note1", "Note2", "Note3", "Note4", "Note5"};
       boolean[] checkedItems = {true, false, false, true, false};
       dialogBuilder.setMultiChoiceItems(animals, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
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
        bubbleLayout.setVisibility(View.INVISIBLE);
    }

    private class SingleTapConfirm extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapUp(MotionEvent event) {
            return true;
        }
    }

    private void initNoteListener() {
          Log.i("click","init");
          notesListener = new HistoryListener() {
            @Override
            public void delete(Trips trip) {
            }
            @Override
            public void showNote(Trips trip){
                noteList=trip.getNotesList();

            for(int i = 0; i < noteList.size();i++)
            {
            Log.i("click","data : "+noteList.get(i));
            }
            }

        };
    }
}