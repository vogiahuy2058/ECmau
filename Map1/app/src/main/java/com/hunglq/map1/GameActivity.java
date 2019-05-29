package com.hunglq.map1;

import android.app.Activity;
import android.os.Bundle;

import com.hunglq.map1.catchpokemon.AppConstants;
import com.hunglq.map1.catchpokemon.GameView;

public class GameActivity extends Activity {

    GameView gameView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        AppConstants.gameActivityContext = this;
        gameView = new GameView(this);
        setContentView(gameView);
    }
}
