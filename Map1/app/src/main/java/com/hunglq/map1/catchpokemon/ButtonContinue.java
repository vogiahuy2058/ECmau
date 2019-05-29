package com.hunglq.map1.catchpokemon;

//ButtonContinue Object
public class ButtonContinue {
    int backgroundImageX, backgroundImageY;

    public ButtonContinue(){
        backgroundImageX = AppConstants.SCREEN_WIDTH /2 - AppConstants.getBitmapBank().getBtnContinueWidth() / 2;
        backgroundImageY = AppConstants.SCREEN_HEIGHT * 3 / 4;
    }

    public int getX(){
        return backgroundImageX;
    }

    public int getY(){
        return backgroundImageY;
    }
}
