package com.example.saes401;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.saes401.story.Story;

import java.util.ArrayList;

public class GameActivity extends AppCompatActivity {

    private ArrayList<Button> buttonChoise = new ArrayList<Button>();
    private TextView hpCountTextView, timeCountTextView, textContentGame;

    private Button actionButton, inventory;

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
        Story story = new Story(this);
        story.start();
    }

    public TextView getHpCountTextView() {
        return hpCountTextView;
    }

    public TextView getTimeCountTextView() {
        return timeCountTextView;
    }

    public TextView getTextContentGame() {
        return textContentGame;
    }

    public Button getActionButton() {
        return actionButton;
    }

    public Button getInventory() {
        return inventory;
    }

    public ArrayList<Button> getButtonChoise(){
        return buttonChoise;
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

    public void resetLayout(){
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
