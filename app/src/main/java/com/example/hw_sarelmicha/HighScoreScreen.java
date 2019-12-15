package com.example.hw_sarelmicha;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

public class HighScoreScreen extends AppCompatActivity implements  HighScoreVariables {

    private ArrayList<Player> allPlayersList;
    private TextView[] allHighScores;
    private HighScore highScore;
    private LinearLayout highScoreContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_high_score_screen);
        highScore = new HighScore(getApplicationContext().getSharedPreferences(SCORE_FILE, MODE_PRIVATE));
        highScoreContainer = (LinearLayout) findViewById(R.id.highscores_conatiner);
    }


    @Override
    protected void onResume() {

        OpenScreen.mediaPlayer.start();
        highScore.readScores(); // redundent need to be check and delete
        allPlayersList = highScore.getAllPlayers();
        allHighScores = new TextView[allPlayersList.size()];
        showTop10();

        super.onResume();
    }

    @Override
    protected void onPause() {
        OpenScreen.mediaPlayer.pause();
        super.onPause();
    }

    public void showTop10(){

        createAllTextViewsScore();
        showAllTextViewsScoreOnScreen();
    }

    public void createAllTextViewsScore(){

        for (int i = 0; i < allPlayersList.size() ; i++) {
            allHighScores[i] = new TextView(this);
            allHighScores[i].setText(String.format("#%-1d :%-10s %-5d", i+ 1,allPlayersList.get(i).getName(),allPlayersList.get(i).getScore()));
//            allHighScores[i].setText(i + 1  + " : " + allPlayersList.get(i).getName() +"       " + allPlayersList.get(i).getScore());
            allHighScores[i].setTextSize(20);
            allHighScores[i].setTextColor(Color.WHITE);
        }
    }

    public void showAllTextViewsScoreOnScreen(){

        LinearLayout.LayoutParams paramsButton = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);

        for (int i = 0; i < allHighScores.length ; i++) {

            allHighScores[i].setId(i);
            allHighScores[i].setTextSize(30);
            allHighScores[i].setTypeface(Typeface.create("monospace", Typeface.BOLD));
            allHighScores[i].setTextColor(Color.WHITE);
            allHighScores[i].setLayoutParams(paramsButton);
            highScoreContainer.addView(allHighScores[i]); // dynamicButtonsLinearLayout is the container of the buttons
        }




    }
}
