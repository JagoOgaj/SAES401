package com.example.saes401.utilities;

public abstract class Item {
    private int durability;
    private String name;
    private int damage;
    private int heal;

    private int inventorySize;

    public Item(int durability, String name, int damage, int heal, int inventorySize) {
        this.durability = durability;
        this.name = name;
        this.damage = damage;
        this.heal = heal;
        this.inventorySize = inventorySize;
    }

    private int getDurability() {
        return durability;
    }

    private void setDurability(int newDurability) {
        durability = newDurability;
    }

    private String getName() {
        return name;
    }

    private int getDamage() {
        return damage;
    }

    private void setDamage(int newDamage) {
        damage = newDamage;
    }

    private int getInventorySize() {
        return inventorySize;
    }

    private int getHeal() {
        return heal;
    }
}
