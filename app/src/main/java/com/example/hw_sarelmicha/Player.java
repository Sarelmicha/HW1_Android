package com.example.hw_sarelmicha;

import android.app.Activity;
import android.content.Context;
import android.hardware.SensorEvent;
import android.media.MediaPlayer;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.widget.RelativeLayout;

import java.io.IOException;

public class Player extends  GameObject {

    private View[] life;
    private int numOfLife = 3;
    private MediaPlayer ouchSound;
    private MediaPlayer biteSound;
    private final int STEP = 100;
    private Effects effects;


    public Player(Context context, int screenWidth, int screenHeight,RelativeLayout mainLayout) {
        super(context, screenWidth, screenHeight);
        effects = new Effects();
        addPlayerToScreen(mainLayout, screenWidth);
        setLife(context,mainLayout);
        setSounds(context);
    }

    public void addPlayerToScreen(RelativeLayout mainLayout, int screenWidth){

        RelativeLayout.LayoutParams params = new  RelativeLayout.LayoutParams(200, 200);
        params.rightMargin = (int)(Math.random() * (((screenWidth - 100) - 100) + 1));
        params.bottomMargin = 60;
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        this.setLayoutParams(params);
        this.setBackgroundResource(R.drawable.playerleft);
        this.setAnimation(effects.fadeInEffect());

        mainLayout.addView(this);
    }

    public void setSounds(Context context){
        ouchSound = MediaPlayer.create(context, R.raw.ouchsound);
        biteSound = MediaPlayer.create(context, R.raw.bite);
    }

    public void setLife(Context context,RelativeLayout mainLayout) {

        final int BUBBLE_WIDTH = 130;
        final int BUBBLE_HEIGHT = 100;

        life = new View[numOfLife];


        for (int i = 0; i < numOfLife ; i++) {

            RelativeLayout.LayoutParams params = new  RelativeLayout.LayoutParams(BUBBLE_WIDTH, BUBBLE_HEIGHT);
            params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            params.topMargin = 20;
            params.rightMargin = i * 120;
            life[i] = new View(context);
            life[i].setLayoutParams(params);
            life[i].setBackgroundResource(R.drawable.life);
            life[i].setAnimation(effects.fadeInEffect());
            mainLayout.addView(life[i]);
        }
    }

    public void moveLeft(){

        if (this.getX() < this.getScreenWidth() - this.getWidth()){
            this.setX(this.getX() + STEP);
            this.setBackgroundResource(R.drawable.playerleft);
        }
    }

    public void moveRight(){

        if (this.getX() > 0) {
            this.setX(this.getX() - STEP);
            this.setBackgroundResource(R.drawable.playerright);
        }
    }

    public void moveUpWithSensors(SensorEvent sensorEvent){

        if(sensorEvent.values[0] > 0) // move right and up
            this.setBackgroundResource(R.drawable.rightup);
        else
            this.setBackgroundResource(R.drawable.leftup);

        this.setY(this.getY() + (int) sensorEvent.values[1] - 10);
    }

    public void moveDownWithSensors(SensorEvent sensorEvent){
        if(sensorEvent.values[0] > 0) // move right and down
            this.setBackgroundResource(R.drawable.rightdown);
        else
            this.setBackgroundResource(R.drawable.leftdown);

        this.setY(this.getY() + (int) sensorEvent.values[1] + 10);
    }

    public void moveLeftWithSensors(SensorEvent sensorEvent){
        this.setBackgroundResource(R.drawable.playerleft);
        this.setX((this.getX() - (int) sensorEvent.values[0] + 10));
    }

    public void moveRightWithSensors(SensorEvent sensorEvent){

        this.setBackgroundResource(R.drawable.playerright);
        this.setX((this.getX() - (int) sensorEvent.values[0] - 10));
    }


    public synchronized void reduceLife() {

        Animation fadeOut = effects.fadeOutEffect();
        numOfLife--;
        life[numOfLife].setAnimation(fadeOut);
        life[numOfLife].setVisibility(View.INVISIBLE);

    }

    public synchronized void deduceLife() {

        Animation fadeIn = effects.fadeInEffect();

        if(numOfLife == 3)
            return;
        life[numOfLife].setAnimation(fadeIn);
        life[numOfLife].setVisibility(View.VISIBLE);
        numOfLife++;

    }


    public void makeOuchSound() {
        ouchSound.setVolume(0, 0.7f);
        if(ouchSound.isPlaying()){
            ouchSound.stop();
            try {
                ouchSound.prepare();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        ouchSound.start();
    }

    public void makeBiteSound(){
        biteSound.start();
    }

    public int getNumOfLife() {
        return numOfLife;
    }
}
