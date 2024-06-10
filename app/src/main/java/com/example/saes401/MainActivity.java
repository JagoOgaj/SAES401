package com.example.saes401;

import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.saes401.db.DataModel;
import com.example.saes401.entities.Player;
import com.example.saes401.helper.GameConstant;
import com.example.saes401.helper.Settings;
import com.example.saes401.helper.Utilities;


public class MainActivity extends AppCompatActivity implements Utilities {
    private Intent intent;
    String selectedLanguage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        loadParametre();
        setContentView(R.layout.activity_main);
        this.setListener();
    }


    private void onClickCredits() {
        intent = new Intent(this, CreditsActivity.class);
        startActivity(intent);
    }

    private void onClickStart() {
        startActivityPlayerChoise();
    }

    private void onClickContinue() {
    }

    private void onClickSettings() {startParametre();}

    private void onClickStat() {startStat();}



    @Override
    public void initAttibuts() {
        //void
    }

    @Override
    public void startActivityGameChoise() {
        //void
    }

    @Override
    public void startActivityGameNaration() {
        //void
    }

    @Override
    public void statActivityStory() {
        //void
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