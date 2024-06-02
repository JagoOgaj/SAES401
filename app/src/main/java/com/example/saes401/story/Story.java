package com.example.saes401.story;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
    private Thread thread;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intent = getIntent();
        if (intent != null) {
            this.initAttibuts();
        }
        setContentView(R.layout.gameplay);
        this.startStory();
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
        int hp = JsonReader.getEnemieHP(this, String.format(GameConstant.FORMAT_LEVEL, this.currentLevel), this.currentEnemieIndex);
        String name = JsonReader.getEnemieName(this, String.format(GameConstant.FORMAT_LEVEL, this.currentLevel), this.currentEnemieIndex);
        Inventory inventory =  new Inventory(JsonReader.getItemsOfEnemie(this, String.format(GameConstant.FORMAT_LEVEL, this.currentLevel), this.currentEnemieIndex).length);
        String damage = JsonReader.getEnemieDamageStringFormat(this, String.format(GameConstant.FORMAT_LEVEL, this.currentLevel), this.currentEnemieIndex);
        String image = JsonReader.getEnemieImageSrc(this, String.format(GameConstant.FORMAT_LEVEL, this.currentLevel), this.currentEnemieIndex);
        this.currentEnemieInstance = new Enemie(
                hp,
                name,
                this.currentLevel,
                this.currentEnemieIndex,
                inventory,
                damage,
                image
        );
    }

    private void addItemOfEnemie() throws Exception {
        String[] items = JsonReader.getItemsOfEnemie(this, String.format(GameConstant.FORMAT_LEVEL, this.currentLevel), this.currentEnemieIndex);
        for (String item : items) {
            this.currentEnemieInstance.getInventory().addItemsEnemie(new Item(
                    JsonReader.getObjectName(this, item),
                    JsonReader.getObjectDamage(this, item),
                    JsonReader.getImageObject(this, item),
                    JsonReader.getObjectDesc(this, item)
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
        thread = new Thread(this);
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
        try {
            initFront();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        this.fightInstance = new GameFight(playerInstance, currentEnemieInstance, this);
        while (true) {
            if(playerInstance.getHPplayer() == 0 || currentEnemieInstance.getHPEnemie() == 0) break;
            try {
                thread.sleep(6000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            //todo lancer l'animation des dés du joueur
            //todo lancer l'animation des dés de l'enemie
            if (getResultEnemie() > getResultPlayer()) {
                playerInstance.setHP(playerInstance.getHPplayer() - 1);
                //todo modifier l'affichage du coeur
            } else if (getResultPlayer() > getResultEnemie()) {
                currentEnemieInstance.setHP(currentEnemieInstance.getHPEnemie() - 1);
                //todo modifier l'affichage du coeur
            } else {
                Random random = new Random();
                if (random.nextInt(2) == 1) {
                    //enemie perdu
                    currentEnemieInstance.setHP(currentEnemieInstance.getHPEnemie() - 1);
                } else {
                    //player perdu
                    playerInstance.setHP(playerInstance.getHPplayer() - 1);
                }
                //todo modifier l'affichage du coeur
            }
        }
        this.gameContinue = playerInstance.getHPplayer() == 0;
        startActivityGame();
    }

    private void initFront() throws Exception {
        initFrontPlayer();
        initFrontEnemie();
    }

    private void initFrontEnemie() {
       initFrontHeart(getLinearHeartContainerEnemie(), currentEnemieInstance.getHPEnemie());
       initAvatar(getEnemieImageView(), getResources().getIdentifier(currentEnemieInstance.getImageSrc(), "drawable", getPackageName()), true);
       initNameEnemie(getTextViewEnemyName(), currentEnemieInstance.getName());
    }

    private void initFrontPlayer() throws Exception {
        initFrontHeart(getLinearHeartContainerPlayer(), playerInstance.getHPplayer());
        initAvatar(getPlayerImageView(), R.drawable.sf_gorath_le_guerrier, false);
        initChoiseLoot(getViewChoiseLoot(), playerInstance.getInventory());
    }


    private void initFrontHeart(LinearLayout layout, int hp){
        layout.removeAllViews();
        for (int i=0; i < hp; i++){
            ImageView imageView = new ImageView(this);
            imageView.setImageResource(R.drawable.coueurtest);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            imageView.setLayoutParams(layoutParams);
            layout.addView(imageView);
        }
    }

    private void initChoiseLoot(LinearLayout layout, Inventory inventory) throws Exception {
        layout.removeAllViews();
        for (int i = 0; i < inventory.getCurentLength() + 1; i++){
            ImageView imageView = new ImageView(this);
            imageView.setImageResource(getResources().getIdentifier(inventory.getItem(i).getImage(), "drawable", getPackageName()));
            layout.addView(imageView);
        }
    }

    private void initNameEnemie(TextView textView, String name){
        textView.setText(name);
    }

    private void initAvatar(ImageView imageView, int resID, boolean needRotation){
        imageView.setImageResource(resID);
        if (needRotation) imageView.setScaleX(-1);
    }

    private ImageView getEnemieImageView() {
        return findViewById(R.id.enemieImage);
    }


    private ImageView getPlayerImageView() {
        return findViewById(R.id.playerImage);
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

    private LinearLayout getLinearHeartContainerEnemie() {
        return findViewById(R.id.heartContainer2);
    }

    private LinearLayout getLinearHeartContainerPlayer() {
        return findViewById(R.id.heartContainer1);
    }

    private TextView getTextViewEnemyName(){
        return findViewById(R.id.enemieName);
    }

}
