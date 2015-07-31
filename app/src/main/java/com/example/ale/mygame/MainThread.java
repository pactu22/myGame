package com.example.ale.mygame;

import android.annotation.SuppressLint;
import android.graphics.Canvas;
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

    @SuppressLint("WrongCall")
    @Override
    public void run() {
        Canvas canvas;
        while (running) {

            canvas = null;
            try {
                canvas = this.surfaceHolder.lockCanvas();



                synchronized (surfaceHolder) {
                    // update game state
                    gamePanel.updateNest(); //move "randomly"
                    gamePanel.updateDogs();
                    gamePanel.updateDuck();

                    gamePanel.onDraw(canvas);
                    gamePanel.checkCollision();
                    gamePanel.updateOvalCenter();
                }
            } finally {
                // in case of an exception the surface is not left in
                // an inconsistent state

                if (canvas != null) {
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
        }
    }

}
