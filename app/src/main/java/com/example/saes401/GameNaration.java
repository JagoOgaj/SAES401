package com.example.saes401;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.saes401.entities.Enemie;
import com.example.saes401.entities.Player;
import com.example.saes401.helper.GameConstant;
import com.example.saes401.helper.JsonReader;
import com.example.saes401.helper.OnTextLoadedListener;
import com.example.saes401.helper.Utilities;
import com.example.saes401.story.Story;

import java.io.Serializable;

public class GameNaration extends AppCompatActivity implements Utilities {
    private Intent intent;
    private int currentLevel;
    private int currentIndexEnemie;
    private Boolean isPLayerWin;

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
            this.isPLayerWin = false;
            this.currentIndexEnemie = 0;
        }
        try {
            launchNaration();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void initAttibuts() {
        this.currentLevel = intent.getIntExtra(GameConstant.KEY_LEVEL, 0);
        this.isPLayerWin = intent.getBooleanExtra(GameConstant.KEY_PLAYER_WIN, false);
        this.currentIndexEnemie = intent.getIntExtra(GameConstant.KEY_ENEMIE_INDEX, 0);
    }

    @Override
    public void startActivityGame() {
        this.intent = new Intent(this, GameActivity.class);
        this.intent.putExtra(GameConstant.KEY_LEVEL, this.currentLevel);
        this.intent.putExtra(GameConstant.KEY_PREVIOUS_ACTIVITY, GameConstant.VALUE_GAME_NARATION);
        this.intent.putExtra(GameConstant.KEY_PLAYER_WIN, this.isPLayerWin);
        this.intent.putExtra(GameConstant.KEY_ENEMIE_INDEX, this.currentIndexEnemie);
        startActivity(this.intent);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        currentLevel = savedInstanceState.getInt(GameConstant.KEY_LEVEL);
        isPLayerWin = savedInstanceState.getBoolean(GameConstant.KEY_PLAYER_WIN);
        currentIndexEnemie = savedInstanceState.getInt(GameConstant.KEY_ENEMIE_INDEX);
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
        outState.putBoolean(GameConstant.KEY_PLAYER_WIN, this.isPLayerWin);
        outState.putInt(GameConstant.KEY_ENEMIE_INDEX, this.currentIndexEnemie);
    }

    @Override
    public void setListener() {

    }

    private void launchNaration() throws Exception {
        if (this.currentLevel < 0 || this.currentLevel > 3) {
            throw new Exception("null level");
        } else {
            String naration;
            if (!isPLayerWin){
                naration = JsonReader.getNarationAfterLooseEnemie(this, String.format(GameConstant.FORMAT_LEVEL, this.currentLevel), this.currentIndexEnemie);
            }
            else {
                naration = JsonReader.getNarationAfterWinEnemie(this, String.format(GameConstant.FORMAT_LEVEL, this.currentLevel), this.currentIndexEnemie);
            }
            setVisibilityOfContinue(
                    getTextView(),
                    naration
            );
        }
    }

    private void loadText(TextView textView, String naration, OnTextLoadedListener listener) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                final Handler handler = new Handler();
                for (int k = 0; k < naration.length(); k++) {
                    final int finalK = k;
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            textView.append(String.valueOf(naration.charAt(finalK)));
                            if (finalK == naration.length() - 1 && listener != null) {
                                listener.onTextLoaded();
                            }
                        }
                    }, 100 * k);
                }
            }
        }, 2000);
    }

    private void setVisibilityOfContinue(TextView textView, String naration) {
        loadText(textView, naration, new OnTextLoadedListener() {
            @Override
            public void onTextLoaded() {
                getButtonContinue().setVisibility(View.VISIBLE);
                getButtonContinue().setOnClickListener(v -> startActivityGame());
            }
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
}
