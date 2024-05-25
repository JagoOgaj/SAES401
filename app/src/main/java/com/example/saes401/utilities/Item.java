package com.example.saes401.utilities;

import android.os.Parcel;
import android.os.Parcelable;

public class Item implements Parcelable {
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
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(durability);
        dest.writeString(name);
        dest.writeInt(damage);
        dest.writeInt(heal);
        dest.writeInt(inventorySize);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    protected Item(Parcel in) {
        durability = in.readInt();
        name = in.readString();
        damage = in.readInt();
        heal = in.readInt();
        inventorySize = in.readInt();
    }

    public static final Parcelable.Creator<Item> CREATOR = new Creator<Item>() {
        @Override
        public Item createFromParcel(Parcel in) {
            return new Item(in);
        }

        @Override
        public Item[] newArray(int size) {
            return new Item[size];
        }
    };

    private int getDurability() {
        return durability;
    }

    public void setDurability(int newDurability) {
        durability = newDurability;
    }

    public String getName() {
        return name;
    }

    public int getDamage() {
        return damage;
    }

    public void setDamage(int newDamage) {
        damage = newDamage;
    }

    public int getInventorySize() {
        return inventorySize;
    }

    public int getHeal() {
        return heal;
    }
}
