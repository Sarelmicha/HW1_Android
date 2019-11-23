package com.example.hw_sarelmicha;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class OpenScreen extends AppCompatActivity {

    private Button newGameBtn;

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

    }

    private void newGame() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }
}
