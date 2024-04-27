package com.example.saes401.story;

import com.example.saes401.GameActivity;
import com.example.saes401.entities.Player;
import com.example.saes401.helper.GameConstant;

public class Story extends Thread {
    private GameActivity gameActivity;
    private Player player;
    public Story(GameActivity gameActivity){
        this.player = new Player(GameConstant.PLAYER_NAME, GameConstant.DEFAULT_HP, GameConstant.DEFAULT_MAX_DAMAGE, GameConstant.DEFAULT_COIN);
        this.gameActivity = gameActivity;
    }

    @Override
    public void run() {
        launchStory();
    }

    private void launchStory() {

    }
}
