package com.gs.moneybook;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.view.Display;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.LinearInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class WellComeScreen extends AppCompatActivity {

    private ImageView triangle1, triangle2, triangle3;
    private int screenWidth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_well_come_screen);

        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        screenWidth = display.getWidth();

        // Initialize triangle image views based on the updated layout IDs
        triangle1 = findViewById(R.id.triangle_1);
        triangle2 = findViewById(R.id.triangle_2);
        triangle3 = findViewById(R.id.triangle_3);

        // Start the triangle animations
        animateTriangles(triangle1, 0);
        animateTriangles(triangle2, 50);
        animateTriangles(triangle3, 100);
    }

    private void animateTriangles(ImageView triangle, long delay) {
        // Translation Animation (Move from left to right infinitely)
        ObjectAnimator translationX = ObjectAnimator.ofFloat(triangle, "translationX", 0f, screenWidth);
        translationX.setDuration(1500);
        translationX.setRepeatCount(ObjectAnimator.INFINITE);
        translationX.setRepeatMode(ObjectAnimator.RESTART);
        translationX.setStartDelay(delay);
        //translationX.setInterpolator(new LinearInterpolator());

        // Scale Animation (Change size while moving)
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(triangle, "scaleX", 1f, 1.5f, 1f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(triangle, "scaleY", 1f, 1.5f, 1f);
        scaleX.setDuration(1800);
        scaleY.setDuration(1800);
        scaleX.setRepeatCount(ObjectAnimator.INFINITE);
        scaleY.setRepeatCount(ObjectAnimator.INFINITE);
        scaleX.setRepeatMode(ObjectAnimator.REVERSE);
        scaleY.setRepeatMode(ObjectAnimator.REVERSE);

        // Combine both animations (translation and scale) into an AnimatorSet
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(translationX, scaleX, scaleY);
       // animatorSet.setStartDelay(delay);
        animatorSet.start();
    }
}