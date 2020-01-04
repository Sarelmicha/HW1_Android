package com.example.hw_sarelmicha;
import android.content.SharedPreferences;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import android.util.Log;
import java.util.ArrayList;
import java.util.List;

public class HighScore implements HighScoreVariables {

    private final String ALL_PLAYERS = "Players";
    private SharedPreferences sharedPreferences;
    private ArrayList<PlayerInfo> allPlayersInfos;


    public HighScore(SharedPreferences sharedPreferences) {

        this.allPlayersInfos = new ArrayList<PlayerInfo>();
        this.sharedPreferences = sharedPreferences;
    }

    public void readScores(){

        String jsonString = sharedPreferences.getString(ALL_PLAYERS, null);
        if (jsonString != null) {
            // Option A:
            Gson gson = new Gson();
            allPlayersInfos = gson.fromJson(jsonString, new TypeToken<List<PlayerInfo>>(){}.getType());
        }
    }

    public void addPlayer(PlayerInfo newPlayerInfo){

        int index = findIndexToInsert(newPlayerInfo);

        if(index > -1) { // if we found a player that his score is smaller than newPlayer Score
            allPlayersInfos.add(index, newPlayerInfo);
            if(allPlayersInfos.size() > MAX_SIZE)
                deleteOneScore(allPlayersInfos.size() - 1);
        }
         else if(index == -1 && allPlayersInfos.size() < MAX_SIZE)
            allPlayersInfos.add(newPlayerInfo);
    }

    public int findIndexToInsert(PlayerInfo newPlayerInfo){

        for (int i = 0; i < allPlayersInfos.size() ; i++) {
            if(newPlayerInfo.compareTo(allPlayersInfos.get(i)) > 0){
                return i;
            }
        }
        return -1;
    }

    public void showAllPlayers(){

        for (int i = 0; i < allPlayersInfos.size(); i++) {
            Log.d("check", "showAllPlayers: " + allPlayersInfos.get(i).toString());
        }
    }

    public ArrayList<PlayerInfo> getAllPlayers(){
        return allPlayersInfos;
    }

    public void writeScore(){

        SharedPreferences.Editor editor = sharedPreferences.edit();
        String jsonStringAllPlayers;

        Gson gson = new Gson();
        jsonStringAllPlayers = gson.toJson(allPlayersInfos);
        editor.putString(ALL_PLAYERS, jsonStringAllPlayers);
        editor.apply();
    }

    public void deleteOneScore(int index){
        allPlayersInfos.remove(index);
    }


    public void deleteAllScores(){
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.clear();
        editor.apply();
    }
}


