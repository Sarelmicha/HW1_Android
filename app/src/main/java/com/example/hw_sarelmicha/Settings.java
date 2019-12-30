package com.example.hw_sarelmicha;

import android.content.Intent;
import android.content.SharedPreferences;
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
    private Button vibrationBtn;
    private boolean musicOn;
    private boolean regularMode;
    private boolean vibrationOn;
    private SharedPreferences sharedPreferences;
    private final String REGULAR_MODE = "Regular Mode";
    private final String FREE_DIVE_MODE = "Free Dive Mode";
    private final String MUSIC_ON = "Music On";
    private final String MUSIC_OFF = "Music Off";
    private final String VIBRATION_ON = "Vibration On";
    private final String VIBRATION_OFF = "Vibration Off";
    private final String SETTINGS_FILE = "SettingsFile";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        getSettingsStateFromFile();
        setIds();
        setTextOnButtons();
        addListenersButtons();

    }

    private void setTextOnButtons() {
        if(musicOn)
            musicBtn.setText(MUSIC_ON);
        else
            musicBtn.setText(MUSIC_OFF);
        if(regularMode)
            modeBtn.setText(REGULAR_MODE);
        else
            modeBtn.setText(FREE_DIVE_MODE);
        if(vibrationOn)
            vibrationBtn.setText(VIBRATION_ON);
        else
            vibrationBtn.setText(VIBRATION_OFF);
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

        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean("music",musicOn);
        editor.putBoolean("mode",regularMode);
        editor.putBoolean("vibration",vibrationOn);
        editor.commit();
        super.onPause();
    }

    @Override
    public void onBackPressed() {
        Intent data = new Intent();
        data.putExtra("music", musicOn);
        data.putExtra("mode", regularMode);
        data.putExtra("vibration",vibrationOn);
        setResult(RESULT_OK, data);
        super.onBackPressed();
    }

    private void getSettingsStateFromFile(){

        sharedPreferences = (getApplicationContext().getSharedPreferences(SETTINGS_FILE, MODE_PRIVATE));
        musicOn = sharedPreferences.getBoolean("music",true);
        regularMode = sharedPreferences.getBoolean("mode", true);
        vibrationOn = sharedPreferences.getBoolean("vibration", true);
    }

    private void setIds(){
        //Initialize Buttons
        modeBtn = (Button)findViewById(R.id.mode);
        musicBtn = (Button)findViewById(R.id.music);
        vibrationBtn = (Button)findViewById(R.id.vibration);

    }

    private void addListenersButtons(){

        musicBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(musicBtn.getText().toString().equals(MUSIC_ON)){
                    musicOn = false;
                    OpenScreen.mediaPlayer.pause();
                    musicBtn.setText(MUSIC_OFF);
                } else {
                    musicOn = true;
                    OpenScreen.mediaPlayer.start();
                    musicBtn.setText(MUSIC_ON);
                }
            }
        });

        modeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(modeBtn.getText().toString().equals(REGULAR_MODE)){
                    regularMode = false;
                    modeBtn.setText(FREE_DIVE_MODE);
                } else {
                    regularMode = true;
                    modeBtn.setText(REGULAR_MODE);
                }
            }
        });

        vibrationBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(vibrationBtn.getText().toString().equals(VIBRATION_ON)){
                    vibrationOn = false;
                    vibrationBtn.setText(VIBRATION_OFF);
                } else {
                    vibrationOn = true;
                    vibrationBtn.setText(VIBRATION_ON);
                }
            }
        });
    }
}
