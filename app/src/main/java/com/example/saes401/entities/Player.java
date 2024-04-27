package com.example.saes401.entities;

import com.example.saes401.helper.GameConstant;
import com.example.saes401.utilities.Inventory;

public class Player extends GameCharacter{
    private Inventory inventory;
    private int coin;
    public Player(String name, int HP, int damage, int coin){
        super(name, HP, damage);
        inventory = new Inventory(GameConstant.DEFAULT_INVENTORY_SLOT);
        this.coin = coin;

    }



}
