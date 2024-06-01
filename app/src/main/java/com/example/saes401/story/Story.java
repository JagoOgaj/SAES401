package com.example.saes401.story;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.saes401.GameActivity;
import com.example.saes401.R;
import com.example.saes401.entities.Enemie;
import com.example.saes401.entities.Player;
import com.example.saes401.helper.GameConstant;
import com.example.saes401.helper.JsonReader;
import com.example.saes401.helper.Utilities;
import com.example.saes401.utilities.GameFight;
import com.example.saes401.utilities.Inventory;
import com.example.saes401.utilities.Item;

import java.util.Random;

public class Story extends AppCompatActivity implements Utilities, Runnable {
    private Intent intent;
    private int currentLevel;
    private Player playerInstance;
    private Enemie currentEnemieInstance;
    private int currentEnemieIndex;
    private GameFight fightInstance;
    private boolean gameContinue;
    private boolean levelStart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intent = getIntent();
        if (intent != null) {
            this.initAttibuts();
        }
        this.startStory();
        setContentView(R.layout.gameplay);
    }

    @Override
    public void initAttibuts() {
        this.playerInstance = intent.getParcelableExtra(GameConstant.KEY_PLAYER);
        this.currentLevel = intent.getIntExtra(GameConstant.KEY_LEVEL, 0);
        this.currentEnemieIndex = intent.getIntExtra(GameConstant.KEY_ENEMIE_INDEX, 0);
        this.levelStart = intent.getBooleanExtra(GameConstant.KEY_START_LEVEL, false);
        this.gameContinue = intent.getBooleanExtra(GameConstant.KEY_PLAYER_WIN, false);
        try {
            initEnemie();
            addItemOfEnemie();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initEnemie() throws Exception {
        this.currentEnemieInstance = new Enemie(
                JsonReader.getEnemieHP(this, String.format(GameConstant.FORMAT_LEVEL, this.currentLevel), this.currentEnemieIndex),
                JsonReader.getEnemieName(this, String.format(GameConstant.FORMAT_LEVEL, this.currentLevel), this.currentEnemieIndex),
                this.currentLevel,
                this.currentEnemieIndex,
                new Inventory(JsonReader.getItemsOfEnemie(this, String.format(GameConstant.FORMAT_LEVEL, this.currentLevel), this.currentEnemieIndex).length),
                JsonReader.getEnemieDamageStringFormat(this, String.format(GameConstant.FORMAT_LEVEL, this.currentLevel), this.currentEnemieIndex)
        );
    }

    private void addItemOfEnemie() throws Exception {
        String[] items = JsonReader.getItemsOfEnemie(this, String.format(GameConstant.FORMAT_LEVEL, this.currentLevel), this.currentEnemieIndex);
        for (String item : items) {
            this.currentEnemieInstance.getInventory().addItemsEnemie(new Item(
                    JsonReader.getObjectName(this, item),
                    JsonReader.getObjectDamage(this, item),
                    JsonReader.getImageObject(this, item),
                    JsonReader.getObjectSize(this, item)
            ));
        }
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
        this.intent = new Intent(this, GameActivity.class);
        this.intent.putExtra(GameConstant.KEY_LEVEL, this.currentLevel);
        this.intent.putExtra(GameConstant.KEY_ENEMIE_INDEX, this.currentEnemieIndex);
        this.intent.putExtra(GameConstant.KEY_PLAYER, this.playerInstance);
        this.intent.putExtra(GameConstant.KEY_PREVIOUS_ACTIVITY, GameConstant.VALUE_STORY);
        this.intent.putExtra(GameConstant.KEY_PLAYER_WIN, this.gameContinue);
        this.intent.putExtra(GameConstant.KEY_START_LEVEL, this.levelStart);
        startActivity(intent);
    }

    @Override
    public void setListener() {

    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        currentLevel = savedInstanceState.getInt(GameConstant.KEY_LEVEL);
        playerInstance = savedInstanceState.getParcelable(GameConstant.KEY_PLAYER);
        currentEnemieIndex = savedInstanceState.getInt(GameConstant.KEY_ENEMIE_INDEX);
        currentEnemieInstance = (Enemie) savedInstanceState.getSerializable(GameConstant.KEY_ENEMIE_INSTANCE);
        gameContinue = savedInstanceState.getBoolean(GameConstant.KEY_PLAYER_WIN);
        levelStart = savedInstanceState.getBoolean(GameConstant.KEY_START_LEVEL);
        startStory();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(GameConstant.KEY_LEVEL, this.currentLevel);
        outState.putInt(GameConstant.KEY_ENEMIE_INDEX, this.currentEnemieIndex);
        outState.putParcelable(GameConstant.KEY_PLAYER, this.playerInstance);
        outState.putParcelable(GameConstant.KEY_ENEMIE_INSTANCE, this.currentEnemieInstance);
        outState.putBoolean(GameConstant.KEY_PLAYER_WIN, this.gameContinue);
        outState.putBoolean(GameConstant.KEY_START_LEVEL, this.levelStart);
    }

    private void startStory() {
        Thread thread = new Thread(this);
        thread.start();
    }

    private int getResultPlayer() {
        int resultPlayer = 0;
        int[] dices = fightInstance.getDicePlayer();
        int result = 0;
        for (int i = 0; i < dices.length; i++) {
            result += dices[i];
        }
        try {
            resultPlayer = fightInstance.getResultPlayer(result);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resultPlayer;
    }

    private int getResultEnemie() {
        int result = 0;
        try {
            result = fightInstance.getResultEnemie(fightInstance.getDiceEnemie());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public void run() {
        this.fightInstance = new GameFight(playerInstance, currentEnemieInstance, this);
        while (playerInstance.getHP() == 0 || currentEnemieInstance.getHP() == 0) {
            //todo lancer l'animation des dés du joueur
            //todo lancer l'animation des dés de l'enemie
            if (getResultEnemie() > getResultPlayer()) {
                playerInstance.setHP(playerInstance.getHP() - 1);
                //todo modifier l'affichage du coeur
            } else if (getResultPlayer() > getResultEnemie()) {
                currentEnemieInstance.setHP(currentEnemieInstance.getHP() - 1);
                //todo modifier l'affichage du coeur
            } else {
                Random random = new Random();
                if (random.nextInt(2) == 1) {
                    //enemie perdu
                    currentEnemieInstance.setHP(currentEnemieInstance.getHP() - 1);
                } else {
                    //player perdu
                    playerInstance.setHP(playerInstance.getHP() - 1);
                }
                //todo modifier l'affichage du coeur
            }
        }
        this.gameContinue = playerInstance.getHP() == 0;
        startActivityGame();
    }


    //Get
    private ImageView getEnemieImageView() {
        return findViewById(R.id.enemieImage);
    }


    private ImageView getPlayerImageView() {
        return findViewById(R.id.playerImage);
    }

    private LinearLayout getViewHeartContainerEnemie() {
        return findViewById(R.id.heartContainer2);
    }

    private LinearLayout geViewtHeartContainerPlayer() {
        return findViewById(R.id.heartContainer1);
    }

    private LinearLayout getViewLootContainerEnemie() {
        return findViewById(R.id.lootContainer2);
    }

    private LinearLayout geViewtLootContainerPLayer() {
        return findViewById(R.id.lootContainer1);
    }

    private LinearLayout getViewGameplay() {
        return findViewById(R.id.gameplay);
    }

    private TextView getTextViewGamePLay() {
        return findViewById(R.id.resultText);
    }

    public final LinearLayout getViewChoiseLoot() {
        return findViewById(R.id.choiseLoot);
    }

    private LinearLayout getViewAtoutEnemie() {
        return findViewById(R.id.atoutContainer2);
    }

    private LinearLayout getViewAtoutPlayer() {
        return findViewById(R.id.atoutContainer1);
    }

}
