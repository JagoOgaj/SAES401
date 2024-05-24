package com.example.saes401;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.saes401.story.Story;

public class GameActivity extends AppCompatActivity {

    private Intent intent;

    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        intent = getIntent();
    }

    public String getLevel() {
        return intent.getStringExtra("storyLevel");
    }

    public final ImageView getEnemieImageView() {
        return findViewById(R.id.enemieImage);
    }

    public final ImageView getPlayerImageView() {
        return findViewById(R.id.playerImage);
    }

    public final LinearLayout getHeartContainerEnemie() {
        return findViewById(R.id.heartContainer2);
    }

    public final LinearLayout getHeartContainerPlayer() {
        return findViewById(R.id.heartContainer1);
    }

    public final LinearLayout getLootContainerEnemie() {
        return findViewById(R.id.lootContainer2);
    }

    public final LinearLayout getLootContainerPLayer() {
        return findViewById(R.id.lootContainer1);
    }

    public final LinearLayout getGameplay() {
        return findViewById(R.id.gameplay);
    }

    public final TextView getTextViewGamePLay() {
        return findViewById(R.id.resultText);
    }

    public final LinearLayout getChoiseLoot() {
        return findViewById(R.id.choiseLoot);
    }

    public final LinearLayout getAtoutEnemie() {
        return findViewById(R.id.atoutContainer2);
    }

    public final LinearLayout getAtoutPlayer() {
        return findViewById(R.id.atoutContainer1);
    }
}
