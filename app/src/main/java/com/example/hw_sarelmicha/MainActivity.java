package com.example.hw_sarelmicha;
//Created by Mor Soferian and Sarel Micha
import android.animation.ValueAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
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


public class MainActivity extends AppCompatActivity {

    private final int NUM_OF_COLS = 3;
    private int numOfLife = 3;
    private View[] enemies;
    private View[] life;
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
        addLife();
        animations = new ValueAnimator[NUM_OF_COLS];

    }

    private void addLife() {
        life = new View[numOfLife];

        life[0] = (View)findViewById(R.id.life0);
        life[1] = (View)findViewById(R.id.life1);
        life[2] = (View)findViewById(R.id.life2);

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
            animations[animationIndex].setDuration(8000);
            animations[animationIndex].setStartDelay((long) (Math.random() * (2000)));
            animations[animationIndex].setRepeatCount(Animation.INFINITE);
            animations[animationIndex].addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

                private int x = animationIndex;
                @Override
                public void onAnimationUpdate(ValueAnimator updatedAnimation) {

                        float animatedValue = (float)updatedAnimation.getAnimatedValue();

                        enemies[x].setTranslationY(animatedValue);
                        enemies[x].setVisibility(View.VISIBLE);
                        if(isCollide(enemies[x])){
                            updatedAnimation.setStartDelay(0);
                            updatedAnimation.start();
                            reduceLife();
                        }
                        if(enemies[x].getY()  > screenHeight - 10){ //Enemie is Out of Screen
                            updatedAnimation.setStartDelay((long) (Math.random() * (1000)));
                            updatedAnimation.start();
                        }
                }
            });

            animations[animationIndex].start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        numOfLife = 3;

        for (int i = 0; i < NUM_OF_COLS ; i++) {
            animations[i].end();
        }
    }

    private synchronized void reduceLife() {

        numOfLife--;
        life[numOfLife].setVisibility(View.INVISIBLE);

        if(numOfLife == 0)
            endGame();

    }

    private void endGame() {
        Intent intent = new Intent(this, GameOverScreen.class);
        startActivity(intent);
    }

    private boolean isCollide(View enemy) {

        int[] locationEnemy = new int[2];
        int[] locationPlayer = new int[2];

        enemy.getLocationOnScreen(locationEnemy);
        player.getLocationOnScreen(locationPlayer);

        Rect R1=new Rect((int)locationEnemy[0] , (int)locationEnemy[1] + (enemy.getHeight() / 2) , (int)(locationEnemy[0] + enemy.getWidth()), (int)(locationEnemy[1] + enemy.getHeight()));
        Rect R2=new Rect((int)(locationPlayer[0]) + (player.getWidth() / 5) , (int)(locationPlayer[1]) + (player.getHeight() / 2), (int)(locationPlayer[0] + player.getWidth()) - (player.getWidth() / 3), (int)(locationPlayer[1] + player.getHeight()));

        return R1.intersect(R2);
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


