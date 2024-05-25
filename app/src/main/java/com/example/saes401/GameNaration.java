package com.example.saes401;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.example.saes401.helper.GameConstant;
import com.example.saes401.helper.JsonReader;
import com.example.saes401.helper.OnTextLoadedListener;
import com.example.saes401.helper.Utilities;
import com.example.saes401.story.Story;

public class GameNaration extends AppCompatActivity implements Utilities {
    private Intent intent;
    private int currentLevel;

    @Override
    protected void onCreate(Bundle savedInstance) {
        super.onCreate(savedInstance);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_naration);
        intent = getIntent();
        if (intent != null){
            this.initAttibuts();
        }
        if (savedInstance == null && intent == null){
            this.currentLevel = 0;
        }

        try{
            launchNaration();
        }
        catch (Exception e){
            e.printStackTrace();
        }
    }
    @Override
    public void initAttibuts() {
        this.currentLevel = intent.getIntExtra(GameConstant.KEY_LEVEL, 0);
    }
    @Override
    public void startActivityGame() {
        this.intent = new Intent(this, GameActivity.class);
        this.intent.putExtra(GameConstant.KEY_LEVEL, this.currentLevel);
        this.intent.putExtra(GameConstant.KEY_PREVIOUS_ACTIVITY, GameConstant.VALUE_GAME_NARATION);
        startActivity(this.intent);
    }

    @Override
    public void setListener() {

    }

    private void launchNaration() throws Exception {
        if(this.currentLevel < 0  || this.currentLevel > 3){
            throw new Exception("null level");
        }
        else {
            setVisibilityOfContinue(
                    getTextView(),
                    JsonReader.getNaration(this, this.currentLevel)
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
