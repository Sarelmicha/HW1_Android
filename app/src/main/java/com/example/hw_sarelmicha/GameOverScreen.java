package com.example.hw_sarelmicha;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

public class GameOverScreen extends AppCompatActivity {

    private Button mainMenu;
    private Button restart;
    private Button exit;
    private TextView score;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over_screen);
        setIds();
        Bundle scoreData = getIntent().getExtras();
        score.setText(score.getText().toString() + scoreData.getInt("score"));
        addListenersButtons();
    }

    @Override
    protected void onPause() {
            OpenScreen.mediaPlayer.pause();;
        super.onPause();
    }

    @Override
    protected void onResume() {
            OpenScreen.mediaPlayer.start();
        super.onResume();
    }

    private void setIds(){

        mainMenu = (Button)findViewById(R.id.main_menu);
        restart = (Button)findViewById(R.id.restart);
        exit = (Button)findViewById(R.id.exit);
        score = (TextView)findViewById(R.id.score);
    }


    private void addListenersButtons(){

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

        exit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finishAffinity();
                OpenScreen.exitGame();
            }
        });
    }

    private void restartGame() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    private void goToMainMenu() {
        Intent intent = new Intent(this, OpenScreen.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
        finish();
    }
}