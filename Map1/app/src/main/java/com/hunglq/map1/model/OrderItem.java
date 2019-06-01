package com.hunglq.map1.model;

import java.io.Serializable;

public class OrderItem implements Serializable {
    int itemId;
    int amount;
    int orderId;

    public OrderItem(int orderId, int itemId, int amount) {
        this.itemId = itemId;
        this.amount = amount;
        this.orderId = orderId;
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

    public int getOrderId() {
        return orderId;
    }

    public void setOrderId(int orderId) {
        this.orderId = orderId;
    }
}
