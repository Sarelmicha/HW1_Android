package com.example.hw_sarelmicha;

import android.app.Activity;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;;
import android.view.View;
import android.widget.Button;


public class OpenScreen extends Activity implements HighScoreVariables {

    private Button newGameBtn;
    private Button exitBtn;
    private Button highScoreBtn;
    private HighScore highScore;
    public static MediaPlayer mediaPlayer;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_screen);
        setIds();
        startSoundtrack();
        addListenersButtons();
        highScore = new HighScore(getApplicationContext().getSharedPreferences(SCORE_FILE, MODE_PRIVATE));

    }

    @Override
    protected void onPause() {
        mediaPlayer.pause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mediaPlayer.start();
        highScore.readScores();
        //highScore.showAllPlayers();
        super.onResume();

    }
    private void setIds(){

        //Initialize Buttons
        newGameBtn = (Button)findViewById(R.id.newGameBtn);
        exitBtn = (Button)findViewById(R.id.exitBtn);
        highScoreBtn = (Button)findViewById(R.id.highScoreBtn);
    }

    private void startSoundtrack(){
        mediaPlayer = MediaPlayer.create(this, R.raw.gamemusic);
        mediaPlayer.setLooping(true);
        mediaPlayer.start();
    }

    private void addListenersButtons(){

        newGameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newGame();
            }
        });

        highScoreBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showHighScores();
            }
        });

        exitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                exitGame();
            }
        });

    }


    private void newGame() {
        Intent intent = new Intent(this, Difficulty.class);
        startActivity(intent);
    }

    private void showHighScores(){
        Intent intent = new Intent(this, HighScoreScreen.class);
        startActivity(intent);
    }

    public static void exitGame() {
        System.exit(0);
    }
}