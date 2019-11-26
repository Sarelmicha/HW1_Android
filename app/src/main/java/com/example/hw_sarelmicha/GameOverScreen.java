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

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Scanner;

public class GameOverScreen extends AppCompatActivity {

    private Button mainMenu;
    private Button restart;
    private Button exit;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_over_screen);

        mainMenu = (Button)findViewById(R.id.main_menu);
        restart = (Button)findViewById(R.id.restart);
        exit = (Button)findViewById(R.id.exit);

        addListenersButtons();

    }

    @Override
    protected void onPause() {
        Log.d("CHECK", "onPause:im on pause now! ");
            OpenScreen.mediaPlayer.pause();;
        super.onPause();
    }

    @Override
    protected void onResume() {
        Log.d("CHECK", "onPause:im on resume now! ");
            OpenScreen.mediaPlayer.start();
        super.onResume();
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