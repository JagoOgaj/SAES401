package com.example.saes401.utilities;

import com.example.saes401.entities.Enemie;
import com.example.saes401.entities.Player;

import java.util.Random;

public class GameFight {
    private Random random;
    private Player player;
    private Enemie enemie;

    public GameFight(Player player, Enemie enemie){
        this.enemie = enemie;
        this.player = player;
        this.random = new Random();
    }

    public int[] getDicePlayer(){
        int[] dices = new int[random.nextInt(3) + 1];
        for(int i = 0; i < dices.length; i++){
            dices[i] = random.nextInt(6) + 1;
        }
        return dices;
    }

    public int[] getDiceEnemie(){
        int[] dices = new int[random.nextInt(3) + 1];
        for(int i = 0; i < dices.length; i++){
            dices[i] = random.nextInt(6) + 1;
        }
        return dices;
    }

    public int getBonusPlayer(){
        return player.getItem().getDamage();
    }

    public int getBonusEnemie(){
        return enemie.getItem().getDamage();
    }
}
