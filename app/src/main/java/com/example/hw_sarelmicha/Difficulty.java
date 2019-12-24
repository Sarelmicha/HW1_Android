package com.example.hw_sarelmicha;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

public class Difficulty extends Activity {

    private final int EASY_MODE = 3;
    private final int HARD_MODE = 4;
    private final int EXPERT_MODE = 5;
    private Button easyBtn;
    private Button hardBtn;
    private Button expertBtn;
    private EditText playerName;
    private boolean musicOn;
    private boolean regularMode;
    private boolean vibration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_difficulty);
        Bundle bundle = getIntent().getExtras();
        musicOn = bundle.getBoolean("music");
        regularMode = bundle.getBoolean("mode");
        vibration = bundle.getBoolean("vibration");
        setIds();
        playerName.addTextChangedListener(new InputValidator());
        setClickListeners();
    }

    @Override
    protected void onPause() {
        if(musicOn)
            OpenScreen.mediaPlayer.pause();
        super.onPause();
    }

    @Override
    protected void onResume() {
        if(musicOn)
            OpenScreen.mediaPlayer.start();
        super.onResume();
    }

    private void setIds(){

        easyBtn = (Button)findViewById(R.id.easy_btn);
        hardBtn = (Button)findViewById(R.id.hard_btn);
        expertBtn = (Button)findViewById(R.id.expert_btn);
        playerName = (EditText)findViewById(R.id.player_name);
    }

    private void setClickListeners(){

        easyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                passDifficultyToAnotherActivity(EASY_MODE);
            }
        });
        hardBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                passDifficultyToAnotherActivity(HARD_MODE);
            }
        });
        expertBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                passDifficultyToAnotherActivity(EXPERT_MODE);
            }
        });
    }

    private void passDifficultyToAnotherActivity(int difficulty){

        Intent intent = new Intent(this, MainActivity.class);
        intent.putExtra("difficulty",difficulty);
        if(InputValidator.isNameEmpty(playerName.getText().toString()))
            intent.putExtra("name", "Player");
         else
            intent.putExtra("name", playerName.getText().toString());

         Bundle bundle = getIntent().getExtras();
        intent.putExtra("freeDive",bundle.getBoolean("mode"));
        intent.putExtra("music",bundle.getBoolean("music"));
        intent.putExtra("lat",bundle.getDouble("lat"));
        intent.putExtra("lon", bundle.getDouble("lon"));
        intent.putExtra("music", musicOn);
        intent.putExtra("mode",regularMode);
        intent.putExtra("vibration",vibration);

        startActivity(intent);
        finish();
    }
}
