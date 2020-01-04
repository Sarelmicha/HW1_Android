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

    private final int MAX_NUM_OF_LIFE = 3;
    private int playerWidth;
    private int playerHeight;
    private final int SIZE_TO_ADD = 52;
    private View[] life;
    private int numOfLife = MAX_NUM_OF_LIFE;
    private MediaPlayer ouchSound;
    private MediaPlayer biteSound;
    private final int STEP = 100;
    private Effects effects;
    private RelativeLayout.LayoutParams playerParams;


    public Player(Context context,int screenWidth,RelativeLayout mainLayout,int width, int height,Effects effects) {
        super(context);
        this.effects = effects;
        setLife(context,mainLayout);
        setSounds(context);
        setPlayerWidth(width);
        setPlayerHeight(height);
        addPlayerToScreen(mainLayout, screenWidth);
    }

    public void addPlayerToScreen(RelativeLayout mainLayout, int screenWidth){

        final int BOTTOM_MARGIN = 30;

        playerParams = new  RelativeLayout.LayoutParams(playerWidth, playerHeight);
        playerParams.bottomMargin = BOTTOM_MARGIN;
        playerParams.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        playerParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        this.setLayoutParams(playerParams);
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
        final int TOP_MARGIN = 20;

        life = new View[numOfLife];

        for (int i = 0; i < numOfLife ; i++) {

            RelativeLayout.LayoutParams params = new  RelativeLayout.LayoutParams(BUBBLE_WIDTH, BUBBLE_HEIGHT);
            params.addRule(RelativeLayout.ALIGN_PARENT_TOP);
            params.addRule(RelativeLayout.ALIGN_PARENT_RIGHT);
            params.topMargin = TOP_MARGIN;
            params.rightMargin = i * 120;
            life[i] = new View(context);
            life[i].setLayoutParams(params);
            life[i].setBackgroundResource(R.drawable.life);
            life[i].setAnimation(effects.fadeInEffect());
            mainLayout.addView(life[i]);
        }
    }

    public void moveLeft(int screenWidth){

        if (this.getX() < screenWidth - this.getWidth()){
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

        if(sensorEvent.values[0] > 0) {// move right and up
                if (this.getBackground().getConstantState() != getResources().getDrawable(R.drawable.rightup).getConstantState()) {
                this.setBackgroundResource(R.drawable.rightup);
                playerParams.width = playerWidth + SIZE_TO_ADD;
                playerParams.height = playerHeight + SIZE_TO_ADD;
                }
        }
        else {
                if(this.getBackground().getConstantState() != getResources().getDrawable(R.drawable.leftup).getConstantState()) {
                this.setBackgroundResource(R.drawable.leftup);
                playerParams.width = playerWidth + SIZE_TO_ADD;
                playerParams.height = playerHeight + SIZE_TO_ADD;
            }
        }

        this.setY(this.getY() + (int) sensorEvent.values[1] - 10);
    }

    public void moveDownWithSensors(SensorEvent sensorEvent){
        if(sensorEvent.values[0] > 0){ // move right and down
                if(this.getBackground().getConstantState() != getResources().getDrawable(R.drawable.rightdown).getConstantState()) {
                this.setBackgroundResource(R.drawable.rightdown);
                playerParams.width = playerWidth + SIZE_TO_ADD;
                playerParams.height = playerHeight + SIZE_TO_ADD;
                }
        }
        else {
                if (this.getBackground().getConstantState() != getResources().getDrawable(R.drawable.leftdown).getConstantState()) {
                this.setBackgroundResource(R.drawable.leftdown);
                playerParams.width = playerWidth + SIZE_TO_ADD;
                playerParams.height = playerHeight + SIZE_TO_ADD;
            }
        }
        this.setY(this.getY() + (int) sensorEvent.values[1] + 10);
    }

    public void moveLeftWithSensors(SensorEvent sensorEvent){
        if(this.getBackground().getConstantState() != getResources().getDrawable(R.drawable.playerleft).getConstantState()) {
            this.setBackgroundResource(R.drawable.playerleft);
            playerParams.width = playerWidth;
            playerParams.height = playerHeight;
        }
        this.setX((this.getX() - (int) sensorEvent.values[0] + 10));
    }

    public void moveRightWithSensors(SensorEvent sensorEvent){
        if(this.getBackground().getConstantState() != getResources().getDrawable(R.drawable.playerright).getConstantState()) {
            this.setBackgroundResource(R.drawable.playerright);
            playerParams.width = playerWidth;
            playerParams.height = playerHeight;
        }
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

        if(numOfLife == MAX_NUM_OF_LIFE)
            return;
        life[numOfLife].setAnimation(fadeIn);
        life[numOfLife].setVisibility(View.VISIBLE);
        numOfLife++;
    }

    public void changeSize(int size){
        setPlayerWidth(getWidth() + size);
        setPlayerHeight(getHeight() + size);
        playerParams.width = playerWidth;
        playerParams.height = playerHeight;
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

    public void setPlayerWidth(int playerWidth) {
        this.playerWidth = playerWidth;
    }

    public void setPlayerHeight(int playerHeight) {
        this.playerHeight = playerHeight;
    }
}
