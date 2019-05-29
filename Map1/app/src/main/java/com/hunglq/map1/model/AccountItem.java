package com.hunglq.map1.model;

import java.io.Serializable;

public class AccountItem implements Serializable {
    String uid;
    int itemId;
    int amount;

    public AccountItem(String uid, int itemId, int amount) {
        this.uid = uid;
        this.itemId = itemId;
        this.amount = amount;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getItemId() {
        return itemId;
    }

    public void setItemId(int itemId) {
        this.itemId = itemId;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }
}
