package com.example.ale.mygame.components;

/**
 * Created by ale on 7/24/15.
 */
public class Speed {
    public static final int DIRECTION_RIGHT	= 1;
    public static final int DIRECTION_LEFT	= -1;
    public static final int DIRECTION_UP	= -1;
    public static final int DIRECTION_DOWN	= 1;

    private float xv = 1;	// velocity value on the X axis
    private float yv = 1;	// velocity value on the Y axis

    private int xDirection =-1;
    private int yDirection;

    public Speed() {
        //speed of the bitmap
        this.xv = 1;
        this.yv = 1;
    }

    public Speed(float xv, float yv) {

        this.yv = yv;
      //  Log.d("DOG::::" ,"VEL: "+  xv);
        if(xv < 0 ){
         //   Log.d("XV: ", "negativo");
            this.xv = xv*-1;
            xDirection = DIRECTION_LEFT;
        }
        else{
          //  Log.d("XV: ", "pos");
            this.xv = xv;
            xDirection = DIRECTION_RIGHT;
        }
        if(yv < 0 ){
         //   Log.d("yV: ", "y negativo");
            this.yv = yv*-1;
            yDirection = DIRECTION_UP;
        }
        else{
         //   Log.d("yV: ", " y pos");
            this.yv = yv;
            yDirection = DIRECTION_DOWN;
        }


    }

    public float getXv() {
        return xv;
    }
    public void setXv(float xv) {
        this.xv = xv;
    }
    public float getYv() {
        return yv;
    }
    public void setYv(float yv) {
        this.yv = yv;
    }

    public int getxDirection() {

        return xDirection;
    }
    public void setxDirection(int xDirection) {
        this.xDirection = xDirection;
    }
    public int getyDirection() {
        return yDirection;
    }
    public void setyDirection(int yDirection) {
        this.yDirection = yDirection;
    }

    // changes the direction on the X axis
    public void toggleXDirection() {
        xDirection = xDirection * -1;
    }

    // changes the direction on the Y axis
    public void toggleYDirection() {
        yDirection = yDirection * -1;
    }
}
