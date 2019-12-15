package com.example.hw_sarelmicha;

import android.app.Activity;
import android.content.SharedPreferences;

import com.example.hw_sarelmicha.Player;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import android.support.v7.app.AppCompatActivity;
import android.util.Log;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

public class HighScore implements HighScoreVariables {

    private final String ALL_PLAYERS = "Players";
    private SharedPreferences sharedPreferences;
    private ArrayList<Player> allPlayers;


    public HighScore(SharedPreferences sharedPreferences) {

        allPlayers = new ArrayList<Player>();
        this.sharedPreferences = sharedPreferences;
    }

    public void readScores(){

        String jsonString = sharedPreferences.getString(ALL_PLAYERS, null);
        if (jsonString != null) {
            // Option A:
            Gson gson = new Gson();
            allPlayers = gson.fromJson(jsonString, new TypeToken<List<Player>>(){}.getType());
        }
    }

    public void addPlayer(Player newPlayer){

        int index = findIndexToInsert(newPlayer);

        if(index > -1) {
            allPlayers.add(index, newPlayer);
            if(allPlayers.size() > MAX_SIZE)
                allPlayers.remove(allPlayers.size() - 1);
        }
         else if(index == -1 && allPlayers.size() < MAX_SIZE)
             allPlayers.add(newPlayer);
    }

    public int findIndexToInsert(Player newPlayer){

        for (int i = 0; i < allPlayers.size() ; i++) {
            if(newPlayer.compareTo(allPlayers.get(i)) > 0){
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

    public ArrayList<Player> getAllPlayers(){
        return allPlayers;
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


