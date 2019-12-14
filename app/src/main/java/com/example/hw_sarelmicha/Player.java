package com.example.hw_sarelmicha;

public class Player{

    private String name;
    private int score;
    private String lat;
    private String lon;

    public Player(String name, int score, String lat, String lon) {
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


