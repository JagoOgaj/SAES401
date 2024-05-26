package com.example.saes401.entities;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.example.saes401.helper.GameConstant;
import com.example.saes401.utilities.Inventory;
import com.example.saes401.utilities.Item;

public class Player extends GameCharacter implements Parcelable {
    private Inventory inventory;
    private int HP;
    private int currentItem;

    public Player(int HP) {
        super(HP);
        this.HP = HP;
        this.inventory = new Inventory(GameConstant.DEFAULT_INVENTORY_SLOT);
        this.currentItem = 0;
    }

    protected Player(Parcel in) {
        super(in.readInt());
        HP = in.readInt();
        inventory = in.readParcelable(Inventory.class.getClassLoader());
    }

    public static final Creator<Player> CREATOR = new Creator<Player>() {
        @Override
        public Player createFromParcel(Parcel in) {
            return new Player(in);
        }

        @Override
        public Player[] newArray(int size) {
            return new Player[size];
        }
    };

    public void setInventory(Item item) throws Exception {
        this.inventory.setItemsInventory(item);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeInt(getHP());
        dest.writeInt(HP);
        dest.writeParcelable(inventory, flags);
    }

    private Inventory getInventory() {
        return inventory;
    }

    public boolean isFullinventory() {
        return inventory.isFullInventory();
    }

    public Item getItem() {
        return inventory.getItem(this.currentItem);
    }

    public int getHP() {
        return HP;
    }

    public void setHP(int HP) {
        this.HP = HP;
    }
}