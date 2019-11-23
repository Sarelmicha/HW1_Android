package com.example.hw_sarelmicha;
//Created by Mor Soferian and Sarel Micha
import android.animation.ValueAnimator;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.drawable.Drawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class MainActivity extends AppCompatActivity {

    private final int NUM_OF_COLS = 3;
    private View[] enemies;
    private LinearLayout[] cols;
    private ValueAnimator[] animations;
    private RelativeLayout mainLayout;
    private RelativeLayout leftScreen;
    private RelativeLayout rightScreen;
    private LinearLayout linearLayoutsContainer;
    private View player;
    private int screenWidth;
    private int screenHeight;
    private static int animationIndex;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setScreenHeightAndWidth();

        mainLayout = (RelativeLayout)findViewById(R.id.main_layout);
        linearLayoutsContainer = (LinearLayout)findViewById(R.id.linearLayouts_container);
        player = (View)findViewById(R.id.player);
        leftScreen = (RelativeLayout)findViewById(R.id.left_screen);
        rightScreen = (RelativeLayout)findViewById(R.id.right_screen);

        addClickListeners();
        addEnemies(NUM_OF_COLS);
        animations = new ValueAnimator[NUM_OF_COLS];

    }

    void setScreenHeightAndWidth(){

        //Get Screen size
        WindowManager wm = getWindowManager();
        Display disp = wm.getDefaultDisplay();
        Point size = new Point();
        disp.getSize(size);
        screenWidth = size.x;
        screenHeight = size.y;
    }

    @Override
    protected void onResume() {
        super.onResume();

        final float initialHeight = -100;

        for (animationIndex = 0; animationIndex < NUM_OF_COLS; animationIndex++) {

            animations[animationIndex] = ValueAnimator.ofFloat(initialHeight, screenHeight);
            animations[animationIndex].setDuration(4000);
            animations[animationIndex].setStartDelay((long) (Math.random() * (2000)));
            animations[animationIndex].setRepeatCount(Animation.INFINITE);
            animations[animationIndex].addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                private int x = animationIndex;
                @Override
                public void onAnimationUpdate(ValueAnimator updatedAnimation) {

                        float animatedValue = (float)updatedAnimation.getAnimatedValue();
                        enemies[x].setTranslationY(animatedValue);
                        enemies[x].setVisibility(View.VISIBLE);
                        if(enemies[x].getY()  > screenHeight - 10){
                            updatedAnimation.setStartDelay((long) (Math.random() * (1000)));
                            updatedAnimation.start();
                        }
                }
            });

            animations[animationIndex].start();
        }
    }

    private void addEnemies(int numOfCols) {
        enemies = new View[numOfCols];
        cols = new LinearLayout[numOfCols];
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(0,LinearLayout.LayoutParams.MATCH_PARENT, 1);

        for (int i = 0; i < numOfCols; i++) {

            cols[i] = new LinearLayout(MainActivity.this);
            cols[i].setLayoutParams(lp);
            enemies[i] = new View(MainActivity.this);
            enemies[i].setLayoutParams(new LinearLayout.LayoutParams(screenWidth/numOfCols,250));
            addGravity(enemies[i],Gravity.TOP);
            enemies[i].setVisibility(View.INVISIBLE);
            setViewBackground(enemies[i],R.drawable.plastic);
            cols[i].addView(enemies[i]);
            linearLayoutsContainer.addView(cols[i]);
        }
    }

    private void addClickListeners(){
        rightScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(player.getX() > 0)
                    player.setX(player.getX()- 50);

            }
        });

        leftScreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(player.getX() < screenWidth - player.getWidth())
                    player.setX(player.getX()+ 50);

            }
        });
    }

    private void addGravity(View view, int ...gravity){
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)view.getLayoutParams();

        for (int x: gravity) {
            params.gravity = x;
        }
        view.setLayoutParams(params); //causes layout update
    }

    private void setViewBackground(View view, int draw){
        view.setBackgroundResource(draw);
    }

}


