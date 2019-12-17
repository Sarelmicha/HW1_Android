package com.example.hw_sarelmicha;

import android.content.Context;
import android.media.MediaPlayer;
import android.util.Log;

import java.io.IOException;

public class FallingObject extends GameObject {

    public static final int NUM_OF_PICS = 4;
    public static final int MAX_ENEMIES = 3;
    public static int MIN_ENEMIES = 0;
    public static int[] dropObjectsPics;
    private MediaPlayer coinSound;


    public FallingObject(Context context, int screenWidth, int screenHeight, int background) {
        super(context, screenWidth, screenHeight);
        this.setBackgroundResource(background);
        coinSound = MediaPlayer.create(context,R.raw.coinsound);

    }

    public static void addEnemiesPics() {

        dropObjectsPics = new int[NUM_OF_PICS];
        dropObjectsPics[0] = R.drawable.plastic;
        dropObjectsPics[1]= R.drawable.plastic1;
        dropObjectsPics[2]= R.drawable.plastic2;
        dropObjectsPics[3] = R.drawable.coin;
    }

    public void makeCoinSound(){
        if(coinSound.isPlaying()) {
            coinSound.stop();
            try {
                coinSound.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        coinSound.start();
    }


}
