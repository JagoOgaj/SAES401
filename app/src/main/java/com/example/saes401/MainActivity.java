package com.example.saes401;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        findViewById(R.id.creditsButton).setOnClickListener(view -> onClickCredits());
        findViewById(R.id.startButton).setOnClickListener(view -> onClickStart());
        findViewById(R.id.previousGame).setOnClickListener(view -> onClickPrevious());
        findViewById(R.id.continueButton).setOnClickListener(view -> onClickContinue());

    }

    private void onClickCredits() {
        //affiche les credits de l'app
        intent = new Intent(this, CreditsActivity.class);
        startActivity(intent);
    }

    private void onClickStart() {
        //lance une nouvelle aventure
        intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }

    private void onClickPrevious() {
        //affiche les ancienne partie avec stats
    }

    private void onClickContinue() {
        //reprend l'ancienne partie pas terminer
    }
}