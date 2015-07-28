package com.example.ale.mygame.model;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

import com.example.ale.mygame.components.Speed;

/**
 * Created by ale on 7/24/15.
 */
public class Nest {
    private Bitmap bitmap; // the actual bitmap
    private int x;   // the X coordinate
    private int y;   // the Y coordinate
    private boolean touched; // if droid is touched/picked up
    private Rect rectangle = new Rect();


    private Speed speed;

    public Nest(Bitmap bitmap, int x, int y) {
        this.bitmap = bitmap;
        this.x = x;
        this.y = y;
        speed = new Speed(5,5);
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
        canvas.drawBitmap(bitmap, x - (bitmap.getWidth() / 2), y - (bitmap.getHeight() / 2), null);
    }
    public Rect getRectangle(){
        return rectangle;
    }



    public Speed getSpeed() {
        return speed;
    }

    public void update() {
        if (!touched) {
            x += (speed.getXv() * speed.getxDirection());
        }
    }
}