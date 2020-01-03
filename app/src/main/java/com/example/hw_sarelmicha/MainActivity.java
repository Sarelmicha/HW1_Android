//Created by Mor Soferian and Sarel Micha

package com.example.hw_sarelmicha;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Handler;
import android.os.Vibrator;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends FragmentActivity implements SensorEventListener {

    private final int MAX_ENEMIES = 3;
    private final int MIN_ENEMIES = 0;
    private final int MIN_DURATION = 3000;
    private final int MAX_DURATION = 5000;

    private int NUM_OF_COLS;
    private int screenWidth;
    private int screenHeight;
    private int animationIndex;

    private PlayerInfo playerInfo;
    private Player player;
    private JellyFish jellyFish;

    private int score = 0;
    private FallingObject[] dropObjects;
    private View[] life;

    private LinearLayout[] cols;
    private ValueAnimator[] animations;
    private Handler handler;
    private RelativeLayout mainLayout;
    private RelativeLayout leftScreen;
    private RelativeLayout rightScreen;
    private LinearLayout linearLayoutsContainer;

    private TextView scoreView;
    private boolean makeJelly = true;
    private SensorManager sensorManager;
    private Sensor accelerometer;
    private boolean regularMode;
    private Vibrator vibrator;
    private Effects effects;
    private View icon;
    private boolean musicOn;
    private boolean vibrationOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Get Mode from user
        handleData();
        setScreenHeightAndWidth();
        setIds();
        player = new Player(this,screenWidth,mainLayout,170,170, new Effects());
        if(vibrationOn)
            vibrator = (Vibrator) getSystemService(Context.VIBRATOR_SERVICE);
        effects = new Effects();

        FallingObject.addEnemiesPics();
        addFallingObjects(NUM_OF_COLS);
        setUpAnimations();

        if(regularMode) {
            addClickListeners();
            setInstructionIcon(R.drawable.click);
        }
        else{
            setUpSensors();
            setInstructionIcon(R.drawable.phonerotate);
        }
    }

    private void handleData() {

        Bundle data = getIntent().getExtras();
        regularMode = data.getBoolean("mode");
        NUM_OF_COLS = data.getInt("difficulty");
        playerInfo = new PlayerInfo(data.getString("name"),score,data.getDouble("lat"),data.getDouble("lon"));
        musicOn = data.getBoolean("music");
        vibrationOn = data.getBoolean("vibration");
    }

    private void setInstructionIcon(int photo){

        final int DELAY_TIME = 4 * 1000; //4 seconds to be on screen

        RelativeLayout.LayoutParams params = new  RelativeLayout.LayoutParams(300, 300);

        params.addRule(RelativeLayout.CENTER_IN_PARENT);
        icon = new View(this);
        icon.setBackgroundResource(photo);
        icon.setAnimation(effects.fadeInEffect());
        icon.setLayoutParams(params);
        mainLayout.addView(icon);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                icon.setAnimation(effects.fadeOutEffect());
                icon.setVisibility(View.INVISIBLE);
                icon = null;
            }
        },DELAY_TIME);
    }

        @Override
    protected void onResume() {
        final int DELAY_TIME = 30 * 1000; //30 seconds for a new JellyFish

        if(!regularMode)
            sensorManager.registerListener(this, accelerometer, SensorManager.SENSOR_DELAY_GAME);
        if(musicOn)
            OpenScreen.mediaPlayer.start();
        makeJelly = true;

        final Runnable createJellyfish = new Runnable() {
            @Override
            public void run() {
                if(makeJelly){
                    jellyFish = new JellyFish(MainActivity.this,screenWidth,mainLayout,new Effects());
                    handler.postDelayed(this, DELAY_TIME);
                }
            }
        };
        //trigger first time
        handler.postDelayed(createJellyfish,DELAY_TIME);

        for (int i = 0; i < NUM_OF_COLS; i++) {
            animations[i].resume();
        }
        super.onResume();
    }

    @Override
    protected void onPause() {
        if (musicOn)
            OpenScreen.mediaPlayer.pause();
        if (!regularMode)
            sensorManager.unregisterListener(this);

        makeJelly = false;

        for (int i = 0; i < NUM_OF_COLS; i++) {
            animations[i].pause();
        }

        super.onPause();
    }

    private void setUpSensors(){

        sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        accelerometer = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        sensorManager.registerListener(this,accelerometer,SensorManager.SENSOR_DELAY_GAME);
    }

    private void setIds(){

        mainLayout = (RelativeLayout) findViewById(R.id.main_layout);
        linearLayoutsContainer = (LinearLayout) findViewById(R.id.linearLayouts_container);
        leftScreen = (RelativeLayout) findViewById(R.id.left_screen);
        rightScreen = (RelativeLayout) findViewById(R.id.right_screen);
        scoreView = (TextView) findViewById(R.id.score);
        handler = new Handler();
    }


    private void setUpAnimations() {

        animations = new ValueAnimator[NUM_OF_COLS];

        for (animationIndex = 0; animationIndex < NUM_OF_COLS; animationIndex++) {
            setAnimationParameters();
            animations[animationIndex].addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                private int x = animationIndex;

                @Override
                public void onAnimationUpdate(ValueAnimator updatedAnimation) {

                    float animatedValue = (float) updatedAnimation.getAnimatedValue();

                    dropObjects[x].setTranslationY(animatedValue);
                    dropObjects[x].setVisibility(View.VISIBLE);

                    if (player.isCollide(dropObjects[x])) {
                        collideWithObjectOccurred(updatedAnimation,x);
                    } else if (dropObjects[x].isOutOfHeightScreen(screenHeight)) {
                        objectIsOutOfScreen(updatedAnimation,x);
                    }
                    if(jellyFish != null){
                        if((float)jellyFish.getJellyCounter().getAnimatedValue() > 0.5) {
                            jellyFish.disappeared();
                            jellyFish = null;
                        }
                    }
                }
            });

            animations[animationIndex].start();
        }
    }

    private void setAnimationParameters(){

        final float initialHeight = -300;

        animations[animationIndex] = ValueAnimator.ofFloat(initialHeight, screenHeight + 20);
        animations[animationIndex].setDuration(MAX_DURATION + (long)(Math.random() * (MAX_DURATION - MIN_DURATION)));
        animations[animationIndex].setStartDelay((long) (Math.random() * (MIN_DURATION)));
        animations[animationIndex].setRepeatCount(Animation.INFINITE);
    }



    private void objectIsOutOfScreen(ValueAnimator updatedAnimation,int x){
        updatedAnimation.setDuration(MIN_DURATION + (long)(Math.random() * (MAX_DURATION - MIN_DURATION)));
        updatedAnimation.setStartDelay((long) (Math.random() * (1000)));
        dropObjects[x].setBackgroundResource(FallingObject.dropObjectsPics[(int)(Math.random() * ((MAX_ENEMIES - MIN_ENEMIES) + 1))]);
        if(dropObjects[x].getBackground().getConstantState()==getResources().getDrawable(R.drawable.coin).getConstantState()){
            dropObjects[x].setWidthAndHeight(150,150);
        } else {
            dropObjects[x].setWidthAndHeight(screenWidth / NUM_OF_COLS, 250);
        }
        updatedAnimation.start();
    }

    private void collideWithObjectOccurred(ValueAnimator updatedAnimation,int x){

        if(dropObjects[x].getBackground().getConstantState()== getResources().getDrawable(R.drawable.coin).getConstantState()){
            if(musicOn)
                dropObjects[x].makeCoinSound();
            updateScore();
        } else {
            if(vibrationOn)
                vibrator.vibrate(400);
            if(musicOn)
                player.makeOuchSound();
            player.reduceLife();
            if (player.getNumOfLife() == 0)
                endGame();
        }
        dropObjects[x].setBackgroundResource(FallingObject.dropObjectsPics[(int)(Math.random() * ((MAX_ENEMIES - MIN_ENEMIES) + 1))]);
        if(dropObjects[x].getBackground().getConstantState()==getResources().getDrawable(R.drawable.coin).getConstantState()){
            dropObjects[x].setWidthAndHeight(150, 150);
        } else {
            dropObjects[x].setWidthAndHeight(screenWidth / NUM_OF_COLS, 250);
        }
            updatedAnimation.setStartDelay(0);
            updatedAnimation.start();
    }

    private void collideWithJellyfishOccurred(){

        jellyFish.setVisibility(View.INVISIBLE);
        jellyFish = null;
        if(musicOn)
            player.makeBiteSound();
        player.deduceLife();
    }

    private void setScreenHeightAndWidth() {

        //Get Screen size
        WindowManager wm = getWindowManager();
        Display disp = wm.getDefaultDisplay();
        Point size = new Point();
        disp.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;
    }

    private synchronized void updateScore() {

        score+= 1;
        scoreView.setText("SCORE:" + score);
    }

    private void endGame() {
        //set player score before sending it to gameOverScreen
        playerInfo.setScore(score);
        Intent intent = new Intent(this, GameOverScreen.class);
        intent.putExtra("difficulty",NUM_OF_COLS);
        intent.putExtra("player",playerInfo);
        intent.putExtra("music",musicOn);
        intent.putExtra("mode",regularMode);
        intent.putExtra("vibration",vibrationOn);
        startActivity(intent);
        finish();
    }

    private void addFallingObjects(int numOfCols) {
        dropObjects = new FallingObject[numOfCols];

        cols = new LinearLayout[numOfCols];
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);

        for (int i = 0; i < numOfCols; i++) {

            cols[i] = new LinearLayout(MainActivity.this);
            cols[i].setLayoutParams(lp);
            dropObjects[i] = new FallingObject(MainActivity.this,FallingObject.dropObjectsPics[(int)(Math.random() * ((MAX_ENEMIES - MIN_ENEMIES) + 1))]);
            if(dropObjects[i].getBackground().getConstantState()==getResources().getDrawable(R.drawable.coin).getConstantState()){
                dropObjects[i].setWidthAndHeight(150,150);
            } else {
                dropObjects[i].setWidthAndHeight(screenWidth / numOfCols, 250);
            }
            addGravity(dropObjects[i], Gravity.TOP);
            dropObjects[i].setVisibility(View.INVISIBLE);
            cols[i].addView(dropObjects[i]);
            linearLayoutsContainer.addView(cols[i]);
        }
    }

    private void addClickListeners() {

        rightScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               player.moveRight();
                if (jellyFish != null) {
                    if (player.isCollide(jellyFish)){
                        collideWithJellyfishOccurred();
                    }
                }
            }
        });

        leftScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               player.moveLeft(screenWidth);
                if(jellyFish != null){
                    if(player.isCollide(jellyFish)){
                       collideWithJellyfishOccurred();
                    }
                }
            }
        });
    }

    private void addGravity(View view, int... gravity) {
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();

        for (int x : gravity) {
            params.gravity = x;
        }
        view.setLayoutParams(params); //causes layout update
    }

    @Override
    public void onSensorChanged(SensorEvent sensorEvent) {

        if (sensorEvent.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            //Move Left and Right
            if ((player.getX() < screenWidth - player.getWidth() || (int) sensorEvent.values[0] > 0)
                    && (player.getX() > 0 || (int) sensorEvent.values[0] < 0 )) {
                if((int) sensorEvent.values[0] <= 0){
                  player.moveLeftWithSensors(sensorEvent);
                } else {
                    player.moveRightWithSensors(sensorEvent);
                }
            }
            //Move Up and Down
            if ((player.getY() < screenHeight - player.getHeight() || (int) sensorEvent.values[1] < 0)
                    && (player.getY() > player.getHeight() || (int) sensorEvent.values[1] > 0 )){
                if((int) sensorEvent.values[1] > 0){
                    //Move down
                    player.moveDownWithSensors(sensorEvent);
                } else {
                    //Move up
                    player.moveUpWithSensors(sensorEvent);
                }
            }
            //Collide Jellyfish
            if(jellyFish != null){
                if(player.isCollide(jellyFish)){
                  collideWithJellyfishOccurred();
                }
            }
        }
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int i) {

    }
}