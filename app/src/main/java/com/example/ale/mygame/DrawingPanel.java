package com.example.ale.mygame;

import android.app.Activity;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.ale.mygame.model.Duck;

/**
 * Created by ale on 7/24/15.
 */
public class DrawingPanel extends SurfaceView implements SurfaceHolder.Callback {

    private static final String TAG = DrawingPanel.class.getSimpleName();
    private MainThread thread;
    private Duck duck;



    public DrawingPanel(Context context) {
        super(context);
        // adding the callback (this) to the surface holder to intercept events
        getHolder().addCallback(this);

        // create droid and load bitmap
        duck = new Duck(BitmapFactory.decodeResource(getResources(), R.drawable.duck), 50, 50);
        // create the game loop thread
        thread = new MainThread(getHolder(), this);

    // make the GamePanel focusable so it can handle events
        setFocusable(true);

    }
    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        // at this point the surface is created and
        // we can safely start the game loop

        thread.setRunning(true);
        thread.start();

    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        boolean retry = true;
        while (retry) {
            try {
                thread.join();
                retry = false;
            } catch (InterruptedException e) {
                // try again shutting down the thread
            }
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        //draws the duck image to the coordinates 10,10
        //canvas.drawBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.duck), 10, 10, null);
        // fills the canvas with black
        canvas.drawColor(Color.BLACK);
        duck.draw(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            // delegating event handling to the droid
            duck.handleActionDown((int)event.getX(), (int)event.getY());

            // check if in the lower part of the screen we exit
            if (event.getY() > getHeight() - 50) {
                thread.setRunning(false);
                ((Activity)getContext()).finish();
            } else {
                Log.d(TAG, "Coords: x=" + event.getX() + ",y=" + event.getY());
            }
        } if (event.getAction() == MotionEvent.ACTION_MOVE) {
            // the gestures
            if (duck.isTouched()) {
                // the droid was picked up and is being dragged
                duck.setX((int)event.getX());
                duck.setY((int)event.getY());
            }
        } if (event.getAction() == MotionEvent.ACTION_UP) {
            // touch was released
            if (duck.isTouched()) {
                duck.setTouched(false);
            }
        }
        return true;
    }

}
