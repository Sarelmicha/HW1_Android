package com.example.hw_sarelmicha;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.media.MediaPlayer;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;;
import android.view.View;
import android.widget.Button;

import com.google.gson.Gson;


public class OpenScreen extends Activity {

    private Button newGameBtn;
    private Button exitBtn;
    private HighScore highScore;
    public static MediaPlayer mediaPlayer;
    private final String SCORE_FILE =  "Scores";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_screen);
        setIds();
        startSoundtrack();
        addListenersButtons();


        SharedPreferences sharedPreferences = getApplicationContext().getSharedPreferences(SCORE_FILE, MODE_PRIVATE);
        highScore = new HighScore(sharedPreferences);

        Player p1 = new Player("mor", 100, "1", "1");
        highScore.addPlayer(p1);
        highScore.showAllPlayers();
        Player p2 = new Player("srul", 100, "1", "1");
        highScore.addPlayer(p2);
        highScore.writeScore();
        highScore.readScores();
        highScore.showAllPlayers();
    }




    @Override
    protected void onPause() {
        mediaPlayer.pause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        mediaPlayer.start();
         super.onResume();

    }
    private void setIds(){

        //Initialize Buttons
        newGameBtn = (Button)findViewById(R.id.newGameBtn);
        exitBtn = (Button)findViewById(R.id.exitBtn);
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

    public static void exitGame() {
        System.exit(0);
    }
}
