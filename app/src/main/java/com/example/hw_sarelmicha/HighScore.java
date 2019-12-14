package com.example.hw_sarelmicha;

import android.app.Activity;
import android.content.SharedPreferences;

import com.example.hw_sarelmicha.Player;
import com.google.gson.Gson;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class HighScore {

    private final String ALL_PLAYERS = "Players";
    private SharedPreferences sharedPreferences;
    private ArrayList allPlayers;


    public HighScore(SharedPreferences sharedPreferences) {

        allPlayers = new ArrayList();
        this.sharedPreferences = sharedPreferences;
    }

    public void readScores(){

        String jsonString = sharedPreferences.getString(ALL_PLAYERS, null);
        Log.d("check", "json = : " + jsonString);
        if (jsonString != null) {
            // Option A:
            Gson gson = new Gson();
            allPlayers = gson.fromJson(jsonString, ArrayList.class);
        }
    }

    public void addPlayer(Player newPlayer){
        if(allPlayers.size() < 10){
            //allPlayers[size] = newPlayer;
            allPlayers.add(newPlayer);
        } else {
            //Sort and insert.
            int index = findIndexToInsert(newPlayer);
            if(index > -1){
                allPlayers.add(index,newPlayer);
                allPlayers.remove(allPlayers.size() - 1);
            }
        }
    }

    public int findIndexToInsert(Player newPlayer){

        for (int i = 0; i < allPlayers.size() ; i++) {
            if(newPlayer.compareTo((Player) allPlayers.get(i)) > 0){
                return i;
            }
        }
        return -1;
    }

    public void showAllPlayers(){

        for (int i = 0; i < allPlayers.size(); i++) {
            Log.d("check", "showAllPlayers: " + allPlayers.get(i).toString());
        }
    }

    public void writeScore(){

        SharedPreferences.Editor editor = sharedPreferences.edit();
        String jsonStringAllPlayers;

        Gson gson = new Gson();
        jsonStringAllPlayers = gson.toJson(allPlayers);
        editor.putString(ALL_PLAYERS, jsonStringAllPlayers);
        editor.apply();

    }
}


