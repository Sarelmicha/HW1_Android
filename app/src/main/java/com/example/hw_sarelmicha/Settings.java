package com.example.hw_sarelmicha;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class Settings extends AppCompatActivity {
    private Button modeBtn;
    private Button musicBtn;
    private boolean musicOn;
    private boolean regularMode;
    private final String REGULAR_MODE = "Regular Mode";
    private final String FREE_DIVE_MODE = "Free Dive Mode";
    private final String MUSIC_ON = "Music On";
    private final String MUSIC_OFF = "Music Off";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        musicOn = getIntent().getExtras().getBoolean("music");
        regularMode = getIntent().getExtras().getBoolean("mode");
        setIds();
        setTextOnButtons();
        addListenersButtons();

    }

    private void setTextOnButtons() {
        if(musicOn)
            musicBtn.setText(MUSIC_OFF);
        else
            musicBtn.setText(MUSIC_ON);
        if(regularMode)
            modeBtn.setText(FREE_DIVE_MODE);
        else
            modeBtn.setText(REGULAR_MODE);
    }

    @Override
    protected void onResume() {
        if(musicOn)
            OpenScreen.mediaPlayer.start();
        super.onResume();
    }

    @Override
    protected void onPause() {
        if(musicOn)
            OpenScreen.mediaPlayer.pause();
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        Intent data = new Intent();
        data.putExtra("music", musicOn);
        data.putExtra("mode", regularMode);
        setResult(RESULT_OK, data);
        super.onBackPressed();
    }

    private void setIds(){
        //Initialize Buttons
        modeBtn = (Button)findViewById(R.id.mode);
        musicBtn = (Button)findViewById(R.id.music);
    }

    private void addListenersButtons(){

        musicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(musicBtn.getText().toString().equals(MUSIC_OFF)){
                    musicOn = false;
                    OpenScreen.mediaPlayer.pause();
                    musicBtn.setText(MUSIC_ON);
                } else {
                    musicOn = true;
                    OpenScreen.mediaPlayer.start();
                    musicBtn.setText(MUSIC_OFF);
                }
            }
        });

        modeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(modeBtn.getText().toString().equals(FREE_DIVE_MODE)){
                    regularMode = false;
                    modeBtn.setText(REGULAR_MODE);
                } else {
                    regularMode = true;
                    modeBtn.setText(FREE_DIVE_MODE);
                }
            }
        });
    }
}
