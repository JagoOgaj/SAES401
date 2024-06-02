package com.example.saes401.utilities;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.Random;

public class Inventory implements Parcelable {
    private Item[] items;
    private int slots;

    public Inventory(int slots) {
        items = new Item[slots];
        this.slots = slots;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(slots);
        dest.writeTypedArray(items, flags);
    }

    // Méthode pour décrire le type du contenu du Parcel
    @Override
    public int describeContents() {
        return 0;
    }

    // Constructeur spécial pour recréer l'objet depuis le Parcel
    protected Inventory(Parcel in) {
        slots = in.readInt();
        items = in.createTypedArray(Item.CREATOR);
    }

    // Parcelable CREATOR permettant de recréer les objets Inventory
    public static final Creator<Inventory> CREATOR = new Creator<Inventory>() {
        @Override
        public Inventory createFromParcel(Parcel in) {
            return new Inventory(in);
        }

        @Override
        public Inventory[] newArray(int size) {
            return new Inventory[size];
        }
    };

    public void setItemsInventory(Item newItem) throws Exception {
        if (!canInserItem()) throw new Exception("can't insert into");
        items[getLastIndex()] = newItem;
    }

    public void addItemsEnemie(Item item) throws Exception {
        items[getLastIndex()] = item;
    }

    public void setCapacityInventory(int newCapa) {
        Item[] newItems = new Item[newCapa];
        for (int i = 0; i < this.items.length; i++)
            newItems[i] = items[i];
        items = newItems;
    }

    public void addItemRandomPlayer(Item item){
        Random random = new Random();
        this.items[random.nextInt(this.items.length)] = item;
    }

    public int getCapacityInventory() {
        return this.slots;
    }

    public int getCurentLength() throws Exception{
        return getLastIndex();
    }

    public Item[] getItemsInventory() {
        return items;
    }

    public Item getItem(int i) {
        return items[i];
    }

    public boolean isEmptyInventory() {
        return items.length == 0;
    }

    public boolean isFullInventory() throws Exception {
        return getLastIndex() == slots;
    }

    private boolean canInserItem() throws Exception {
        return getLastIndex() <= items.length - 1;
    }


    private int getLastIndex() throws Exception{
        int index = -1;
        for (int i = 0; i < this.items.length; i++)
            if (this.items[i] == null) return i;
        return this.items.length;
    }
}
