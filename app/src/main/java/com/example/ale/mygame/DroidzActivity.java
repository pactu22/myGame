package com.example.ale.mygame;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.KeyEvent;
import android.view.Window;
import android.view.WindowManager;

public class DroidzActivity extends Activity implements SensorEventListener {
    /** Called when the activity is first created. */

    private static final String TAG = DroidzActivity.class.getSimpleName();

    /** Manager used for detecting changes to the phone's direction and gravity */
   private DrawingPanel dp;

    // sensor-related
    private SensorManager mSensorManager;
    private Sensor mAccelerometer;

    // screen size
    private int mWidthScreen;
    private int mHeightScreen;

    // motion parameters
    private final float FACTOR_FRICTION = 0.5f; // imaginary friction on the screen
    private final float GRAVITY = 9.8f; // acceleration of gravity
    private float mAx; // acceleration along x axis

    private float mAy; // acceleration along y axis
    private final float mDeltaT = 0.5f; // imaginary time interval between each acceleration updates

    @Override
    public void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "On create...");
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);

        // set the screen always portait
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

        // initializing sensors
        mSensorManager = (SensorManager) getSystemService(SENSOR_SERVICE);
        mAccelerometer = mSensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);

        // obtain screen width and height
        Display display = ((WindowManager) this.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
        mWidthScreen = display.getWidth();
        mHeightScreen = display.getHeight();

        // requesting to turn the title OFF
        // making it full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        dp = new DrawingPanel(this);
        dp.setDa(this);
        // set our MainGamePanel as the View
        setContentView(dp);

        Log.d(TAG, "View added");

    }

    @Override
    protected void onPause() {
        super.onPause();
        // stop senser sensing
        mSensorManager.unregisterListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        // start sensor sensing
        mSensorManager.registerListener(this, mAccelerometer, SensorManager.SENSOR_DELAY_NORMAL);
    }

    @Override
    protected void onDestroy() {
        Log.d(TAG, "Destroying...");
        super.onDestroy();
    }

    @Override
    protected void onStop() {
        Log.d(TAG, "Stopping...");
        super.onStop();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        dp = new DrawingPanel(this);
        dp.setDa(this);
        // set our MainGamePanel as the View
        setContentView(dp);

    }
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
           Log.d(TAG, "BACK BUTTON");
                modalWindow();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void modalWindow() {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure you want to finish the game");
        alertDialogBuilder.setTitle("Finish");
        alertDialogBuilder.setIcon(R.drawable.sad);


        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {

                Intent intentGame = new Intent(getBaseContext(), MainActivity.class);
                startActivity(intentGame);
            }
        });

        alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
              //TODO
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        // obtain the three accelerations from sensors
        mAx = event.values[0];
        mAy = event.values[1];

        float mAz = event.values[2];

        // taking into account the frictions
        mAx = Math.signum(mAx) * Math.abs(mAx) * (1 - FACTOR_FRICTION * Math.abs(mAz) / GRAVITY);
        mAy = Math.signum(mAy) * Math.abs(mAy) * (1 - FACTOR_FRICTION * Math.abs(mAz) / GRAVITY);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
    public float getmAy() {
        return mAy;
    }

    public float getmAx() {
        return mAx;
    }

    public float getmDeltaT() {
        return mDeltaT;
    }

    public int getmWidthScreen() {
        return mWidthScreen;
    }

    public int getmHeightScreen() {
        return mHeightScreen;
    }
}