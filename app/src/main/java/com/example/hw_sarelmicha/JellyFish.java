package com.example.hw_sarelmicha;

import android.animation.ValueAnimator;
import android.content.Context;
import android.view.View;
import android.widget.RelativeLayout;

public class JellyFish extends GameObject {

    private Effects effects;
    private ValueAnimator jellyCounter;

    public JellyFish(Context context,int screenWidth,RelativeLayout mainLayout,Effects effects) {
        super(context);
        this.effects = effects;
        addJellyfish(screenWidth,mainLayout);
    }

    public void setJellyCounter(){

        final int TIME_TO_LIVE = 10 * 1000; //10 seconds for jelly to live

        jellyCounter = new ValueAnimator();
        jellyCounter = ValueAnimator.ofFloat(0, 1); //number for 0 to 1 will be increase in 10 seconds;
        jellyCounter.setDuration(TIME_TO_LIVE);
        jellyCounter.start();
    }

    private void addJellyfish(int screenWidth, RelativeLayout mainLayout) {

        final int JELLYFISH_WIDTH = 200;
        final int JELLYFISH_HEIGHT = 200;
        final int BOTTOM_MARGIN = 30;

        RelativeLayout.LayoutParams params = new  RelativeLayout.LayoutParams(JELLYFISH_WIDTH, JELLYFISH_HEIGHT);
        params.rightMargin = (int)(Math.random() * (((screenWidth - 100) - 100) + 1));
        params.bottomMargin = BOTTOM_MARGIN;
        params.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
        setLayoutParams(params);
        setBackgroundResource(R.drawable.jellyfish);
        setAnimation(effects.fadeInEffect());
        mainLayout.addView(this);
        setJellyCounter();
    }

    public void disappeared(){
        setAnimation(effects.fadeOutEffect());
        setVisibility(View.INVISIBLE);
        jellyCounter.end();
    }

    public ValueAnimator getJellyCounter() {
        return jellyCounter;
    }
}
