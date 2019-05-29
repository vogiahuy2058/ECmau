package com.hunglq.map1.catchpokemon;

import android.graphics.Canvas;
import android.os.SystemClock;
import android.util.Log;
import android.view.SurfaceHolder;

//Use this thread run game
class GameThread extends Thread {

    SurfaceHolder surfaceHolder;
    boolean isRunning;
    long startTime, loopTime;
    long DELAY = 33;

    public GameThread(SurfaceHolder surfaceHolder) {
        this.surfaceHolder = surfaceHolder;
        isRunning = true;
    }

    @Override
    public void run(){
        while (isRunning){
            startTime = SystemClock.uptimeMillis();
            //locking the canvas
            Canvas canvas = surfaceHolder.lockCanvas();
            if(canvas != null){
                synchronized (surfaceHolder){
                    AppConstants.getGameEngine().updateAndDrawBackgroundImage(canvas);
                    AppConstants.getGameEngine().updateAndDrawPokemon(canvas);
                    AppConstants.getGameEngine().updateAndDrawBall(canvas);
                    //unlocking the canvas
                    surfaceHolder.unlockCanvasAndPost(canvas);
                }
            }
            //loopTime
            loopTime = SystemClock.uptimeMillis() - startTime;
            //Pausing here to make sure we update the right amount per second
            if (loopTime < DELAY){
                try{
                    Thread.sleep(DELAY - loopTime);
                } catch (InterruptedException e){
                    Log.e("Interrupted", "Interrupted while sleeping");
                }
            }
        }
    }

    //Return whether the thread is running
    public boolean isRunning(){
        return isRunning;
    }

    //Sets the thread state, false  = stopped, true = running
    public void setIsRunning(boolean state){
        isRunning = state;
    }

}
