package com.example.hw_sarelmicha;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import java.io.Serializable;

public class OpenScreen extends AppCompatActivity {

    private Button newGameBtn;
    private Button exit;
    private MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_open_screen);

        newGameBtn = (Button)findViewById(R.id.newGameBtn);
        newGameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newGame();
            }
        });

        exit = (Button)findViewById(R.id.exitBtn);
       exit.setOnClickListener(new View.OnClickListener() {
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
