package com.example.hw_sarelmicha;

import android.content.Context;
import android.graphics.Rect;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

public class GameObject extends View {

    public GameObject(Context context) {
        super(context);
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

    public boolean isOutOfWidthScreen(int screenWidth) {
        if (this.getX() >= screenWidth - 1)
            return true;
        return false;
    }

    public boolean isOutOfHeightScreen(int screenHeight) {
        if (this.getY() >= screenHeight - 1)
            return true;
        return false;
    }

    public void setWidthAndHeight(int width, int height) {

        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width, height);
        this.setLayoutParams(params);
    }
}
