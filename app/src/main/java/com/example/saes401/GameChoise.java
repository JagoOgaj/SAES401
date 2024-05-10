package com.example.saes401;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.saes401.entities.Player;
import com.example.saes401.helper.JsonReader;
import com.example.saes401.story.Story;

import java.util.ArrayList;

public class GameChoise extends AppCompatActivity {

    private Intent intent;
    private String currentLevel;
    private String currentChoise;
    private Player player;

    @Override
    protected void onCreate(Bundle savedInstance){
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_choise);
        intent = getIntent();
        currentLevel = intent.getStringExtra("curentLevel");
    }

    private LinearLayout getChoisePanel(){
        return findViewById(R.id.choiseBeforeLevel);
    }

    private TextView getTextView(){
        return findViewById(R.id.textLevel);
    }

    private Button getButtonContinue(){
        return findViewById(R.id.buttonContinueToLevel);
    }

    private void init(){
        try{
            initChoisePanel();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        initTextView();
        initBoutonContinue();
    }

    private void initBoutonContinue() {
        getButtonContinue().setVisibility(View.VISIBLE);
        getButtonContinue().setOnClickListener(view -> {
            Intent intent = new Intent(this, Story.class);
            intent.putExtra("curentLevel", currentLevel);
        });
    }

    private void initTextView(){

    }
    private void initChoisePanel() throws Exception {
        String[] choise = JsonReader.getChoise(this, currentLevel);
        for(int i = 0; i < choise.length; i++){
            ImageButton imageButton = new ImageButton(this);
            String imageSrc = JsonReader.getImageObject(this, choise[i]);


            int drawableId = getResources().getIdentifier(imageSrc, "drawable", getPackageName());

            if (drawableId != 0) {

                imageButton.setImageResource(drawableId);
                int finalI = i;
                imageButton.setOnClickListener(view -> {
                    currentChoise = choise[finalI];
                    //ajouter l'objet a l'inventaire du joueur
                });

                getChoisePanel().addView(imageButton);
            } else {
                throw new Exception("null pointer image");
            }
        }
    }

}
