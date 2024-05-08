package com.example.saes401.entities;

public abstract class GameCharacter {
    private String name;
    private int HP;
    private int damage;


    public GameCharacter(String name, int HP, int damage) {
        this.name = name;
        this.HP = HP;
        this.damage = damage;
    }

    public String getName() {
        return name;
    }

    public int getHP() {
        return HP;
    }

    public int getDamage() {
        return damage;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDamage(int damage) {
        this.damage = damage;
    }

    public void setHP(int HP) {
        this.HP = HP;
    }
}
