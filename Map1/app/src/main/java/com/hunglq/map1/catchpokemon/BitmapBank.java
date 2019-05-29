package com.hunglq.map1.catchpokemon;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.hunglq.map1.MapsActivity;
import com.hunglq.map1.R;

import java.io.IOException;
import java.io.InputStream;

//Hold bitmap properties of all object
public class BitmapBank {
    Bitmap background;
    Bitmap[] ball;
    Bitmap pokemon;
    Bitmap btnContinue;
    Bitmap btnRun;

    public BitmapBank(Resources resources, Context context){
        background = BitmapFactory.decodeResource(resources, R.drawable.background);
        background = scaleImage(background);
        ball = new Bitmap[9];
        ball[0] = BitmapFactory.decodeResource(resources, R.drawable.ball);
        ball[1] = BitmapFactory.decodeResource(resources, R.drawable.ball1);
        ball[2] = BitmapFactory.decodeResource(resources, R.drawable.ball2);
        ball[3] = BitmapFactory.decodeResource(resources, R.drawable.ball3);
        ball[4] = BitmapFactory.decodeResource(resources, R.drawable.ball4);
        ball[5] = BitmapFactory.decodeResource(resources, R.drawable.ball5);
        ball[6] = BitmapFactory.decodeResource(resources, R.drawable.ball6);
        ball[7] = BitmapFactory.decodeResource(resources, R.drawable.ball7);
        ball[8] = BitmapFactory.decodeResource(resources, R.drawable.ball8);
        //pokemon = BitmapFactory.decodeResource(resources, R.drawable.poke001);
        btnContinue = BitmapFactory.decodeResource(resources, R.drawable.continue1);
        btnRun = BitmapFactory.decodeResource(resources, R.drawable.run);

        InputStream inputStream = null;
        try {
            inputStream = context.getAssets().open("images/pokemon/"+ MapsActivity.pokemonID +".png");
        } catch (IOException e) {
            e.printStackTrace();
        }
        Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
        pokemon = Bitmap.createScaledBitmap(bitmap, 500, 500, false);
    }

    public Bitmap getBtnRun(){return btnRun;}

    public int getBtnRunWidth(){return btnRun.getWidth();}

    public int getBtnRunHeight(){return btnRun.getHeight();}

    public Bitmap getBtnContinue(){return btnContinue;}

    public int getBtnContinueWidth(){ return btnContinue.getWidth();}

    public int getBtnContinueHeight(){ return btnContinue.getHeight();}

    public Bitmap getPokemon(){ return pokemon;}

    public int getPokemonWidth(){return pokemon.getWidth();}

    public int getPokemonHeight(){return pokemon.getHeight();}

    public Bitmap getBall(int frame){
        return ball[frame];
    }

    public int getBallWidth(){
        return ball[0].getWidth();
    }

    public int getBallHeight(){
        return ball[0].getHeight();
    }

    public Bitmap getBackground() {
        return background;
    }

    public int getBackgroundWidth(){
        return background.getWidth();
    }

    public int getBackgroundHeight(){
        return background.getHeight();
    }

    public Bitmap scaleImage(Bitmap bitmap){
        float widthHeightRatio = getBackgroundHeight()/getBackgroundWidth();
        int backgroundScaleWidth = (int) widthHeightRatio * AppConstants.SCREEN_HEIGHT;
        Log.i("Check", backgroundScaleWidth + " - " + AppConstants.SCREEN_HEIGHT);
        return Bitmap.createScaledBitmap( bitmap, backgroundScaleWidth, AppConstants.SCREEN_HEIGHT, false);

    }

}
