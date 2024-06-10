package com.example.saes401;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.saes401.db.DataModel;
import com.example.saes401.entities.Player;
import com.example.saes401.helper.GameConstant;
import com.example.saes401.helper.Settings;
import com.example.saes401.helper.Utilities;
import com.example.saes401.soud.GameSound;

public class MainActivity extends AppCompatActivity implements Utilities {
    private Intent intent;
    private static MediaPlayer homeScreenMediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        loadParametre();
        setContentView(R.layout.activity_main);
        this.setListener();
        if (homeScreenMediaPlayer == null) {
            homeScreenMediaPlayer = GameSound.homeScreenSound(this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (homeScreenMediaPlayer != null && !homeScreenMediaPlayer.isPlaying()) {
            homeScreenMediaPlayer.start();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Ne pas arrêter le son ici pour qu'il continue de jouer en arrière-plan
    }

    private void stopHomeScreenSound() {
        if (homeScreenMediaPlayer != null) {
            GameSound.stopHomeScreenSound(homeScreenMediaPlayer);
            homeScreenMediaPlayer = null;
        }
    }

    private void onClickCredits() {
        GameSound.playClickSound(this);
        stopHomeScreenSound();
        intent = new Intent(this, CreditsActivity.class);
        startActivity(intent);
    }

    private void onClickStart() {
        GameSound.playClickSound(this);
        stopHomeScreenSound();
        startActivityPlayerChoise();
    }

    private void onClickContinue() {
        GameSound.playClickSound(this);
        stopHomeScreenSound();
        // Implementation for continue action
    }

    private void onClickSettings() {
        GameSound.playClickSound(this);
        // Ne pas arrêter le son ici pour qu'il continue de jouer en arrière-plan
        startParametre();
    }

    private void onClickStat() {
        startStat();
    }

    @Override
    public void initAttibuts() {
        //void
    }

    @Override
    public void startActivityGameChoise() {
        // Not used
    }

    @Override
    public void startActivityGameNaration() {
        // Not used
    }

    @Override
    public void statActivityStory() {
        // Not used
    }

    @Override
    public void startActivityGame() {
        //void
    }

    @Override
    public void startActivityPlayerChoise() {
        intent = new Intent(this, PlayerChoise.class);
        startActivity(intent);
    }

    public void startParametre() {
        intent = new Intent(this, ParametreActivity.class);
        startActivity(intent);
    }

    public void startStat() {
        intent = new Intent(this, statActivity.class);
        startActivity(intent);

    }

    @Override
    public void setListener() {
        findViewById(R.id.creditsButton).setOnClickListener(view -> onClickCredits());
        findViewById(R.id.startButton).setOnClickListener(view -> onClickStart());
        findViewById(R.id.continueButton).setOnClickListener(view -> onClickContinue());
        findViewById(R.id.parametreButton).setOnClickListener(view -> onClickSettings());
        findViewById(R.id.statButton).setOnClickListener(view -> onClickStat());

    }

    private void loadParametre() {
        // Charger la langue
        String language = Settings.loadLanguage(this);
        Settings.changeLanguage(MainActivity.this, language);

        // Charger le volume et l'appliquer
        int volume = Settings.loadVolume(this);
        AudioManager audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        audioManager.setStreamVolume(AudioManager.STREAM_MUSIC, volume, 0);
    }
}
