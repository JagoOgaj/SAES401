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
import com.example.saes401.helper.Utilities;
import com.example.saes401.utilities.GameFight;
import com.example.saes401.utilities.Inventory;

import java.io.Serializable;
import java.util.Random;

public class Story extends AppCompatActivity implements Utilities, Runnable {
    private Intent intent;
    private int currentLevel;
    private Player playerInstance;
    private Enemie currentEnemieInstance;
    private int currentEnemie;
    private GameFight fight;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intent = getIntent();
        if(intent != null){
            this.initAttibuts();
        }
        if (savedInstanceState == null && intent == null) {
            currentLevel = 0;
            playerInstance = new Player(GameConstant.DEFAULT_HP);
            currentEnemieInstance = new Enemie(0, "", 0, 0, new Inventory(0), "");
            currentEnemie = 0;
        }
        this.startStory();
        setContentView(R.layout.gameplay);
    }

    @Override
    public void initAttibuts() {
        this.playerInstance = intent.getParcelableExtra(GameConstant.KEY_PLAYER);
        this.currentLevel = intent.getIntExtra(GameConstant.KEY_LEVEL, 0);
        this.currentEnemie = intent.getIntExtra(GameConstant.KEY_ENEMIE_INDEX, 0);
        this.currentEnemieInstance = intent.getParcelableExtra(GameConstant.KEY_ENEMIE_INSTANCE);
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
        this.intent.putExtra(GameConstant.KEY_ENEMIE_INDEX, this.currentEnemie);
        this.intent.putExtra(GameConstant.KEY_PLAYER, this.playerInstance);
        this.intent.putExtra(GameConstant.KEY_ENEMIE_INSTANCE, this.currentEnemieInstance);
        this.intent.putExtra(GameConstant.KEY_PREVIOUS_ACTIVITY, GameConstant.VALUE_STORY);
        startActivity(intent);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        currentLevel = savedInstanceState.getInt(GameConstant.KEY_LEVEL);
        playerInstance = (Player) savedInstanceState.getSerializable(GameConstant.KEY_PLAYER);
        currentEnemie = savedInstanceState.getInt(GameConstant.KEY_ENEMIE_INDEX);
        currentEnemieInstance = (Enemie) savedInstanceState.getSerializable(GameConstant.KEY_ENEMIE_INSTANCE);
        startStory();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(GameConstant.KEY_LEVEL, this.currentLevel);
        outState.putInt(GameConstant.KEY_ENEMIE_INDEX, this.currentEnemie);
        outState.putSerializable(GameConstant.KEY_PLAYER, (Serializable) this.playerInstance);
        outState.putSerializable(GameConstant.KEY_ENEMIE_INSTANCE, (Serializable) this.currentEnemieInstance);
    }

    private void startStory(){
        Thread thread = new Thread(this);
        thread.start();
    }

    private int getResultPlayer(){
        int[] dices = fight.getDicePlayer();
        int result = 0;
        for (int i = 0; i < dices.length; i++){
            result += dices[i];
        }
        return result + fight.getBonusPlayer();
    }

    private int getResultEnemie(){
        int[] dices = fight.getDiceEnemie();
        int result = 0;
        for (int i = 0; i < dices.length; i++){
            result += dices[i];
        }
        return result + fight.getBonusEnemie();
    }
    @Override
    public void run() {
        this.fight = new GameFight(playerInstance ,currentEnemieInstance);
        while (playerInstance.getHP() < 0 || currentEnemieInstance.getHP() < 0){
            //todo lancer l'animation des dés du joueur
            //todo lancer l'animation des dés de l'enemie
            if(getResultEnemie() > getResultPlayer()){
                playerInstance.setHP(playerInstance.getHP() - 1);
                //todo modifier l'affichage du coeur
            }
            else if(getResultPlayer() > getResultEnemie()){
                currentEnemieInstance.setHP(currentEnemieInstance.getHP() - 1);
                //todo modifier l'affichage du coeur
            }
            else {
                Random random = new Random();
                if(random.nextInt(2) == 1){
                    //enemie perdu
                    currentEnemieInstance.setHP(currentEnemieInstance.getHP() - 1);
                }
                else {
                    //player perdu
                    playerInstance.setHP(playerInstance.getHP() - 1);
                }
                //todo modifier l'affichage du coeur
            }
        }
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
