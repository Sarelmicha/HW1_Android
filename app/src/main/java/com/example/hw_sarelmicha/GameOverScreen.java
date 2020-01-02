package com.example.hw_sarelmicha;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class GameOverScreen extends Activity implements HighScoreVariables {

    private Button mainMenu;
    private Button restart;
    private Button newGame;
    private TextView score;
    private int difficulty;
    private boolean regularMode;
    private PlayerInfo playerInfo;
    private HighScore highScore;
    private boolean musicOn;
    private boolean vibrationOn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over_screen);
        setIds();
        handleData();
        score.setText(score.getText().toString() + playerInfo.getScore());
        handleHighScore();
        addListenersButtons();

    }

    private void handleData(){

        Bundle data = getIntent().getExtras();
        difficulty = data.getInt("difficulty");
        musicOn = data.getBoolean("music");
        regularMode = data.getBoolean("mode");
        vibrationOn = data.getBoolean("vibration");
        playerInfo = (PlayerInfo) data.getSerializable("player");
    }

    private void handleHighScore(){

        highScore = new HighScore(getApplicationContext().getSharedPreferences(SCORE_FILE, MODE_PRIVATE));
        highScore.readScores();
        highScore.addPlayer(playerInfo);
        highScore.writeScore();

    }

    @Override
    protected void onPause() {
        if(musicOn)
            OpenScreen.mediaPlayer.pause();;
        super.onPause();
    }

    @Override
    protected void onResume() {
        if(musicOn)
            OpenScreen.mediaPlayer.start();
        super.onResume();
    }

    private void setIds(){

        newGame = (Button)findViewById(R.id.newGame);
        mainMenu = (Button)findViewById(R.id.main_menu);
        restart = (Button)findViewById(R.id.restart);
        score = (TextView)findViewById(R.id.score);
    }


    private void addListenersButtons(){

        newGame.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                createNewGame();
            }
        });

        mainMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                goToMainMenu();
            }
        });

        restart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                restartGame();
            }
        });

    }

    private void createNewGame() {

        Intent intent = new Intent(this, Difficulty.class);
        intent.putExtra("lat", playerInfo.getLat());
        intent.putExtra("lon", playerInfo.getLon());
        intent.putExtra("music",musicOn);
        intent.putExtra("mode",regularMode);
        intent.putExtra("vibration",vibrationOn);
        startActivity(intent);
        finish();
    }

    private void restartGame() {
        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("name",playerInfo.getName());
        intent.putExtra("difficulty",difficulty);
        intent.putExtra("lat", playerInfo.getLat());
        intent.putExtra("lon", playerInfo.getLon());
        intent.putExtra("music",musicOn);
        intent.putExtra("mode",regularMode);
        intent.putExtra("vibration",vibrationOn);
        startActivity(intent);
        finish();
    }

    private void goToMainMenu() {

        Intent intent = new Intent(this, OpenScreen.class);
        intent.putExtra("music",musicOn);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
        finish();
    }
}