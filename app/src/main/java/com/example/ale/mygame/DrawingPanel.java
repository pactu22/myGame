package com.example.ale.mygame;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;
import android.widget.Toast;

import com.example.ale.mygame.components.Speed;
import com.example.ale.mygame.model.Dog;
import com.example.ale.mygame.model.Duck;
import com.example.ale.mygame.model.Nest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by ale on 7/24/15.
 */
public class DrawingPanel extends SurfaceView implements SurfaceHolder.Callback {

    private static final String TAG = DrawingPanel.class.getSimpleName();
    private MainThread thread;
    private Duck duck;
    private Nest nest;
    private Dog dog;
    private List<Dog> dogs;


    private final float FACTOR_BOUNCEBACK = 0.50f;
    private float mVx;
    private float mVy;
    private int mXCenter;
    private int mYCenter;
    private final int RADIUS = 50;



    public void setDa(DroidzActivity da) {
        this.ma = da;
    }

    private DroidzActivity ma;

    public DrawingPanel(Context context) {
        super(context);
        // adding the callback (this) to the surface holder to intercept events
        getHolder().addCallback(this);


        WindowManager mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        // make the GamePanel focusable so it can handle events
        setFocusable(true);

        // create the imges
        duck = new Duck(BitmapFactory.decodeResource(getResources(), R.drawable.duckk),
                ((WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth()/2,
                ((WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getHeight()/2);

        nest = new Nest(BitmapFactory.decodeResource(getResources(), R.drawable.rainbow),
                ((WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth()/2, 150);

        dog = new Dog(BitmapFactory.decodeResource(getResources(), R.drawable.lion), 250,350);

        dogs = new ArrayList<Dog>();
        dogs.add(dog);
        /*
        for(int i = 1; i <= 3; ++i){
            dog = new Dog(BitmapFactory.decodeResource(getResources(), R.drawable.lion), 250,350);
            dogs.add(dog);
        }*/

        // create the game loop thread
        thread = new MainThread(getHolder(), this);

    }
    public boolean updateOvalCenter()
    {

        mVx -= ma.getmAx() * ma.getmDeltaT();
        mVy += ma.getmAy() * ma.getmDeltaT();

        mXCenter += (int)(ma.getmDeltaT() * (mVx + 0.5 * ma.getmAx() * ma.getmDeltaT()));
        mYCenter += (int)(ma.getmDeltaT()* (mVy + 0.5 * ma.getmAy() * ma.getmDeltaT()));

        if(mXCenter < RADIUS)
        {
            mXCenter = RADIUS;
            mVx = -mVx * FACTOR_BOUNCEBACK;
        }

        if(mYCenter < RADIUS)  {
            mYCenter = RADIUS;
            mVy = -mVy * FACTOR_BOUNCEBACK;
        }

        if(mXCenter > ma.getmWidthScreen() - RADIUS) {
            mXCenter = ma.getmWidthScreen() - RADIUS;
            mVx = -mVx * FACTOR_BOUNCEBACK;
        }

        if(mYCenter > ma.getmHeightScreen() - 2 * RADIUS) {
            mYCenter = ma.getmHeightScreen() - 2 * RADIUS;
            mVy = -mVy * FACTOR_BOUNCEBACK;
        }

        return true;
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

        // fills the canvas with black
        canvas.drawColor(Color.BLACK);

        //draws the duck image to the coordinates 10,10
        //canvas.drawBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.nido), 10, 10, null);

        nest.draw(canvas);

        duck.draw(canvas);
        for(Dog dog: dogs) {

            dog.draw(canvas);
        }

    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            // delegating event handling to the droid
            duck.handleActionDown((int)event.getX(), (int)event.getY());

            // check if in the lower part of the screen we exit
            if (event.getY() > getHeight() - 50) {
                finishGame();
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

    public void checkCollission(){

        for(Dog dog: dogs) {
            if (duck.getRectangle().intersect(dog.getRectangle())) {
                collisionMsg();
            }
        }
    }
    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
           open();
            return false;
        }
    });

    private void finishGame(){
        thread.setRunning(false);
        ((Activity)getContext()).finish();
    }
    private void collisionMsg(){
        thread.setRunning(false);
        final Message msg = new Message();
        new Thread()
        {
            public void run()
            {
                msg.arg1=1;
                handler.sendMessage(msg);
            }
        }.start();
    }

    public void updateNest() {
        // check collision with right wall if heading right
        if (nest.getSpeed().getxDirection() == Speed.DIRECTION_RIGHT
                && nest.getX() + nest.getBitmap().getWidth() / 2 >= getWidth()) {
            nest.getSpeed().toggleXDirection();
        }
        // check collision with left wall if heading left
        if (nest.getSpeed().getxDirection() == Speed.DIRECTION_LEFT
                && nest.getX() - nest.getBitmap().getWidth() / 2 <= 0) {
            nest.getSpeed().toggleXDirection();
        }
        // check collision with bottom wall if heading down
        if (nest.getSpeed().getyDirection() == Speed.DIRECTION_DOWN
                && nest.getY() + nest.getBitmap().getHeight() / 2 >= getHeight()) {
            nest.getSpeed().toggleYDirection();
        }
        // check collision with top wall if heading up
        if (nest.getSpeed().getyDirection() == Speed.DIRECTION_UP
                && nest.getY() - nest.getBitmap().getHeight() / 2 <= 0) {
            nest.getSpeed().toggleYDirection();
        }
        // Update the lone droid
        nest.update();
    }

    public void updateDogs() {
        for(Dog dog: dogs){

            // check collision with right wall if heading right
            if (dog.getSpeed().getxDirection() == Speed.DIRECTION_RIGHT
                    && dog.getX() + dog.getBitmap().getWidth() / 2 >= getWidth()) {
                dog.getSpeed().toggleXDirection();
            }
            // check collision with left wall if heading left
            if (dog.getSpeed().getxDirection() == Speed.DIRECTION_LEFT
                    && dog.getX() - dog.getBitmap().getWidth() / 2 <= 0) {
                dog.getSpeed().toggleXDirection();
            }
            // check collision with bottom wall if heading down
            if (dog.getSpeed().getyDirection() == Speed.DIRECTION_DOWN
                    && dog.getY() + dog.getBitmap().getHeight() / 2 >= getHeight()) {
                dog.getSpeed().toggleYDirection();
            }
            // check collision with top wall if heading up
            if (dog.getSpeed().getyDirection() == Speed.DIRECTION_UP
                    && dog.getY() - (dog.getBitmap().getHeight()/2)  <= nest.getY() + nest.getBitmap().getHeight()/2) {
                dog.getSpeed().toggleYDirection();
            }
            // Update the lone droid
            dog.update();
        }
    }

    public void updateDuck(){

        duck.setX(mXCenter);
        duck.setY(mYCenter);
    }

    public void open(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setMessage("You have lost! Do you want to play again?" );

        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                Toast.makeText(getContext(), "You clicked yes button", Toast.LENGTH_LONG).show();
            }
        });

        alertDialogBuilder.setNegativeButton("No",new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                finishGame();
            }
        });

        AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();
    }




}
