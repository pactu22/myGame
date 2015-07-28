package com.example.ale.mygame.model;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.example.ale.mygame.components.Speed;

/**
 * Created by ale on 7/24/15.
 */
public class Duck {
    private Bitmap bitmap; // the actual bitmap
    private int x;   // the X coordinate
    private int y;   // the Y coordinate
    private boolean touched; // if droid is touched/picked up
    private Rect rectangle = new Rect();


    private Speed speed;

    public Duck(Bitmap bitmap, int x, int y) {
        this.bitmap = bitmap;
        this.x = x;
        this.y = y;
        speed = new Speed();

        rectangle = new Rect(getLeftX(), getTopY(),getRightX(), getBottomY());

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
        rectangle = new Rect(getLeftX(), getTopY(),getRightX(), getBottomY());
        canvas.drawBitmap(bitmap, x - (bitmap.getWidth() / 2), y - (bitmap.getHeight() / 2), null);

    }

    public void handleActionDown(int eventX, int eventY) {
        if (eventX >= (x - bitmap.getWidth() / 2) && (eventX <= (x + bitmap.getWidth()/2))) {
            if (eventY >= (y - bitmap.getHeight() / 2) && (y <= (y + bitmap.getHeight() / 2))) {
                // droid touched
                setTouched(true);
            } else {
                setTouched(false);
            }
        } else {
            setTouched(false);
        }

    }

    public Speed getSpeed() {
        return speed;
    }
    public void update() {
        if (!touched) {
            x += (speed.getXv() * speed.getxDirection());
            y += (speed.getYv() * speed.getyDirection());
        }
    }

    public Rect getRectangle(){
        return rectangle;
    }
}
