package com.example.ale.mygame;

import android.app.Activity;
import android.app.AlertDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;

public class DroidzActivity extends Activity {
    /** Called when the activity is first created. */

    private static final String TAG = DroidzActivity.class.getSimpleName();

    @Override
    public void onCreate(Bundle savedInstanceState) {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        super.onCreate(savedInstanceState);
        // requesting to turn the title OFF
        // making it full screen
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
        // set our MainGamePanel as the View
        setContentView(new DrawingPanel(this));
        Log.d(TAG, "View added");

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
        showWindow();
    }
    private void showWindow(){
        // 1. Instantiate an AlertDialog.Builder with its constructor
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

// 2. Chain together various setter methods to set the dialog characteristics
        builder.setMessage("RUEBA")
                .setTitle("XX");

// 3. Get the AlertDialog from create()
        AlertDialog dialog = builder.create();
    }
}