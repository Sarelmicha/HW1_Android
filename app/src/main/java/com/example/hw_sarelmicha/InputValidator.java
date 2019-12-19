package com.example.hw_sarelmicha;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;

public class InputValidator implements TextWatcher {

    private final int MAX_LENGTH = 6;
    @Override
    public void afterTextChanged(Editable s) {
        try {
            String playerName = s.toString();
            lengthCheck(s,playerName);
            englishCheck(s, playerName);

        } catch (Exception e){
            Log.d("ERROR", "something went wrong...");
        }
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count,
                                  int after) {

    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {

    }

    public void lengthCheck(Editable s,String playerName){

        if (playerName.length() > MAX_LENGTH) {
            playerName = playerName.substring(0, playerName.length() - 1);
            s.replace(0,s.length(), playerName);
        }
    }

    public void englishCheck(Editable s,String playerName){

        for(int i = 0; i < playerName.length(); i++){
            if(playerName.charAt(i) < 65 || (playerName.charAt(i) > 90 && playerName.charAt(i) < 97) || playerName.charAt(i) > 122){
                playerName = playerName.substring(0, playerName.length() - 1);
                s.replace(0,s.length(), playerName);
            }
        }

    }

    public static boolean isNameEmpty(String playerName){

        if(playerName.length() == 0){
            return true;
        }
        return false;
    }
}