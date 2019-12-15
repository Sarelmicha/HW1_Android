package com.example.hw_sarelmicha;

import java.io.Serializable;

public class Player implements Serializable {

    private String name;
    private int score;
    private float lat;
    private float lon;

    public Player(String name, int score, float lat, float lon) {
        this.name = name;
        this.score = score;
        this.lat = lat;
        this.lon = lon;
    }

    public int compareTo(Player player){

        if(this.score > player.score)
            return 1;
        else
            return 0;

    }

    public String getName() {
        return name;
    }

    public int getScore() {
        return score;
    }

    public float getLat() {
        return lat;
    }

    public float getLon() {
        return lon;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setLat(float lat) {
        this.lat = lat;
    }

    public void setLon(float lon) {
        this.lon = lon;
    }

    @Override
    public String toString() {
        return "Player{" +
                "name='" + name + '\'' +
                ", score=" + score +
                ", lat='" + lat + '\'' +
                ", lon='" + lon + '\'' +
                '}';
    }
}


