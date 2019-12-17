package com.example.hw_sarelmicha;

import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

public class Effects {


    public Animation fadeInEffect(){

        //Fade In effect
        Animation fadeIn = new AlphaAnimation(0, 1);
        fadeIn.setInterpolator(new AccelerateInterpolator());
        fadeIn.setStartOffset(0);
        fadeIn.setDuration(500);

        return fadeIn;
    }

    public Animation fadeOutEffect(){

        //Fade Out effect
        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setInterpolator(new AccelerateInterpolator());
        fadeOut.setStartOffset(0);
        fadeOut.setDuration(500);

        return fadeOut;
    }
}
