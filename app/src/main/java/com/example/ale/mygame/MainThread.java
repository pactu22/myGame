package com.example.ale.mygame;

import android.graphics.Canvas;
import android.util.Log;
import android.view.SurfaceHolder;

/**
 * Created by ale on 7/24/15.
 */
public class MainThread extends Thread {

    private static final String TAG = MainThread.class.getSimpleName();

    private SurfaceHolder surfaceHolder;
    private DrawingPanel gamePanel;
    // flag to hold game state
    private boolean running;

    public MainThread(SurfaceHolder surfaceHolder, DrawingPanel gamePanel) {
        super();
        this.surfaceHolder = surfaceHolder;
        this.gamePanel = gamePanel;
    }


    public void setRunning(boolean running) {
        this.running = running;
    }

    @Override
    public void run() {
        Canvas canvas;
        //long tickCount = 0L;
        Log.d(TAG, "Starting game loop");

        while (running) {
            canvas = null;
            try {
                canvas = this.surfaceHolder.lockCanvas();
                synchronized (surfaceHolder) {
                    // update game state
                    this.gamePanel.updateNest(); //move "randomly"
                    this.gamePanel.updateDogs();
                    gamePanel.onDraw(canvas);
                    gamePanel.checkCollission();
                }
            } finally {
                // in case of an exception the surface is not left in
                // an inconsistent state

                if (canvas != null) {
                    surfaceHolder.unlockCanvasAndPost(canvas);

                }
                // tickCount++;
                // update game state
                // render state to the screen
            }
            //Log.d(TAG, "Game loop executed " + tickCount + " times");

        }
    }
}
