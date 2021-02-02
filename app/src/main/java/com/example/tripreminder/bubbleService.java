package com.example.tripreminder;

import android.app.Service;
import android.content.Intent;
import android.graphics.PixelFormat;
import android.os.IBinder;
import android.util.Log;
import android.view.GestureDetector;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewManager;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.txusballesteros.bubbles.BubbleLayout;

public class bubbleService extends Service {
    private BubbleLayout bubbleLayout;
    private WindowManager windowManager;
    private GestureDetector gestureDetector;
    public bubbleService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
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
                    Log.i("click","clickkkkkked");
                    Intent dialogIntent = new Intent(getApplicationContext(), ReadNote.class);
                    dialogIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(dialogIntent);
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

    @Override
    public void onDestroy() {
        super.onDestroy();
        //windowManager.removeView(bubbleLayout);
        //((ViewManager)bubbleLayout.getParent()).removeView(bubbleLayout);
        bubbleLayout.setVisibility(View.GONE);
    }

    private class SingleTapConfirm extends GestureDetector.SimpleOnGestureListener {

        @Override
        public boolean onSingleTapUp(MotionEvent event) {
            return true;
        }
    }


}