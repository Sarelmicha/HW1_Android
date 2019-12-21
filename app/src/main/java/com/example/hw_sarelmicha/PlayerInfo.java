package com.example.hw_sarelmicha;

import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.SupportMapFragment;

import java.io.Serializable;

public class PlayerInfo implements Serializable {

    private String name;
    private int score;
    private double lat;
    private double lon;

    public PlayerInfo(String name, int score, float lat, float lon) {

        this.name = name;
        this.score = score;
        this.lat = lat;
        this.lon = lon;
    }

    public int compareTo(PlayerInfo playerInfo){

        if(this.score > playerInfo.score)
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

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void setLon(double lon) {
        this.lon = lon;
    }


    @Override
    public String toString() {
        return "PlayerInfo{" +
                "name='" + name + '\'' +
                ", score=" + score +
                ", lat='" + lat + '\'' +
                ", lon='" + lon + '\'' +
                '}';
    }
}


