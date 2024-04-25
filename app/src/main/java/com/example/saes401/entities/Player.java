package com.example.saes401.entities;

import com.example.saes401.utilities.Inventory;

public class Player {
    Characters character;
    private int HP;
    private int DAMAGE;
    private Inventory inventory;

    public Player(Characters character){
        HP = character.getHp();
        DAMAGE = character.getDamage();
        inventory = new Inventory(character.getInventorySlot());
    }

}
