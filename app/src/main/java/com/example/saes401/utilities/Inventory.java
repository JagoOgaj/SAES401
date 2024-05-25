package com.example.saes401.utilities;


import android.os.Parcel;
import android.os.Parcelable;

public class Inventory implements Parcelable {
    private Item[] items;
    private int slots;

    public Inventory(int slots){
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

    public void setItemsInventory(Item newItem, int i){
        items[i] = newItem;
    }

    public void setCapacityInventory(int newCapa){
        Item[] newItems = new Item[newCapa];
        for(int i = 0; i < getCurentLength(); i++)
            newItems[i] = items[i];
        items = newItems;
    }

    public int getCurentLength(){return items.length;}
    public Item[] getItemsInventory(){return items;}
    public Item getItem(int i){return items[i];}
    public boolean isEmptyInventory(){return items.length == 0;}
    public boolean isFullInventory(){return items.length == slots;}
}
