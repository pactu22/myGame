package com.example.ale.mygame;

import android.app.Activity;
import android.content.Context;
import android.content.pm.ActivityInfo;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
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