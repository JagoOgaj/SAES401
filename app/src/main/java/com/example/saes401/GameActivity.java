package com.example.saes401;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.saes401.entities.Player;
import com.example.saes401.helper.GameConstant;
import com.example.saes401.helper.JsonReader;
import com.example.saes401.helper.Utilities;
import com.example.saes401.story.Story;

public class GameActivity extends AppCompatActivity implements Utilities {
    private Intent intent;
    private Player playerInstance;
    private int currentLevel;
    private int currentEnemieInstance;
    private String previousActivity;
    private Boolean gameContinue;
    private Boolean levelStart;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        intent = getIntent();
        if (intent != null) {
            this.initAttibuts();
            this.initStartActivity();
        }
    }

    private void initStartActivity() {
        // Nartion -> GameChoise -> Story
        if (this.previousActivity.contains(GameConstant.VALUE_MAIN_ACTIVITY)) {
            startActivityGameNaration();
        } else if (this.previousActivity.contains(GameConstant.VALUE_STORY)) {
            if (!this.gameContinue) {
                this.currentEnemieInstance = -1;
                startActivityGameNaration();
            } else {
                if (enemieLeft() && !(this.currentLevel < 3)) {
                    this.currentEnemieInstance++;
                    this.levelStart = false;

                } else if (!enemieLeft() && this.currentLevel < 3) {
                    this.currentLevel++;
                    this.levelStart = true;
                }
                startActivityGameNaration();
            }
        } else if (this.previousActivity.contains(GameConstant.VALUE_GAME_CHOISE)) {
            statActivityStory();
        } else if (this.previousActivity.contains(GameConstant.VALUE_GAME_NARATION)) {
            if (this.currentLevel > 3 || !this.gameContinue) {
                //todo sauvgarder la partie dans la base de données et aller dans main activity
                startMainActivity();
            } else startActivityGameChoise();

        }
    }

    private boolean enemieLeft() {
        boolean enemieLeft = false;
        try {
            enemieLeft = JsonReader.getNumberEnemies(this, String.format(GameConstant.FORMAT_LEVEL, this.currentLevel)) > 0;
        } catch (Exception e) {
            Log.d("GameActivity", "enemieLeftError: " + e.getMessage());
        }
        return enemieLeft;
    }

    @Override
    public void initAttibuts() {
        this.currentEnemieInstance = intent.getIntExtra(GameConstant.KEY_ENEMIE_INDEX, 0);
        this.gameContinue = intent.getBooleanExtra(GameConstant.KEY_PLAYER_WIN, false);
        this.levelStart = intent.getBooleanExtra(GameConstant.KEY_START_LEVEL, false);
        this.playerInstance = intent.getParcelableExtra(GameConstant.KEY_PLAYER);
        this.currentLevel = intent.getIntExtra(GameConstant.KEY_LEVEL, 0);
        this.previousActivity = intent.getStringExtra(GameConstant.KEY_PREVIOUS_ACTIVITY);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        currentLevel = savedInstanceState.getInt(GameConstant.KEY_LEVEL);
        playerInstance = savedInstanceState.getParcelable(GameConstant.KEY_PLAYER);
        currentEnemieInstance = savedInstanceState.getInt(GameConstant.KEY_ENEMIE_INDEX);
        previousActivity = savedInstanceState.getString(GameConstant.KEY_PREVIOUS_ACTIVITY);
        gameContinue = savedInstanceState.getBoolean(GameConstant.KEY_PLAYER_WIN);
        levelStart = savedInstanceState.getBoolean(GameConstant.KEY_START_LEVEL);
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(GameConstant.KEY_LEVEL, this.currentLevel);
        outState.putParcelable(GameConstant.KEY_PLAYER, this.playerInstance);
        outState.putInt(GameConstant.KEY_ENEMIE_INDEX, this.currentEnemieInstance);
        outState.putString(GameConstant.KEY_PREVIOUS_ACTIVITY, this.previousActivity);
        outState.putBoolean(GameConstant.KEY_PLAYER_WIN, this.gameContinue);
        outState.putBoolean(GameConstant.KEY_START_LEVEL, this.levelStart);
    }

    @Override
    public void startActivityGameChoise() {
        this.intent = new Intent(this, GameChoise.class);
        putExtra();
        startActivity(this.intent);
    }

    @Override
    public void startActivityGameNaration() {
        this.intent = new Intent(this, GameNaration.class);
        putExtra();
        startActivity(this.intent);
    }

    @Override
    public void statActivityStory() {
        this.intent = new Intent(this, Story.class);
        putExtra();
        startActivity(this.intent);
    }

    private void putExtra(){
        this.intent.putExtra(GameConstant.KEY_PLAYER, this.playerInstance);
        this.intent.putExtra(GameConstant.KEY_LEVEL, this.currentLevel);
        this.intent.putExtra(GameConstant.KEY_ENEMIE_INDEX, this.currentEnemieInstance);
        this.intent.putExtra(GameConstant.KEY_START_LEVEL, this.levelStart);
        this.intent.putExtra(GameConstant.KEY_PLAYER_WIN, this.gameContinue);
    }

    @Override
    public void startActivityGame() {
        //void
    }

    @Override
    public void setListener() {

    }

    private void startMainActivity() {
        intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
