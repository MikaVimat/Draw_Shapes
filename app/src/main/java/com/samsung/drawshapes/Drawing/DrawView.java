package com.samsung.drawshapes.Drawing;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

public class DrawView extends SurfaceView implements SurfaceHolder.Callback {

    public   ThreadForDrawing _myThread;
    public static boolean isFignerUp = false;
    public static boolean isMouseDown = false;
    public static Context thisContext;
    public static DrawView thisView;

    public DrawView(Context context) {
        super(context);
        thisContext = context;
        thisView = this;
        getHolder().addCallback(this);
    }

    public DrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
        getHolder().addCallback(this);
    }

    public DrawView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        getHolder().addCallback(this);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public DrawView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        getHolder().addCallback(this);
    }

    @Override
    public void surfaceCreated(@NonNull SurfaceHolder surfaceHolder) {
        System.out.println("SRF_CREATED");
        _myThread =  new ThreadForDrawing(getContext(), getHolder() ,this);
        _myThread.start();
    }




    @Override
    public void surfaceChanged(@NonNull SurfaceHolder surfaceHolder, int i, int i1, int i2) {

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if(_myThread != null) {
            _myThread.pressedCoordinates((int) event.getX(), (int) event.getY());
            Log.d("motion_touched", event.getX() + "" + event.getY());
            if (event.getAction() == KeyEvent.ACTION_UP) {
                Log.d("motion_UP", event.getX() + "" + event.getY());
                isFignerUp = true;
            }
            if (event.getAction() == KeyEvent.ACTION_DOWN) {
                Log.d("motion_DOWN", event.getX() + "" + event.getY());
                _myThread.actionDownCoordinates((int) event.getX(), (int) event.getY());
                isFignerUp = false;
                isMouseDown = true;
            }
        }
        return true;
    }

    @Override
    public boolean onKeyUp(int keyCode, KeyEvent event) {
        Log.d("event_up","KeyUp");

        return false;
    }

    @Override
    public boolean onHoverEvent(MotionEvent event) {
        Log.d("even_hover","hover"+event.getX());
        return true;
    }


    @Override
    public void surfaceDestroyed(@NonNull SurfaceHolder surfaceHolder) {
        System.out.println("SRF_DESTROYED");

        if(_myThread != null){
            ThreadForDrawing.askToStopTheThread();
        }
        boolean tryToStop = true;
        while (tryToStop) {
            try {
                _myThread.join();
                System.out.println("Destroyed_Requesting trying to  STOP");
                tryToStop = false;
            } catch (InterruptedException e) {
                System.out.println("Destroyed_CANNOT STOP THE THREAD");
            }
        }

    }
}
