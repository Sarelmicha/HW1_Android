//Created by Mor Soferian and Sarel Micha
package com.example.hw_sarelmicha;

import android.animation.ValueAnimator;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    private final int NUM_OF_COLS = 3;
    private final int NUM_OF_PICS = 3;
    private final int STEP = 80;
    private final int MAX_ENEMIES = 2;
    private final int MIN_ENEMIES = 0;
    final int MIN_DURATION = 3000;
    final int MAX_DURATION = 7000;
    private int numOfLife = 3;
    private int score = 0;
    private View[] enemies;
    private int[] enemiesPics;
    private View[] life;
    private View jellyFish;
    private LinearLayout[] cols;
    private ValueAnimator[] animations;
    private ValueAnimator jellyCounter;
    private Handler handler;
    private RelativeLayout mainLayout;
    private RelativeLayout leftScreen;
    private RelativeLayout rightScreen;
    private LinearLayout linearLayoutsContainer;
    private View player;
    private int screenWidth;
    private int screenHeight;
    private static int animationIndex;
    private MediaPlayer ouchSound;
    private MediaPlayer biteSound;
    private TextView scoreView;
    private boolean makeJelly = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setScreenHeightAndWidth();
        setIds();
        addClickListeners();
        addEnemiesPics();
        addEnemies(NUM_OF_COLS);
        addLife();
        setUpAnimations();
    }

    @Override
    protected void onResume() {
        final int DELAY_TIME = 30 * 1000; //30 seconds for jelly
        OpenScreen.mediaPlayer.start();

        makeJelly = true;


        final Runnable createJellyfish = new Runnable() {
            @Override
            public void run() {
                if(makeJelly){
                    addJellyfish();
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
        OpenScreen.mediaPlayer.pause();

        makeJelly = false;

        for (int i = 0; i < NUM_OF_COLS; i++) {
            animations[i].pause();
        }

        super.onPause();
    }

    private void setIds(){

        mainLayout = (RelativeLayout) findViewById(R.id.main_layout);
        linearLayoutsContainer = (LinearLayout) findViewById(R.id.linearLayouts_container);
        player = (View) findViewById(R.id.player);
        leftScreen = (RelativeLayout) findViewById(R.id.left_screen);
        rightScreen = (RelativeLayout) findViewById(R.id.right_screen);
        ouchSound = MediaPlayer.create(this, R.raw.ouchsound);
        biteSound = MediaPlayer.create(this, R.raw.bite);
        scoreView = (TextView) findViewById(R.id.score);
        handler = new Handler();
    }

    private void setJellyCounter(){

        final int timeToLive = 10 * 1000; //10 seconds for jelly to live

        jellyCounter = new ValueAnimator();
        jellyCounter = ValueAnimator.ofFloat(0, 1);
        jellyCounter.setDuration(timeToLive);
        jellyCounter.start();
    }

    private void addJellyfish() {

        jellyFish = new View(this);
        RelativeLayout.LayoutParams params = new  RelativeLayout.LayoutParams(200, 200);
        params.rightMargin = (int)(Math.random() * (((screenWidth - 100) - 100) + 1));
        params.bottomMargin = 60;
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        jellyFish.setLayoutParams(params);
        jellyFish.setBackgroundResource(R.drawable.jellyfish);
        jellyFish.setAnimation(fadeInEffect());

        mainLayout.addView(jellyFish);
        setJellyCounter();

    }

    private void addEnemiesPics() {

        enemiesPics = new int[NUM_OF_PICS];
        enemiesPics[0] = R.drawable.plastic;
        enemiesPics[1]= R.drawable.plastic1;
        enemiesPics[2]= R.drawable.plastic2;
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

                    enemies[x].setTranslationY(animatedValue);
                    enemies[x].setVisibility(View.VISIBLE);

                    if (isCollide(enemies[x])) {
                        collideWithEnemyOccurred(updatedAnimation);
                    } else if (isOutOfHeightScreen(enemies[x])) {
                        enemyIsOutOfScreen(updatedAnimation,x);
                    }
                    if(jellyFish != null){
                        if((float)jellyCounter.getAnimatedValue() > 0.5) {
                            makeJellyDisappeared();
                        }
                    }
                }
            });

            animations[animationIndex].start();
        }
    }

    private void makeJellyDisappeared(){
        jellyFish.setAnimation(fadeOutEffect());
        jellyFish.setVisibility(View.INVISIBLE);
        jellyFish = null;
        jellyCounter.end();
    }

    private void setAnimationParameters(){

        final float initialHeight = -300;

        animations[animationIndex] = ValueAnimator.ofFloat(initialHeight, screenHeight);
        animations[animationIndex].setDuration(MAX_DURATION + (long)(Math.random() * (MAX_DURATION - MIN_DURATION)));
        animations[animationIndex].setStartDelay((long) (Math.random() * (MIN_DURATION)));
        animations[animationIndex].setRepeatCount(Animation.INFINITE);
    }

    private boolean isOutOfWidthScreen(View view){
        if(view.getX() >= screenWidth - 1)
            return true;
        return false;
    }

    private boolean isOutOfHeightScreen(View view){
        if(view.getY() >= screenHeight - 1)
            return true;
        return false;
    }

    private void enemyIsOutOfScreen(ValueAnimator updatedAnimation,int x){
        updateScore();
        updatedAnimation.setDuration(MIN_DURATION + (long)(Math.random() * (MAX_DURATION - MIN_DURATION)));
        updatedAnimation.setStartDelay((long) (Math.random() * (1000)));
        enemies[x].setBackgroundResource(enemiesPics[(int)(Math.random() * ((MAX_ENEMIES - MIN_ENEMIES) + 1))]);
        updatedAnimation.start();
    }

    private void collideWithEnemyOccurred(ValueAnimator updatedAnimation){

        makeOuchSound();
        updatedAnimation.setStartDelay(0);
        updatedAnimation.start();
        reduceLife();
    }

    private void collideWithJellyfishOccurred(){

        jellyFish.setVisibility(View.INVISIBLE);
        jellyFish = null;
        makeBiteSound();
        deduceLife();
    }

    private void makeOuchSound() {
        ouchSound.setVolume(0, 0.7f);
        ouchSound.start();
    }

    private void makeBiteSound(){
        biteSound.start();

    }

    private void addLife() {
        life = new View[numOfLife];

        life[0] = (View) findViewById(R.id.life0);
        life[1] = (View) findViewById(R.id.life1);
        life[2] = (View) findViewById(R.id.life2);
    }

    void setScreenHeightAndWidth() {

        //Get Screen size
        WindowManager wm = getWindowManager();
        Display disp = wm.getDefaultDisplay();
        Point size = new Point();
        disp.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;
    }

    private synchronized void updateScore() {

        score++;
        scoreView.setText("Score:" + score);
    }

    private synchronized void reduceLife() {

        Animation fadeOut = fadeOutEffect();

        numOfLife--;
        life[numOfLife].setAnimation(fadeOut);
        life[numOfLife].setVisibility(View.INVISIBLE);

        if (numOfLife == 0)
            endGame();
    }

    private Animation fadeInEffect(){

        //Fade In effect
        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new AccelerateInterpolator());
        fadeIn.setStartOffset(0);
        fadeIn.setDuration(500);

        return fadeIn;
    }

    private Animation fadeOutEffect(){

        //Fade Out effect
        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator());
        fadeOut.setStartOffset(0);
        fadeOut.setDuration(500);

        return fadeOut;
    }

    private synchronized void deduceLife() {

        Animation fadeIn = fadeInEffect();

        if(numOfLife == 3)
            return;
        life[numOfLife].setAnimation(fadeIn);
        life[numOfLife].setVisibility(View.VISIBLE);
        numOfLife++;

    }

    private void endGame() {
        Intent intent = new Intent(this, GameOverScreen.class);
        intent.putExtra("score",score);
        startActivity(intent);
        finish();
    }

    private boolean isCollide(View enemy) {

        int[] locationEnemy = new int[2];
        int[] locationPlayer = new int[2];

        enemy.getLocationOnScreen(locationEnemy);
        player.getLocationOnScreen(locationPlayer);

        Rect R1 = new Rect((int) locationEnemy[0], (int) locationEnemy[1] + (enemy.getHeight() / 2), (int) (locationEnemy[0] + enemy.getWidth()), (int) (locationEnemy[1] + enemy.getHeight()));
        Rect R2 = new Rect((int) (locationPlayer[0]) + (player.getWidth() / 5), (int) (locationPlayer[1]) + (player.getHeight() / 2), (int) (locationPlayer[0] + player.getWidth()) - (player.getWidth() / 3), (int) (locationPlayer[1] + player.getHeight()));

        return R1.intersect(R2);
    }

    private void addEnemies(int numOfCols) {
        enemies = new View[numOfCols];
        cols = new LinearLayout[numOfCols];
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0, LinearLayout.LayoutParams.MATCH_PARENT, 1);

        for (int i = 0; i < numOfCols; i++) {

            cols[i] = new LinearLayout(MainActivity.this);
            cols[i].setLayoutParams(lp);
            enemies[i] = new View(MainActivity.this);
            enemies[i].setLayoutParams(new LinearLayout.LayoutParams(screenWidth / numOfCols, 250));
            addGravity(enemies[i], Gravity.TOP);
            enemies[i].setVisibility(View.INVISIBLE);
            enemies[i].setBackgroundResource(enemiesPics[(int)(Math.random() * ((MAX_ENEMIES - MIN_ENEMIES) + 1))]);
            cols[i].addView(enemies[i]);
            linearLayoutsContainer.addView(cols[i]);
        }
    }

    private void addClickListeners() {

        rightScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (player.getX() > 0){
                    player.setX(player.getX() - STEP);
                    player.setBackgroundResource(R.drawable.playerright);
                    if(jellyFish != null){
                        if(isCollide(jellyFish))
                            collideWithJellyfishOccurred();
                    }
                }
            }
        });

        leftScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (player.getX() < screenWidth - player.getWidth()){
                    player.setX(player.getX() + STEP);
                    player.setBackgroundResource(R.drawable.playerleft);
                    if(jellyFish != null){
                        if(isCollide(jellyFish))
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
}