package com.example.saes401.utilities;


public class Inventory {
    private Item[] items;
    private int slots;

    public Inventory(int slots){
        items = new Item[slots];
        this.slots = slots;
    }

    public Item setItemsInventory(int i, Item newItem){
        Item item = items[i];
        items[i] = newItem;
        return item;
    }

    public void setCapacityInventory(int newCapa){
        Item[] newItems = new Item[newCapa];
        for(int i = 0; i < getCurentLength(); i++)
            newItems[i] = items[i];
        items = newItems;
    }

    public int getCurentLength(){return items.length;}
    public Item[] getItemsInventory(){return items;}
    public boolean isEmptyInventory(){return items.length == 0;}
    public boolean isFullInventory(){return items.length == slots;}
}
