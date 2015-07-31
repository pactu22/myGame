package com.example.ale.mygame.model;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.example.ale.mygame.components.Speed;

/**
 * Created by ale on 7/27/15.
 */
public class Dog {

    private static final String TAG = Dog.class.getSimpleName();

    private Bitmap bitmap; // the actual bitmap
    private int x;   // the X coordinate
    private int y;   // the Y coordinate
    private boolean touched; // if droid is touched/picked up
    private Rect rectangle = new Rect();


    private Speed speed;

    public Dog(Bitmap bitmap, int x, int y) {
        this.bitmap = bitmap;
        this.x = x;
        this.y = y;
        rectangle = new Rect(getLeftX(), getTopY(),getRightX(), getBottomY());

        int rangeX = (8  - (- 8) + 1);


        int randomX = (int)(Math.random() * rangeX) -8;
        int randomY = (int)(Math.random() * 8);
        //Log.d(TAG, "Dog: Random x: " + randomX + "Random y: " + randomY);
        speed = new Speed(randomX,randomY);
        //if (randomX < 0) speed.setxDirection(-1);
    }

    private int getTopY(){
        return  y - (bitmap.getHeight() / 2);
    }
    private int getBottomY(){
        return  y + (bitmap.getHeight() /2);
    }
    private int getLeftX(){
        return  x - (bitmap.getWidth() /2);
    }
    private int getRightX(){
        return  x + (bitmap.getWidth() /2);
    }

    public Bitmap getBitmap() {
        return bitmap;
    }
    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
    public int getX() {
        return x;
    }
    public void setX(int x) {
        this.x = x;
    }
    public int getY() {
        return y;
    }
    public void setY(int y) {
        this.y = y;
    }
    public boolean isTouched() {
        return touched;
    }

    public void setTouched(boolean touched) {
        this.touched = touched;
    }

    public void draw(Canvas canvas) {
        canvas.drawBitmap(bitmap, x - (bitmap.getWidth() / 2), y - (bitmap.getHeight() / 2), null);
        rectangle = new Rect(getLeftX(), getTopY(),getRightX(), getBottomY());
    }


    public Speed getSpeed() {
        return speed;
    }
    public void update() {
        if (!touched) {
            x += (speed.getXv() * speed.getxDirection());
          //  Log.d("SPEED XV: " , String.valueOf(speed.getXv()) );
            //Log.d("DIRECTI XV: " , String.valueOf(speed.getxDirection()) );
            y += (speed.getYv() * speed.getyDirection());
            rectangle = new Rect(getLeftX(), getTopY(),getRightX(), getBottomY());
        }
    }
    public Rect getRectangle(){
        return rectangle;
    }

    @Override
    public int hashCode() {
        return x*y;
    }

    @Override
    public boolean equals(Object o) {
        Dog d = (Dog) o;
        return (this.bitmap.equals(d.getBitmap()) && this.x == d.getX() && this.y == d.getY() );
    }
}
