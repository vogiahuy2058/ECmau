package com.hunglq.map1.model;

public class Account {
    String uid;
    String pwd;
    int coins;

    public Account(String uid, String pwd, int coins) {
        this.uid = uid;
        this.pwd = pwd;
        this.coins=coins;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getPwd() {
        return pwd;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public int getCoins() {
        return coins;
    }

    public void setCoins(int coins) {
        this.coins = coins;
    }
}

