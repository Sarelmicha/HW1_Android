package com.example.hw_sarelmicha;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import java.io.Serializable;

public class OpenScreen extends AppCompatActivity {

    private Button newGameBtn;
    private Button exitBtn;
    public static MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_screen);
        setIds();
        startSoundtrack();
        addListenersButtons();
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
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    public static void exitGame() {
        System.exit(0);
    }
}
