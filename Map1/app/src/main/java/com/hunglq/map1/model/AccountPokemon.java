package com.hunglq.map1.model;

import java.sql.Date;

public class AccountPokemon {
    String uid;
    int pokemonId;
    String date;
    String location;

    public AccountPokemon(String uid, int pokemonId, String date, String location) {
        this.uid = uid;
        this.pokemonId = pokemonId;
        this.date = date;
        this.location = location;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public int getPokemonId() {
        return pokemonId;
    }

    public void setPokemonId(int pokemonId) {
        this.pokemonId = pokemonId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getLocation() {
        try{
            String[] split = location.split(",");
            return split[1].trim() + ", " + split[2] + ", " + split[3] + ".";
        } catch (Exception ex){
            return location;
        }
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public String toString() {
        return pokemonId +" - " + getLocation();
    }
}
