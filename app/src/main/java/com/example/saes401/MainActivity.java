package com.example.saes401;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
        findViewById(R.id.previousButton).setOnClickListener(view -> onClickPrevious());
        findViewById(R.id.continueButton).setOnClickListener(view -> onClickContinue());
        findViewById(R.id.parametreButton).setOnClickListener(view -> onClickSettings());


    }


    private void onClickCredits() {
        //affiche les credits de l'app
        intent = new Intent(this, CreditsActivity.class);
        startActivity(intent);
    }

    private void onClickStart(){

        intent = new Intent(this, GameActivity.class);
        startActivity(intent);
    }
    private void onClickPrevious(){
        intent = new Intent(this, DialogueStory.class);
        startActivity(intent);
    }
    private void onClickContinue(){

    }
    private void onClickSettings(){
        intent = new Intent(this, ParametreActivity.class);
        startActivity(intent);

    }

}