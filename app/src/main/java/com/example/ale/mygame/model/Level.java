package com.example.ale.mygame.model;

/**
 * Created by ale on 7/31/15.
 */
public class Level {

    private String name;
    private int numberObstacles;

    public Level(String name, int numberObstacles) {
        this.name = name;
        this.numberObstacles = numberObstacles;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getNumberObstacles() {
        return numberObstacles;
    }

    public void setNumberObstacles(int numberObstacles) {
        this.numberObstacles = numberObstacles;
    }
}
