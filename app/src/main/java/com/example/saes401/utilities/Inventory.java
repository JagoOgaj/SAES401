package com.example.saes401.utilities;


public class Inventory {
    private Item[] items;

    public Inventory(int slots){
        items = new Item[slots];
    }

    private Item setItemsInventory(int i, Item newItem){
        Item item = items[i];
        items[i] = newItem;
        return item;
    }



    private Item[] getItemsInventory(){
        return items;
    }
    private boolean isEmptyInventory(){
        return items.length == 0;
    }
}
