package com.example.saes401.entities;

public enum Characters {
    KNIGHT(10,100,4, "knight"),

    NINJA(20, 40, 2, "ninja"),
    WIZZARD(50, 20, 5, "wizzard");

    private final int damage;
    private final int hp;
    private final int inventorySlot;
    private String name;

     Characters(int damage, int hp, int inventorySlot, String name){
        this.damage = damage;
        this.hp = hp;
        this.inventorySlot = inventorySlot;
        this.name = name;
    }

    public int getHp() {
        return hp;
    }

    public int getDamage() {
        return damage;
    }

    public int getInventorySlot() {
        return inventorySlot;
    }

    public String getName(){
         return name;
    }

    public static Characters[] getAllCharacter(){
         return Characters.values();
    }
}
