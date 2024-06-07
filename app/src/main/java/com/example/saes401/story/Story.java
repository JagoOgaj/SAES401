package com.example.saes401.story;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
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
    private Map<String, ImageView> heartMap = new HashMap<String, ImageView>();
    private ArrayList<ImageView> imageViewsPLayer = new ArrayList<ImageView>();
    private ArrayList<ImageView> imageViewsEnemie = new ArrayList<ImageView>();
    private int indexItemChoose = -1;
    private Map<ImageView, View.OnClickListener> savedOnClickListeners = new HashMap<>();
    private final Object lock = new Object();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        intent = getIntent();
        if (intent != null) {
            this.initAttibuts();
        }
        setContentView(R.layout.gameplay);
        try {
            initFront();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
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
        Inventory inventory = new Inventory(JsonReader.getItemsOfEnemie(this, String.format(GameConstant.FORMAT_LEVEL, this.currentLevel), this.currentEnemieIndex).length);
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
        setListenerImageView();
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

    private int getResultPlayer(int[] dices) {
        int resultPlayer = 0;
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

    private int getResultEnemie(int[] dices, boolean isBoss, Item itemEnemie) {
        int result = 0;
        try {
            if (isBoss) { //Multiplie ou ajoute touts les bonus pour chaque dès
                for (int i = 0; i < dices.length; i++) {
                    result += fightInstance.getResultEnemie(dices[i], itemEnemie);
                }
            } else { //Multiplie ou ajoute aux résultat des dès
                for (int i = 0; i < dices.length; i++) {
                    result += (dices[i]);
                }
                result = fightInstance.getResultEnemie(result, itemEnemie);
            }
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
        return result;
    }

    @Override
    public void run() {
        this.fightInstance = new GameFight(playerInstance, currentEnemieInstance, this);
        while (true) {
            synchronized (lock) {
                try {
                    runOnUiThread(() -> {
                        getInformationTextView().setText("Selectionnez un item");
                    });
                    lock.wait();
                } catch (InterruptedException e) {
                    Log.d("error -> InitFront", Objects.requireNonNull(e.getMessage()));
                }
            }
            runOnUiThread(() -> {
                getInformationTextView().setText("");
            });
            int[] dicesResultPlayer = fightInstance.getDicePlayer();
            runOnUiThread(() -> animateDiceRoll(dicesResultPlayer, true, -1));
            waitForDelay();
            int resultPlayer = getResultPlayer(dicesResultPlayer);
            runOnUiThread(() -> updateDiceResult(dicesResultPlayer, resultPlayer, true));
            waitForDelay();
            int[] numberDicesEnemie = new int[0];
            Item itemEnemie = currentEnemieInstance.getItem();
            try {
                numberDicesEnemie = fightInstance.getDiceEnemie();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            int[] finalNumberDicesEnemie1 = numberDicesEnemie;
            runOnUiThread(() -> animateDiceRoll(finalNumberDicesEnemie1, false, currentEnemieInstance.getInventory().getIndexOfItem(itemEnemie)));
            waitForDelay();
            int resultEnemie = 0;
            try {
                resultEnemie = getResultEnemie(numberDicesEnemie, this.currentEnemieIndex == JsonReader.getNumberEnemies(this, String.format(GameConstant.FORMAT_LEVEL, this.currentLevel)), itemEnemie);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
            int finalResultEnemie = resultEnemie;
            int[] finalNumberDicesEnemie = numberDicesEnemie;
            runOnUiThread(() -> updateDiceResult(finalNumberDicesEnemie, finalResultEnemie, false));
            waitForDelay();
            if (resultEnemie > resultPlayer) {
                playerInstance.setHP(playerInstance.getHPplayer() - 1);
                runOnUiThread(() -> setFrontHeart(GameConstant.FORMAT_HEART_PLAYER, playerInstance.getHPplayer(), true));
            } else if (resultPlayer > resultEnemie) {
                currentEnemieInstance.setHP(currentEnemieInstance.getHPEnemie() - 1);
                runOnUiThread(() -> setFrontHeart(GameConstant.FORMAT_HEART_ENEMIE, currentEnemieInstance.getHPEnemie(), true));
            } else {
                Random random = new Random();
                if (random.nextInt(2) == 1) {
                    currentEnemieInstance.setHP(currentEnemieInstance.getHPEnemie() - 1);
                    runOnUiThread(() -> setFrontHeart(GameConstant.FORMAT_HEART_ENEMIE, currentEnemieInstance.getHPEnemie(), true));
                } else {
                    playerInstance.setHP(playerInstance.getHPplayer() - 1);
                    runOnUiThread(() -> setFrontHeart(GameConstant.FORMAT_HEART_PLAYER, playerInstance.getHPplayer(), true));
                }
                runOnUiThread(() -> {
                    setFrontHeart(GameConstant.FORMAT_HEART_PLAYER, playerInstance.getHPplayer(), false);
                    setFrontHeart(GameConstant.FORMAT_HEART_ENEMIE, currentEnemieInstance.getHPEnemie(), false);
                });
            }
            if (playerInstance.getHPplayer() <= 0 || currentEnemieInstance.getHPEnemie() == 0) {
                runOnUiThread(() -> {
                    getViewGameplay().removeAllViews();
                    getTextViewGamePLay().setText("");
                    getTextScoreEnemie().setText("");
                    getTextScorePlayer().setText("");
                    getInformationTextView().setText("");
                    String text = playerInstance.getHPplayer() == 0 ? "Vous avez perdu" : "Vous avez gagnez";
                    getTextViewGamePLay().setText(text);
                });
                waitForDelay();
                break;
            } else {
                restoreClickListeners();
            }
        }
        runOnUiThread(() -> {
            setListenerButtonTakeItem(true);
        });
        this.gameContinue = playerInstance.getHPplayer() > 0;
        synchronized (lock) {
            try {
                lock.wait();
            } catch (InterruptedException e) {
                Log.d("error -> InitFront", Objects.requireNonNull(e.getMessage()));
            }
        }
        startActivityGame();
    }


    private void initFront() throws Exception {
        setTextGameplay(-1);
        setScoreText(getTextScoreEnemie(), 0);
        setScoreText(getTextScorePlayer(), 0);
        setCurrentLevelFront();
        setVisibilityButtonTake(false);
        initFrontPlayer();
        initFrontEnemie();
        setListener();
    }

    private void setCurrentLevelFront() {
        getCurrentLevelTextView().setText(
                String.format(
                        GameConstant.FORMAT_CURRENT_LEVEL,
                        this.currentLevel,
                        this.currentEnemieIndex
                ));
    }

    private void setTextGameplay(int result) {
        if (result == -1) getTextViewGamePLay().setText("");
        else getTextViewGamePLay().setText(String.valueOf(result));
    }

    private void setScoreText(TextView textView, int result) {
        textView.setText(
                String.format(
                        GameConstant.FORMAT_SCORE,
                        result
                )
        );
    }

    private void setVisibilityButtonTake(boolean b) {
        if (b) getButtonTakeItem().setVisibility(View.VISIBLE);
        else getButtonTakeItem().setVisibility(View.INVISIBLE);
    }

    private void initFrontEnemie() throws Exception {
        initFrontHeart(getLinearHeartContainerEnemie(), currentEnemieInstance.getHPEnemie(), GameConstant.FORMAT_HEART_ENEMIE);
        initAvatar(getEnemieImageView(), getResources().getIdentifier(currentEnemieInstance.getImageSrc(), "drawable", getPackageName()), true);
        initNameEnemie(getTextViewEnemyName(), currentEnemieInstance.getName());
        initLinearItems(getLinearItemsEnemie(), currentEnemieInstance.getInventory(), false);
    }

    private void initFrontPlayer() throws Exception {
        initFrontHeart(getLinearHeartContainerPlayer(), playerInstance.getHPplayer(), GameConstant.FORMAT_HEART_PLAYER);
        initAvatar(getPlayerImageView(), R.drawable.sf_gorath_le_guerrier, false);
        initLinearItems(getViewChoiseLoot(), playerInstance.getInventory(), true);
    }


    private void initFrontHeart(LinearLayout layout, int hp, String prefix) {
        layout.removeAllViews();
        for (int i = 0; i < hp; i++) {
            ImageView imageView = new ImageView(this);
            imageView.setImageResource(R.drawable.coueurtest);
            String id = String.format(prefix, i);
            imageView.setTag(id);
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    LinearLayout.LayoutParams.WRAP_CONTENT,
                    LinearLayout.LayoutParams.WRAP_CONTENT
            );
            imageView.setLayoutParams(layoutParams);
            layout.addView(imageView);
            heartMap.put(id, imageView);
        }
    }

    public void setVisibilityOfHeart(int index, String prefix) {
        try {
            heartMap.get(String.format(prefix, index)).setVisibility(ImageView.INVISIBLE);
        } catch (Exception e) {
            Log.d("error -> setVisibilityOfHeart", Objects.requireNonNull(e.getMessage()));
        }
    }

    public void setFrontHeart(String prefix, int index, boolean needSleep) {
        getViewGameplay().removeAllViews();
        getTextViewGamePLay().setText("");
        getTextScoreEnemie().setText("");
        getTextScorePlayer().setText("");
        if (needSleep) {
            try {
                setVisibilityOfHeart(index, prefix);
                Thread.sleep(2000);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        } else setVisibilityOfHeart(index, prefix);
    }

    private void initLinearItems(LinearLayout layout, Inventory inventory, boolean isPlayer) throws Exception {
        layout.removeAllViews();
        for (int i = 0; i < inventory.getCurentLength(); i++) {
            ImageView imageView = new ImageView(this);
            imageView.setImageResource(getResources().getIdentifier(inventory.getItem(i).getImage(), "drawable", getPackageName()));
            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                    GameConstant.WIDTH_HEIGHT_ITEMS, // Largeur en pixels
                    GameConstant.WIDTH_HEIGHT_ITEMS // Hauteur en pixels
            );
            layoutParams.setMargins(
                    GameConstant.MARGIN_ITEM,
                    GameConstant.MARGIN_ITEM,
                    GameConstant.MARGIN_ITEM,
                    GameConstant.MARGIN_ITEM
            );
            imageView.setLayoutParams(layoutParams);
            if (isPlayer) {
                imageView.setTag(inventory.getItem(i));
                imageViewsPLayer.add(imageView);
            } else imageViewsEnemie.add(imageView);
            layout.addView(imageView);
        }
    }

    private void waitForDelay() {
        synchronized (lock) {
            try {
                lock.wait(GameConstant.DELAY_TIME);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private void animateDiceRoll(int[] resultDices, boolean isPlayer, int itemEnemie) {
        getTextViewGamePLay().setText("");
        getViewGameplay().removeAllViews();
        if (!isPlayer) {
            imageViewsEnemie.get(itemEnemie).setColorFilter(Color.argb(150, 0, 0, 0)); // Assombrir l'image
            getTextViewGamePLay().setText(currentEnemieInstance.getInventory().getItem(itemEnemie).getDesc());
            getInformationTextView().setText(currentEnemieInstance.getName() + " joue");
        }
        for (int i = 0; i < resultDices.length; i++) {
            ImageView imageView = initImageView(R.drawable.animation_dice_test, true);
            AnimationDrawable anim = (AnimationDrawable) imageView.getBackground();
            getViewGameplay().addView(imageView);
            anim.start();
        }
    }

    private void updateDiceResult(int[] dicesResult, int result, boolean isPlayer) {
        getTextViewGamePLay().setText("");
        getTextScoreEnemie().setText("");
        clearColorFilterImageView(imageViewsEnemie);
        int[] drawable = {
                R.drawable.dicetest1,
                R.drawable.dicetest2,
                R.drawable.dicetest3,
                R.drawable.dicetest4,
                R.drawable.dicetest5,
                R.drawable.dicetest6
        };
        getViewGameplay().removeAllViews();
        for (int i = 0; i < dicesResult.length; i++) {
            ImageView imageView = initImageView(drawable[dicesResult[i] - 1], false);
            getViewGameplay().addView(imageView);
        }
        setTextGameplay(result);
        TextView textView = isPlayer ? getTextScorePlayer() : getTextScoreEnemie();
        setScoreText(textView, result);
    }

    private ImageView initImageView(int res, boolean isAnimation) {
        ImageView imageView = new ImageView(this);
        if (isAnimation) imageView.setBackgroundResource(res);
        else imageView.setImageResource(res);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(
                GameConstant.WIDTH_HEIGHT_ITEMS, // Largeur en pixels
                GameConstant.WIDTH_HEIGHT_ITEMS // Hauteur en pixels
        );
        imageView.setLayoutParams(layoutParams);
        return imageView;
    }

    private void initNameEnemie(TextView textView, String name) {
        textView.setText(name);
    }

    private void initAvatar(ImageView imageView, int resID, boolean needRotation) {
        imageView.setImageResource(resID);
        if (needRotation) imageView.setScaleX(-1);
    }

    private ImageView getEnemieImageView() {
        return findViewById(R.id.enemieImage);
    }

    private LinearLayout getLinearItemsEnemie() {
        return findViewById(R.id.enemieItems);
    }

    private ImageView getPlayerImageView() {
        return findViewById(R.id.playerImage);
    }

    private LinearLayout getViewGameplay() {
        return findViewById(R.id.gameplay);
    }

    private TextView getTextViewGamePLay() {
        return findViewById(R.id.resultTextGamplay);
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

    private TextView getTextScoreEnemie() {
        return findViewById(R.id.scoreEnemie);
    }

    private TextView getTextScorePlayer() {
        return findViewById(R.id.scorePlayer);
    }

    private TextView getTextViewEnemyName() {
        return findViewById(R.id.enemieName);
    }

    private Button getButtonTakeItem() {
        return findViewById(R.id.takeItem);
    }

    private TextView getCurrentLevelTextView() {
        return findViewById(R.id.currentLevel);
    }

    private TextView getInformationTextView() {
        return findViewById(R.id.infoTextView);
    }

    private void setListenerImageView() {
        for (ImageView imageView : imageViewsPLayer) {
            View.OnClickListener listener = new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    clearColorFilterImageView(imageViewsPLayer);
                    Item item = (Item) imageView.getTag();
                    indexItemChoose = playerInstance.getInventory().getIndexOfItem(item);
                    imageView.setColorFilter(Color.argb(150, 0, 0, 0)); // Assombrir l'image
                    getTextViewGamePLay().setText(String.valueOf(playerInstance.getInventory().getItem(indexItemChoose).getDesc()));
                    setVisibilityButtonTake(true);
                    setListenerButtonTakeItem(false);
                }
            };
            savedOnClickListeners.put(imageView, listener);
            imageView.setOnClickListener(listener);
        }
    }

    private void removeClickListeners() {
        for (ImageView imageView : imageViewsPLayer) {
            imageView.setOnClickListener(null);
        }
    }

    private void restoreClickListeners() {
        for (Map.Entry<ImageView, View.OnClickListener> entry : savedOnClickListeners.entrySet()) {
            entry.getKey().setOnClickListener(entry.getValue());
        }
    }

    private void clearColorFilterImageView(ArrayList<ImageView> imageViews) {
        for (ImageView imageView : imageViews) {
            imageView.clearColorFilter();
        }
    }

    private void setListenerButtonTakeItem(boolean isEnd) {
        if (!isEnd) {
            getButtonTakeItem().setOnClickListener(view -> {
                removeClickListeners();
                clearColorFilterImageView(imageViewsPLayer);
                getTextViewGamePLay().setText("");
                playerInstance.setCurrentItem(indexItemChoose);
                setVisibilityButtonTake(false);
                synchronized (lock) {
                    lock.notify();
                }
            });
        } else {
            getButtonTakeItem().setText("Continuer");
            getButtonTakeItem().setVisibility(View.VISIBLE);
            getButtonTakeItem().setOnClickListener(view -> {
                synchronized (lock) {
                    lock.notify();
                }
            });
        }
    }

}
