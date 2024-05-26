package com.example.saes401.utilities;

import android.os.Parcel;
import android.os.Parcelable;

public class Item implements Parcelable {
    private String name;
    private int damage;
    private int heal;
    private int inventorySize;

    public Item(String name, int damage, int heal, int inventorySize) {
        this.name = name;
        this.damage = damage;
        this.heal = heal;
        this.inventorySize = inventorySize;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
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
