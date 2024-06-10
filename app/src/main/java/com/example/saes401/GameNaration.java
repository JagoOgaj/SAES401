package com.example.saes401;

import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.saes401.db.DataModel;
import com.example.saes401.entities.Player;
import com.example.saes401.helper.GameConstant;
import com.example.saes401.helper.JsonReader;
import com.example.saes401.helper.OnTextLoadedListener;
import com.example.saes401.helper.Utilities;
import com.example.saes401.soud.GameSound;

public class GameNaration extends AppCompatActivity implements Utilities {
    private Intent intent;
    private int currentLevel;
    private int currentIndexEnemie;
    private Boolean gameContinue;
    private Boolean levelStart;
    private String naration;
    private Player playerInstance;
    private DataModel dataModel;
    private volatile boolean clickScreen = false;
    private static MediaPlayer narrationMediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_naration);
        intent = getIntent();
        if (intent != null) {
            this.initAttibuts();
        }
        if (savedInstance == null && intent == null) {
            this.currentLevel = 0;
            this.gameContinue = false;
            this.currentIndexEnemie = 0;
        }
        try {
            launchNaration();
            setListener();
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (narrationMediaPlayer == null) {
            narrationMediaPlayer = GameSound.narrationSound(this);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        // Ne pas arrêter le son ici pour qu'il continue de jouer en arrière-plan
    }

    private void stopNarrationSound() {
        if (narrationMediaPlayer != null) {
            GameSound.stopNarrationSound(narrationMediaPlayer);
            narrationMediaPlayer = null;
        }
    }

    @Override
    public void initAttibuts() {
        this.currentLevel = intent.getIntExtra(GameConstant.KEY_LEVEL, 0);
        this.gameContinue = intent.getBooleanExtra(GameConstant.KEY_PLAYER_WIN, false);
        this.currentIndexEnemie = intent.getIntExtra(GameConstant.KEY_ENEMIE_INDEX, 0);
        this.levelStart = intent.getBooleanExtra(GameConstant.KEY_START_LEVEL, false);
        this.playerInstance = intent.getParcelableExtra(GameConstant.KEY_PLAYER);
        this.dataModel = intent.getParcelableExtra(GameConstant.KEY_DATA_MODEL);
    }

    @Override
    public void startActivityGame() {
        GameSound.playClickSound(this);
        stopNarrationSound();
        this.intent = new Intent(this, GameActivity.class);
        this.intent.putExtra(GameConstant.KEY_LEVEL, this.currentLevel);
        this.intent.putExtra(GameConstant.KEY_PREVIOUS_ACTIVITY, GameConstant.VALUE_GAME_NARATION);
        this.intent.putExtra(GameConstant.KEY_PLAYER_WIN, this.gameContinue);
        this.intent.putExtra(GameConstant.KEY_ENEMIE_INDEX, this.currentIndexEnemie);
        this.intent.putExtra(GameConstant.KEY_START_LEVEL, this.levelStart);
        this.intent.putExtra(GameConstant.KEY_PLAYER, this.playerInstance);
        this.intent.putExtra(GameConstant.KEY_DATA_MODEL, this.dataModel);
        startActivity(this.intent);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        currentLevel = savedInstanceState.getInt(GameConstant.KEY_LEVEL);
        gameContinue = savedInstanceState.getBoolean(GameConstant.KEY_PLAYER_WIN);
        currentIndexEnemie = savedInstanceState.getInt(GameConstant.KEY_ENEMIE_INDEX);
        playerInstance = savedInstanceState.getParcelable(GameConstant.KEY_PLAYER);
        levelStart = savedInstanceState.getBoolean(GameConstant.KEY_START_LEVEL);
        this.dataModel = savedInstanceState.getParcelable(GameConstant.KEY_DATA_MODEL);
        try {
            launchNaration();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putInt(GameConstant.KEY_LEVEL, this.currentLevel);
        outState.putBoolean(GameConstant.KEY_PLAYER_WIN, this.gameContinue);
        outState.putInt(GameConstant.KEY_ENEMIE_INDEX, this.currentIndexEnemie);
        outState.putParcelable(GameConstant.KEY_PLAYER, this.playerInstance);
        outState.putBoolean(GameConstant.KEY_START_LEVEL, this.levelStart);
        outState.putParcelable(GameConstant.KEY_DATA_MODEL, this.dataModel);
    }

    @Override
    public void setListener() {
        ConstraintLayout rootLayout = findViewById(R.id.rootLayout);
        rootLayout.setOnTouchListener((v, event) -> {
            if (event.getAction() == MotionEvent.ACTION_DOWN) {
                GameSound.playClickSound(this);
                stopLoadingText();
                try {
                    loadFullNaration(getTextView(), naration, getButtonContinue());
                } catch (Exception e) {
                    Log.d("Error -> InitContinueButton", e.getMessage());
                }
            }
            return false;
        });
    }

    private void loadFullNaration(TextView textView, String naration, Button button) throws Exception {
        textView.setText(naration);
        initContinueButton(button);
    }

    private void launchNaration() throws Exception {
        if (this.currentLevel < 0 || this.currentLevel > 3) {
            throw new Exception("null level");
        } else {
            if (levelStart) {
                naration = JsonReader.getNaration(this, this.currentLevel - 1);
            } else if (!gameContinue) {
                naration = JsonReader.getNarationAfterLooseEnemie(this, String.format(GameConstant.FORMAT_LEVEL, this.currentLevel), this.currentIndexEnemie - 1);
            } else {
                naration = JsonReader.getNarationAfterWinEnemie(this, String.format(GameConstant.FORMAT_LEVEL, this.currentLevel), this.currentIndexEnemie - 1);
            }
            setVisibilityOfContinue(getTextView());
        }
    }

    private void loadText(TextView textView, OnTextLoadedListener listener) {
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            final Handler handler = new Handler(Looper.getMainLooper());
            for (int k = 0; k < naration.length(); k++) {
                final int finalK = k;
                handler.postDelayed(() -> {
                    if (clickScreen) return;
                    textView.append(String.valueOf(naration.charAt(finalK)));
                    if (finalK == naration.length() - 1 && listener != null) {
                        try {
                            listener.onTextLoaded();
                        } catch (Exception e) {
                            Log.d("Error -> InitContinueButton", e.getMessage());
                        }
                    }
                }, 100L * k);
            }
        }, 2000);
    }

    private void setVisibilityOfContinue(TextView textView) {
        loadText(textView, () -> {
            initContinueButton(getButtonContinue());
        });
    }

    private void initContinueButton(Button btn) throws Exception {
        btn.setVisibility(View.VISIBLE);
        btn.setOnClickListener(v -> {
            GameSound.playClickSound(this);
            startActivityGame();
        });
    }

    private TextView getTextView() {
        return findViewById(R.id.narationContainer);
    }

    private Button getButtonContinue() {
        return findViewById(R.id.continueButton);
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

    private void stopLoadingText() {
        clickScreen = true;
    }
}


