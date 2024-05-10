package com.example.saes401.story;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.saes401.GameActivity;
import com.example.saes401.GameNaration;
import com.example.saes401.entities.Player;
import com.example.saes401.helper.GameConstant;

public class Story extends AppCompatActivity {
    private Intent intent;
    private String curentLevel;
    private GameActivity gameActivity;
    private Player player;

    public Story(GameActivity gameActivity) {
        this.player = new Player(GameConstant.PLAYER_NAME, GameConstant.DEFAULT_HP, GameConstant.DEFAULT_MAX_DAMAGE, GameConstant.DEFAULT_COIN);
        this.gameActivity = gameActivity;
        this.curentLevel = this.gameActivity.getLevel();
        launchStory();
    }

    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        intent = getIntent();
        curentLevel = intent.getStringExtra("curentLevel");
    }

    private void level(){
        while(true){

        }
    }

    private void launchStory() {
        Intent intent = new Intent(this, GameNaration.class);
        intent.putExtra("level", curentLevel);
        startActivity(intent);
    }
}
