package com.example.saes401;

import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.saes401.entities.Characters;
import com.example.saes401.entities.Player;
import com.example.saes401.story.Story;

import java.util.ArrayList;

public class GameActivity extends AppCompatActivity {

    private ArrayList<Button> buttonChoise = new ArrayList<Button>();
    private TextView hpCountTextView, timeCountTextView, textContentGame;

    private Button actionButton, inventory;
    private Player player;
    private Story story;

    @Override
    protected void onCreate(Bundle bundle){
        super.onCreate(bundle);
        setContentView(R.layout.activity_game_activity);
        hpCountTextView = findViewById(R.id.hpCount);
        timeCountTextView = findViewById(R.id.timeCount);
        textContentGame = findViewById(R.id.textContent);
        inventory = findViewById(R.id.invantory);
        actionButton = findViewById(R.id.actions);
        remplirArrayListChoice();
        selectCharacter();
    }

    private void remplirArrayListChoice(){
        Button buttonChoice1 = findViewById(R.id.choice1);
        Button buttonChoice2 = findViewById(R.id.choice2);
        Button buttonChoice3 = findViewById(R.id.choice3);
        Button buttonChoice4 = findViewById(R.id.choice4);

        buttonChoise.add(buttonChoice1);
        buttonChoise.add(buttonChoice2);
        buttonChoise.add(buttonChoice3);
        buttonChoise.add(buttonChoice4);
    }

    private void selectCharacter(){
        hpCountTextView.setVisibility(View.INVISIBLE);
        timeCountTextView.setVisibility(View.INVISIBLE);
        inventory.setVisibility(View.INVISIBLE);
        actionButton.setVisibility(View.INVISIBLE);

        Characters[] personnage = Characters.getAllCharacter();
        for(int i = 0; i < buttonChoise.size(); i++){
            final int index = i;
            if(i == personnage.length){
                buttonChoise.get(i).setVisibility(View.INVISIBLE);
            }
            else {
                buttonChoise.get(i).setText(personnage[i].getName());
                buttonChoise.get(i).setOnClickListener(View -> {
                    player = new Player(personnage[index]);
                    textContentGame.setText("Tu a choisi le "+personnage[index].getName());
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            startGame();
                        }
                    }, 3000);
                });
            }
        }
    }

    private void startGame(){
        resetLayout();
        story = new Story(player);
        //commencer l'histoire en fonction du character choisie
    }

    private void resetLayout(){
        for(Button b : buttonChoise){
            b.setText("");
            b.setVisibility(View.VISIBLE);
        }
        textContentGame.setText("");
        hpCountTextView.setVisibility(View.VISIBLE);
        timeCountTextView.setVisibility(View.VISIBLE);
        inventory.setVisibility(View.VISIBLE);
        actionButton.setVisibility(View.VISIBLE);
    }

}
