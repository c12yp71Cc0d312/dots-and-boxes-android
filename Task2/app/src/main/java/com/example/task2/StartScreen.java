package com.example.task2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class StartScreen extends AppCompatActivity {

    Button five;
    Button six;
    Button seven;
    SharedPreferences sharedPreferences;
    Intent switchToGame;



    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_start);

        five = findViewById(R.id.button);
        six = findViewById(R.id.button2);
        seven = findViewById(R.id.button3);
        sharedPreferences = getSharedPreferences("MySharedPrefs", MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPreferences.edit();

        switchToGame = new Intent(StartScreen.this, MainActivity.class);

        five.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToGame.putExtra("Mode",4);
                startActivity(switchToGame);
            }
        });

        six.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToGame.putExtra("Mode",5);
                startActivity(switchToGame);
            }
        });

        seven.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switchToGame.putExtra("Mode",6);
                startActivity(switchToGame);
            }
        });

    }
}
