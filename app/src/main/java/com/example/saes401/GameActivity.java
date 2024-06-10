package com.example.saes401;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.example.saes401.db.DataModel;
import com.example.saes401.db.DatabaseHelper;
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
    private DataModel dataModel;

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
        if (this.previousActivity.contains(GameConstant.VALUE_PLAYER_CHOISE)) {
            startActivityGameNaration();
        } else if (this.previousActivity.contains(GameConstant.VALUE_STORY)) {
            if (this.currentLevel > 3){
                startMainActivity();
            }
            else if (!noEnemieLeft(this.currentEnemieInstance)) {
                this.currentEnemieInstance++;
                this.levelStart = false;

            } else if (noEnemieLeft(this.currentEnemieInstance)) {
                this.currentLevel++;
                this.currentEnemieInstance = 0;
                this.levelStart = true;
            }
            startActivityGameNaration();
        } else if (this.previousActivity.contains(GameConstant.VALUE_GAME_CHOISE)) {
            statActivityStory();
        } else if (this.previousActivity.contains(GameConstant.VALUE_GAME_NARATION)) {
            if (this.currentLevel > 3 || !this.gameContinue) {
                setDataToDB();
                startMainActivity();
            } else startActivityGameChoise();

        }
    }

    private void setDataToDB() {
        this.dataModel.setDatabaseHelper(new DatabaseHelper(this));
        //add last data
        this.dataModel.addEnd();
        this.dataModel.addLastScore(String.format(GameConstant.FORMAT_SCORE, this.currentLevel, this.currentEnemieInstance));
        this.dataModel.addWin(this.currentLevel >= 3 && noEnemieLeft(this.currentEnemieInstance));
        //put data to db
        this.dataModel.putTime();
        this.dataModel.putHeartLost();
        this.dataModel.putLastScore();
        this.dataModel.putMaxDamageToEnemy();
        this.dataModel.putMaxDamageToPlayer();
        this.dataModel.putWin();
    }

    private boolean noEnemieLeft(int index) {
        boolean enemieLeft = false;
        try {
            enemieLeft = JsonReader.getNumberEnemies(this, String.format(GameConstant.FORMAT_LEVEL, this.currentLevel)) == index;
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
        this.dataModel = intent.getParcelableExtra(GameConstant.KEY_DATA_MODEL);
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
        this.dataModel = savedInstanceState.getParcelable(GameConstant.KEY_DATA_MODEL);
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
        outState.putParcelable(GameConstant.KEY_DATA_MODEL, this.dataModel);
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
        this.intent.putExtra(GameConstant.KEY_DATA_MODEL, this.dataModel);
    }

    @Override
    public void startActivityGame() {
        //void
    }

    @Override
    public void startActivityPlayerChoise() {
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
