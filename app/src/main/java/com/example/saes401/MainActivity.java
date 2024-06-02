package com.example.saes401;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.saes401.entities.Player;
import com.example.saes401.helper.GameConstant;
import com.example.saes401.helper.Utilities;


public class MainActivity extends AppCompatActivity implements Utilities {
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        this.setListener();
    }


    private void onClickCredits() {
        intent = new Intent(this, CreditsActivity.class);
        startActivity(intent);
    }

    private void onClickStart() {
        startActivityGame();
    }

    private void onClickContinue() {
    }

    private void onClickSettings() {
        intent = new Intent(this, ParametreActivity.class);
        startActivity(intent);

    }


    @Override
    public void initAttibuts() {

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
        intent = new Intent(this, GameActivity.class);
        intent.putExtra(GameConstant.KEY_LEVEL, 1);
        intent.putExtra(GameConstant.KEY_PREVIOUS_ACTIVITY, GameConstant.VALUE_MAIN_ACTIVITY);
        intent.putExtra(GameConstant.KEY_PLAYER, new Player(GameConstant.DEFAULT_HP));
        intent.putExtra(GameConstant.KEY_START_LEVEL, true);
        intent.putExtra(GameConstant.KEY_PLAYER_WIN, true);
        startActivity(intent);
    }

    @Override
    public void setListener() {
        findViewById(R.id.creditsButton).setOnClickListener(view -> onClickCredits());
        findViewById(R.id.startButton).setOnClickListener(view -> onClickStart());
        findViewById(R.id.continueButton).setOnClickListener(view -> onClickContinue());
        findViewById(R.id.parametreButton).setOnClickListener(view -> onClickSettings());
    }
}