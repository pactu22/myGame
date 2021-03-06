package com.example.ale.mygame;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.WindowManager;

import com.example.ale.mygame.components.Speed;
import com.example.ale.mygame.model.Dog;
import com.example.ale.mygame.model.Duck;
import com.example.ale.mygame.model.Level;
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

    public void setLevel(Level level) {
        this.level = level;
        Log.d("nIVEL: " , String.valueOf(level.getNumberObstacles()));
    }

    private Level level;

    private final float FACTOR_BOUNCEBACK = 0.40f;
    private float mVx;
    private float mVy;
    private int mXCenter;
    private int mYCenter;
    private final int RADIUS = 50;



    public void setDa(DroidzActivity da) {
        this.ma = da;
    }

    private DroidzActivity ma;

    public DrawingPanel(Context context, Level level) {
        super(context);
        // adding the callback (this) to the surface holder to intercept events
        getHolder().addCallback(this);

        Log.d("nIVEL: " , String.valueOf(level.getNumberObstacles()));
        this.level = level;
        WindowManager mWindowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

        // make the GamePanel focusable so it can handle events
        setFocusable(true);

        // create the imges
        int heightScreen = ((WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getHeight();
        int widthScreen = ((WindowManager)getContext().getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay().getWidth();

        duck = new Duck(BitmapFactory.decodeResource(getResources(), R.drawable.penguin1),
                widthScreen/2,
                heightScreen-60);
        Log.d("POSISCION Y: " , String.valueOf(duck.getY()));
        //duck.setY(heightScreen-duck.getBitmap().getHeight()-100);

        nest = new Nest(BitmapFactory.decodeResource(getResources(), R.drawable.rainbow),
                widthScreen/2, 150);

        mXCenter = duck.getX()/2;
        mYCenter = duck.getY()-(duck.getBitmap().getHeight()/2);

       // dog = new Dog(BitmapFactory.decodeResource(getResources(), R.drawable.bomb), widthScreen/2,350);

        dogs = new ArrayList<Dog>();
        //dogs.add(dog);
      //  dog = new Dog(BitmapFactory.decodeResource(getResources(), R.drawable.duck1), 500,heightScreen-60);
        //dogs.add(dog);
        for(int i = 1; i <= level.getNumberObstacles(); ++i){
            dog = new Dog(BitmapFactory.decodeResource(getResources(), R.drawable.bomb), 250,350);
            dogs.add(dog);
        }

        // create the game loop thread
        thread = new MainThread(getHolder(), this);

    }
    public boolean updateOvalCenter()
    {

        mVx -= ma.getmAx() * ma.getmDeltaT();
        mVy += ma.getmAy() * ma.getmDeltaT();
        Log.d("MXCENTER: " , String.valueOf(mXCenter));
        Log.d("MYCENTR: " , String.valueOf(mYCenter));
        mXCenter += (int)(ma.getmDeltaT() * (mVx + 0.5 * ma.getmAx() * ma.getmDeltaT()));
        mYCenter += (int)(ma.getmDeltaT()* (mVy + 0.5 * ma.getmAy() * ma.getmDeltaT()));

       // mXCenter = duck.getX()/2;
        //mYCenter = duck.getY()/2;
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
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            // delegating event handling to the droid

            // check if in the lower part of the screen we exit
            if (event.getY() > getHeight() - 50) {
                finishGame();
            }
            return true;
        }
        return false;
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

        //canvas.drawBitmap(BitmapFactory.decodeResource(getResources(), R.drawable.back), 0, 0, null);

        //draws the duck image to the coordinates 10,10


        nest.draw(canvas);
        if(duck.getRectangle() != null){
            duck.setRectangle(new Rect(mXCenter - RADIUS, mYCenter - RADIUS, mXCenter + RADIUS, mYCenter + RADIUS));
        }
        duck.draw(canvas);

        for(Dog dog: dogs) {

            dog.draw(canvas);
        }

    }

    public void checkCollision(){


        for(Dog dog: dogs) {
            if (duck.getRectangle().intersect(nest.getRectangle())) {
                collisionMsg(false);
            }
            if (duck.getRectangle().intersect(dog.getRectangle())) {
                collisionMsg(true);
            }

        }
    }

    private void finishGame(){
        thread.setRunning(false);
        ((Activity)getContext()).finish();
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

            // check collision with left wall if heading left
            if (dog.getSpeed().getxDirection() == Speed.DIRECTION_LEFT
                    && dog.getX() - dog.getBitmap().getWidth() / 2 <= 0) {
                //Log.d(TAG, "LEFT");
                dog.getSpeed().toggleXDirection();
            }
            // check collision with right wall if heading right
            else if (dog.getSpeed().getxDirection() == Speed.DIRECTION_RIGHT
                    && dog.getX() + dog.getBitmap().getWidth() / 2 >= getWidth()) {
                //Log.d(TAG, "RIGHT");
                dog.getSpeed().toggleXDirection();
            }
            // check collision with bottom wall if heading down
            else if (dog.getSpeed().getyDirection() == Speed.DIRECTION_DOWN
                    && dog.getY() + dog.getBitmap().getHeight() / 2 >= getHeight()) {
                //Log.d(TAG, "DOWN Y: " + dog.getY()+ "  X: " + dog.getX());
                dog.getSpeed().toggleYDirection();
            }
            // check collision with top wall if heading up
            else if (dog.getSpeed().getyDirection() == Speed.DIRECTION_UP
                    && dog.getY() - (dog.getBitmap().getHeight()/2)  <= nest.getY() + nest.getBitmap().getHeight()/2) {
                //Log.d(TAG, "UP Y: " + dog.getY()+ "  X: " + dog.getX());
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


    private void collisionMsg(final boolean lost){
        thread.setRunning(false);
        final Message msg = new Message();

        new Thread()
        {
            public void run()
            {
                if(lost){
                    msg.arg1=1;

                }
                else{
                    msg.arg1=2;
                }
                handler.sendMessage(msg);

            }
        }.start();
    }
    public void open(boolean won){
        String msg, title = null;
        if(won) {
            title = "Congrats!!";
            msg = "Do you want to continue to another level?";
        }
        else{
            title = "You have lost! ";
            msg = "Do you want to play again?" ;
        }
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getContext());
        alertDialogBuilder.setMessage(msg);
        alertDialogBuilder.setTitle(title);
        alertDialogBuilder.setIcon(R.drawable.sad);


        alertDialogBuilder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface arg0, int arg1) {
                finishGame();
                Intent intentGame = new Intent(getContext(), DroidzActivity.class);
                intentGame.putExtra("level", level.getNumberObstacles());
                getContext().startActivity(intentGame);
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

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            if(msg.arg1 == 1){
                //game lost
                open(false);
            }
            else{
                //game won
                open(true);
            }

            return false;
        }
    });

    public MainThread getThread() {
        return thread;
    }


}
