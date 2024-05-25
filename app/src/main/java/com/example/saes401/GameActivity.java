package com.example.saes401;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.saes401.entities.Enemie;
import com.example.saes401.entities.Player;
import com.example.saes401.helper.GameConstant;
import com.example.saes401.helper.Utilities;
import com.example.saes401.story.Story;
import com.example.saes401.utilities.Inventory;

import java.io.Serializable;

public class GameActivity extends AppCompatActivity implements Utilities {
    private Intent intent;
    private Player playerInstance;
    private int currentLevel;
    private int currentEnemie;
    private String previousActivity;
    private Enemie currentEnemieInstance;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        intent = getIntent();
        if (intent != null){
            this.initAttibuts();
        }
        if (bundle == null && intent==null){
            this.playerInstance = new Player(GameConstant.DEFAULT_HP);
            this.currentEnemieInstance = new Enemie(0, "", 0, 0, new Inventory(0), "");
            this.currentLevel = intent.getIntExtra(GameConstant.KEY_LEVEL, 0);
            this.currentEnemie = 0;
            this.previousActivity = "";
        }
    }
    @Override
    public void initAttibuts() {
        if (intent.getStringExtra(GameConstant.KEY_PREVIOUS_ACTIVITY).contains(GameConstant.VALUE_GAME_CHOISE))
        {
            this.playerInstance = intent.getParcelableExtra(GameConstant.KEY_PLAYER);
        }
        if (!intent.getStringExtra(GameConstant.KEY_PREVIOUS_ACTIVITY).contains(GameConstant.VALUE_GAME_NARATION))
        {
            this.currentEnemie = intent.getIntExtra(GameConstant.KEY_ENEMIE_INDEX, 0);
        }
        if(intent.getStringExtra(GameConstant.KEY_PREVIOUS_ACTIVITY).contains(GameConstant.VALUE_STORY))
        {
            this.currentEnemieInstance = intent.getParcelableExtra(GameConstant.KEY_PLAYER);
            this.currentEnemieInstance = intent.getParcelableExtra(GameConstant.KEY_ENEMIE_INSTANCE);
        }
        this.currentLevel = intent.getIntExtra(GameConstant.KEY_LEVEL, 0);
        this.previousActivity = intent.getStringExtra(GameConstant.KEY_PREVIOUS_ACTIVITY);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        currentLevel = savedInstanceState.getInt(GameConstant.KEY_LEVEL);
        playerInstance = (Player) savedInstanceState.getSerializable(GameConstant.KEY_PLAYER);
        currentEnemie = savedInstanceState.getInt(GameConstant.KEY_ENEMIE_INDEX);
        previousActivity = savedInstanceState.getString(GameConstant.KEY_PREVIOUS_ACTIVITY);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState){
        super.onSaveInstanceState(outState);
        outState.putInt(GameConstant.KEY_LEVEL, this.currentLevel);
        outState.putSerializable(GameConstant.KEY_PLAYER, (Serializable) this.playerInstance);
        outState.putInt(GameConstant.KEY_ENEMIE_INDEX, this.currentEnemie);
        outState.putString(GameConstant.KEY_PREVIOUS_ACTIVITY, this.previousActivity);
    }

    @Override
    public void startActivityGameChoise() {
        this.intent = new Intent(this, GameChoise.class);
        this.intent.putExtra(GameConstant.KEY_LEVEL, this.currentLevel);
        this.intent.putExtra(GameConstant.KEY_PLAYER, this.playerInstance);
        startActivity(this.intent);
    }

    @Override
    public void startActivityGameNaration() {
        this.intent = new Intent(this, GameNaration.class);
        this.intent.putExtra(GameConstant.KEY_LEVEL, this.currentLevel);
        startActivity(this.intent);
    }

    @Override
    public void statActivityStory() {
        this.intent = new Intent(this, Story.class);
        this.intent.putExtra(GameConstant.KEY_PLAYER, this.playerInstance);
        this.intent.putExtra(GameConstant.KEY_LEVEL, this.currentLevel);
        this.intent.putExtra(GameConstant.KEY_ENEMIE_INDEX, this.currentEnemie);
        startActivity(this.intent);
    }

    @Override
    public void startActivityGame() {
        //void
    }

    @Override
    public void setListener() {

    }

}
