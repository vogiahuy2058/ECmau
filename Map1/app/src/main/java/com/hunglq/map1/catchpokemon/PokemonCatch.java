package com.hunglq.map1.catchpokemon;

import com.hunglq.map1.MainActivity;
import com.hunglq.map1.MapsActivity;
import com.hunglq.map1.model.Pokemon;

//Pokemon Object
public class PokemonCatch {

    Pokemon pokemon = MainActivity.pokemonList.get(MapsActivity.pokemonID - 1);
    private int pokemonX, pokemonY, pokemonSpeed, catchPoint;

    public PokemonCatch() {
        this.pokemonX = AppConstants.SCREEN_WIDTH / 2 -
                AppConstants.getBitmapBank().getPokemonWidth() / 2;
        this.pokemonY = AppConstants.SCREEN_HEIGHT / 2 -
                AppConstants.getBitmapBank().getPokemonHeight() + 100;
        this.pokemonSpeed = 40;
        this.catchPoint = pokemon.getCatchPoint();
    }

    public int getCatchPoint() {
        return catchPoint;
    }

    public void setCatchPoint(int catchPoint) {
        this.catchPoint = catchPoint;
    }

    public int getX() {
        return pokemonX;
    }

    public void setX(int pokemonX) {
        this.pokemonX = pokemonX;
    }

    public int getY() {
        return pokemonY;
    }

    public void setY(int pokemonY) {
        this.pokemonY = pokemonY;
    }

    public int getPokemonSpeed() {
        return pokemonSpeed;
    }

    public void setPokemonSpeed(int pokemonSpeed) {
        this.pokemonSpeed = pokemonSpeed;
    }
}
