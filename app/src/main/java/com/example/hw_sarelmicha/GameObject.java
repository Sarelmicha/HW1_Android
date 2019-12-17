package com.example.hw_sarelmicha;

import android.content.Context;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;

public class GameObject extends View {

    private int screenWidth;
    private int screenHeight;

    public GameObject(Context context, int screenWidth, int screenHeight) {
        super(context);
        this.screenWidth = screenWidth;
        this.screenHeight = screenHeight;

    }

    public boolean isOutOfWidthScreen(View view){
        if(view.getX() >= screenWidth - 1)
            return true;
        return false;
    }

    public boolean isOutOfHeightScreen(View view){
        if(view.getY() >= screenHeight - 20)
            return true;
        return false;
    }

    public boolean isCollide(GameObject gameObject) {

        int[] locationFirstGameObject = new int[2];
        int[] locationSecondGameObject = new int[2];

        gameObject.getLocationOnScreen(locationFirstGameObject);
        this.getLocationOnScreen(locationSecondGameObject);

        Rect R1 = new Rect((int) locationFirstGameObject[0], (int) locationFirstGameObject[1] + (gameObject.getHeight() / 2), (int) (locationFirstGameObject[0] + gameObject.getWidth()), (int) (locationFirstGameObject[1] + gameObject.getHeight()));
        Rect R2 = new Rect((int) (locationSecondGameObject[0]) + (this.getWidth() / 5), (int) (locationSecondGameObject[1]) + (this.getHeight() / 2), (int) (locationSecondGameObject[0] + this.getWidth()) - (this.getWidth() / 3), (int) (locationSecondGameObject[1] + this.getHeight()));

        return R1.intersect(R2);
    }

    public boolean isOutOfWidthScreen(){
        if(this.getX() >= screenWidth - 1)
            return true;
        return false;
    }

    public boolean isOutOfHeightScreen(){
        if(this.getY() >= screenHeight - 20)
            return true;
        return false;
    }

    public int getScreenWidth() {
        return screenWidth;
    }

    public int getScreenHeight() {
        return screenHeight;
    }

    public void setScreenWidth(int screenWidth) {
        this.screenWidth = screenWidth;
    }

    public void setScreenHeight(int screenHeight) {
        this.screenHeight = screenHeight;
    }
}
