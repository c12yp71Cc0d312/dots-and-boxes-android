package com.example.task2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class StartScreen extends AppCompatActivity {

    RadioGroup gridSize;
    RadioButton fivexfive, sixxsix, fourxfour;
    RadioGroup noOfPlayers;
    RadioButton two, three, four;
    Button play;

    Intent switchToGame;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.screen_start);

        gridSize = findViewById(R.id.size);
        fourxfour = findViewById(R.id.fourxfour);
        fivexfive = findViewById(R.id.fivexfive);
        sixxsix = findViewById(R.id.sixxsix);

        noOfPlayers = findViewById(R.id.players);
        two = findViewById(R.id.two);
        three = findViewById(R.id.three);
        four = findViewById(R.id.four);

        play = findViewById(R.id.button);

        switchToGame = new Intent(StartScreen.this, MainActivity.class);

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(gridSize.getCheckedRadioButtonId() == -1 || noOfPlayers.getCheckedRadioButtonId() == -1) {
                    Toast.makeText(StartScreen.this, "Choose all options!", Toast.LENGTH_SHORT).show();
                }
                else {
                    checkMode();
                    checkPlayers();
                    startActivity(switchToGame);
                }
            }
        });


    }

    public void checkMode() {
       if(fivexfive.isChecked())
           switchToGame.putExtra("Mode", 5);
       else if(fourxfour.isChecked())
           switchToGame.putExtra("Mode", 4);
       else
           switchToGame.putExtra("Mode", 6);
    }

    public void checkPlayers() {
        if(two.isChecked())
            switchToGame.putExtra("Players", 2);
        else if(three.isChecked())
            switchToGame.putExtra("Players", 3);
        else
            switchToGame.putExtra("Players", 4);
    }
}
