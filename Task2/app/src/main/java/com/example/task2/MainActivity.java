package com.example.task2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    
    GameScreen gameScreenLayout;
    public static int mode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mode = getIntent().getIntExtra("Mode",5);
        gameScreenLayout = new GameScreen(this);
        setContentView(gameScreenLayout);

    }

    public int getMode() {
        Log.d(TAG, "getMode: " + mode);
        return mode;
    }
}
