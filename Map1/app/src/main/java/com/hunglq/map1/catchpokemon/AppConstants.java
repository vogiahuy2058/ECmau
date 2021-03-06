package com.hunglq.map1.catchpokemon;

import android.content.Context;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

//This class hold game enggine and bitmap bank properties
public class AppConstants {
    static BitmapBank bitmapBank;
    static GameEngine gameEngine;
    static int SCREEN_WIDTH, SCREEN_HEIGHT;
    static int gravity;
    static int VELOCITY_WHEN_JUMPED;
    public static Context gameActivityContext;

    public static void initialization (Context context){
        setScreenSize(context);
        bitmapBank = new BitmapBank(context.getResources(), context);
        gameEngine = new GameEngine();
        AppConstants.gravity = 3;
        AppConstants.VELOCITY_WHEN_JUMPED = -40;
    }

    //Return BitmapBank instance
    public static BitmapBank getBitmapBank(){
        return bitmapBank;
    }

    //Return GameEngine instance
    public static GameEngine getGameEngine(){
        return gameEngine;
    }

    private static void setScreenSize(Context context){
        WindowManager windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        Display display = windowManager.getDefaultDisplay();
        DisplayMetrics metrics = new DisplayMetrics();
        display.getMetrics(metrics);
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        AppConstants.SCREEN_WIDTH = width;
        AppConstants.SCREEN_HEIGHT = height;
    }
}
