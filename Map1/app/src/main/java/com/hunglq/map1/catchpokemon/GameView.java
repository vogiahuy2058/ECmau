package com.hunglq.map1.catchpokemon;

import android.content.Context;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

public class GameView extends SurfaceView implements SurfaceHolder.Callback {

    GameThread gameThread;
    public static int downX = 0, downY = 0;
    public static int upX = 0, upY = 0;
    public static boolean isDragged = false;
    public static boolean isTouchDown = false;

    public GameView(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        SurfaceHolder holder = getHolder();
        holder.addCallback(this);
        setFocusable(true);
        gameThread = new GameThread(holder);
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if(!gameThread.isRunning()){
            gameThread = new GameThread(holder);
            gameThread.start();
        } else {
            gameThread.start();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if(gameThread.isRunning()){
            gameThread.setIsRunning(false);
            boolean retry = true;
            while (retry){
                try{
                    gameThread.join();
                    retry = false;
                } catch (InterruptedException e){

                }
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        int action = event.getAction();
        if(action == MotionEvent.ACTION_DOWN){
            if(GameEngine.gameState == 0)
                GameEngine.gameState = 1;
            isTouchDown = true;
            downX = (int) event.getX();
            downY = (int) event.getY();
        }
        if(action == MotionEvent.ACTION_UP){
            upX = (int) event.getX();
            upY = (int) event.getY();
            isDragged = true;
        }
        return true;
    }
}
