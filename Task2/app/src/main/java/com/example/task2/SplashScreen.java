package com.example.task2;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class SplashScreen extends AppCompatActivity {

    private static int SPLASH_DURATION = 5000;

    Animation logoAnim, textAnim;
    ImageView logo;
    TextView subText;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        logoAnim = AnimationUtils.loadAnimation(this, R.anim.logo_animation);
        textAnim = AnimationUtils.loadAnimation(this, R.anim.subtext_animation);

        logo = findViewById(R.id.imageView);
        subText = findViewById(R.id.subtext);

        logo.setAnimation(logoAnim);
        subText.setAnimation(textAnim);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent toStartScreen = new Intent(SplashScreen.this, StartScreen.class);
                startActivity(toStartScreen);
                finish();
            }
        }, SPLASH_DURATION);


    }
}
