package com.hunglq.map1.model;

import java.io.Serializable;

public class Cart implements Serializable {
    String uid;
    int itemid;
    int amount;

    public Cart(String uid, int itemid, int amount) {
        this.uid = uid;
        this.itemid = itemid;
        this.amount = amount;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getItemid() {
        return itemid;
    }

    public void setItemid(int itemid) {
        this.itemid = itemid;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
