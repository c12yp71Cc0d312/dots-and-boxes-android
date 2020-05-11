package com.example.task2;

import androidx.appcompat.app.AppCompatActivity;

import android.media.AudioAttributes;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.SoundPool;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    
    GameScreen gameScreenLayout;
    public static int mode;
    public static int players;
    MediaPlayer mediaPlayer;
   /* protected SoundPool soundPool;
    protected int tapSound; */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mode = getIntent().getIntExtra("Mode",5);
        players = getIntent().getIntExtra("Players", 2);
        gameScreenLayout = new GameScreen(getApplicationContext(),this);
        if(mediaPlayer == null)
            mediaPlayer = MediaPlayer.create(this, R.raw.bgm);
        mediaPlayer.start();
        mediaPlayer.setLooping(true);
      /*  if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            AudioAttributes audioAttributes = new AudioAttributes.Builder()
                    .setUsage(AudioAttributes.USAGE_ASSISTANCE_SONIFICATION)
                    .setContentType(AudioAttributes.CONTENT_TYPE_SONIFICATION)
                    .build();

            soundPool = new SoundPool.Builder()
                    .setMaxStreams(4)
                    .setAudioAttributes(audioAttributes)
                    .build();
            Log.d(TAG, "onCreate: sp instance >= 21");
        }
        else {
            soundPool = new SoundPool(4, AudioManager.STREAM_MUSIC, 0);
            Log.d(TAG, "onCreate: sp instance");
        }
        tapSound = soundPool.load(this, R.raw.tap, 1); */
        setContentView(gameScreenLayout);


    }

    public int getMode() {
        Log.d(TAG, "getMode: " + mode);
        return mode;
    }

    public int getPlayers() {
        return players;
    }

    @Override
    protected void onStop() {
        super.onStop();
        mediaPlayer.release();
        mediaPlayer = null;
        //Toast.makeText(this, "stopped", Toast.LENGTH_SHORT).show();
    }

  /*  public SoundPool getSoundPool() {
        return soundPool;
    }

    public int getTapSound() {
        return tapSound;
    } */
}
