package com.hunglq.map1.catchpokemon;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.SystemClock;
import android.util.Log;

import com.hunglq.map1.MapsActivity;
import com.hunglq.map1.PokemonInfoActivity;

//Handler draw, collision, logic of game
public class GameEngine {

    BackgroundImage backgroundImage;
    Ball ball;
    PokemonCatch pokemon;
    ButtonContinue btnContinue;
    ButtonRun btnRun;
    static int gameState;
    int pokemonState;
    int ballState;
    int touchCount;
    Paint mPaint;
    static boolean catchResult = false;
    boolean isCatching = true;
    Thread timer;
    boolean isRunning = true;

    public GameEngine() {
        backgroundImage = new BackgroundImage();
        ball = new Ball();
        pokemon = new PokemonCatch();
        btnContinue = new ButtonContinue();
        btnRun = new ButtonRun();
        gameState = 0;
        pokemonState = 0;
        ballState = 0;
        touchCount = 0;
        mPaint = new Paint();
        mPaint.setColor(Color.WHITE);
        mPaint.setTextSize(200);
        mPaint.setTextAlign(Paint.Align.CENTER);
    }

    public void updateAndDrawBackgroundImage(Canvas canvas) {
        //Set background position
        backgroundImage.setX(backgroundImage.getX() - backgroundImage.getVelocity());
        //Draw background
        canvas.drawBitmap(AppConstants.getBitmapBank().getBackground(),
                backgroundImage.getX(), backgroundImage.getY(), null);
    }

    public void updateAndDrawBall(final Canvas canvas) {
        //State 0: Start game
        if (gameState == 0) {
            //Set ball position
            ball.setX(AppConstants.SCREEN_WIDTH / 2 - AppConstants.getBitmapBank().getBallWidth() / 2);
            ball.setY(AppConstants.SCREEN_HEIGHT * 3 / 4);
            //Draw ball
            canvas.drawBitmap(AppConstants.getBitmapBank().getBall(0), ball.getX(), ball.getY(), null);
            touchCount = 0;
        }
        //State 1: Catch pokemon
        if (gameState == 1) {
            //Handler event
            if (GameView.isDragged) {
                //Move the ball
                ball.setY(ball.getY() - (GameView.downY - GameView.upY) / 20);
                ball.setX(ball.getX() - (GameView.downX - GameView.upX) / 20);
            }
            //Collision between ball and pokemon
            if (pokemon.getX() < ball.getX() + AppConstants.getBitmapBank().getBallWidth() / 2
                    && ball.getX() + AppConstants.getBitmapBank().getBallWidth() / 2 < pokemon.getX() + AppConstants.getBitmapBank().getPokemonWidth()
                    && pokemon.getY() + AppConstants.getBitmapBank().getPokemonHeight() / 2 < ball.getY() + AppConstants.getBitmapBank().getBallHeight() / 2
                    && ball.getY() + AppConstants.getBitmapBank().getBallHeight() / 2 < pokemon.getY() + AppConstants.getBitmapBank().getPokemonHeight()
            ) {
                //Go to state 2
                GameView.downX = GameView.downY = 0;
                isRunning = true;
                isCatching = true;
                gameState = 2;
            }
            //Condition to drop the ball
            if (ball.getY() <= 0
                    || ball.getX() <= 0
                    || ball.getX() >= AppConstants.SCREEN_WIDTH - AppConstants.getBitmapBank().getBallWidth()) {
                ballState = 1;
                GameView.isDragged = false;
            }
            //Drop the ball
            if (ballState == 1) {
                ball.setVelocity(50);
                ball.setY(ball.getY() + ball.getVelocity());
            }
            //Go back to state 0 if dont have collision
            if (ball.getY() > AppConstants.SCREEN_HEIGHT * 3 / 4) {
                ball.setY(AppConstants.SCREEN_HEIGHT * 3 / 4);
                ball.setX(AppConstants.SCREEN_WIDTH / 2 - AppConstants.getBitmapBank().getBallWidth() / 2);
                gameState = 0;
                ballState = 0;
            }
            canvas.drawBitmap(AppConstants.getBitmapBank().getBall(0), ball.getX(), ball.getY(), null);
        }

        //State 2: Catch result (Touch the ball in 3 seconds to catch pokemon)
        if (gameState == 2) {
            //Draw 9 frames of the ball
            int currentFrame = ball.getCurrentFrame();
            ball.setX(AppConstants.SCREEN_WIDTH / 2 - AppConstants.getBitmapBank().getBall(currentFrame).getWidth() / 2);
            ball.setY(AppConstants.SCREEN_HEIGHT / 2 - AppConstants.getBitmapBank().getBall(currentFrame).getHeight() / 2);
            canvas.drawBitmap(AppConstants.getBitmapBank().getBall(currentFrame), ball.getX(), ball.getY(), null);
            if (isCatching)
                currentFrame++;
            SystemClock.sleep(20);
            // If it exceeds maxframe re-initialize to 0
            if (currentFrame > ball.maxFrame) {
                currentFrame = 0;
            }
            ball.setCurrentFrame(currentFrame);

            //Draw touch point
            canvas.drawText(touchCount + "",
                    AppConstants.SCREEN_WIDTH / 2,
                    AppConstants.SCREEN_HEIGHT / 2 - AppConstants.getBitmapBank().getBallHeight(),
                    mPaint);
            //Handler touch the ball event
            if (ball.getX() < GameView.downX && GameView.downX < ball.getX() + AppConstants.getBitmapBank().getBallWidth()
                    && ball.getY() < GameView.downY && GameView.downY < ball.getY() + AppConstants.getBitmapBank().getBallHeight()
                    && isCatching
                    && GameView.isTouchDown) {
                touchCount++;
                GameView.isTouchDown = false;
            }

            //Show result after 3 seconds
            if (!isCatching) {
                //Get enough catch point => Result: Gotcha!
                if (catchResult) {
                    canvas.drawText("Gotcha!",
                            AppConstants.SCREEN_WIDTH / 2,
                            AppConstants.SCREEN_HEIGHT / 2 + AppConstants.getBitmapBank().getBallHeight() * 3 / 2,
                            mPaint);
                    //Handler button continue event
                    if (btnContinue.getX() < GameView.downX && GameView.downX < btnContinue.getX() + AppConstants.getBitmapBank().getBtnContinueWidth()
                            && btnContinue.getY() < GameView.downY && GameView.downY < btnContinue.getY() + AppConstants.getBitmapBank().getBtnContinueHeight()
                            && GameView.isTouchDown) {
                        Log.e("Touch", "Continue");
                        GameView.isTouchDown = false;
                        //Close game activity => Open pokemon info activity
                        MapsActivity.isGocha = true;
                        Activity activity = (Activity) AppConstants.gameActivityContext;
                        Intent intent = new Intent(activity, PokemonInfoActivity.class);
                        activity.startActivity(intent);
                        activity.finish();
                        SystemClock.sleep(500);
                        gameState = 0;
                    }
                }
                //Get not enough catch point => Result: Fail!
                else {
                    //Draw result
                    canvas.drawText("Fail!",
                            AppConstants.SCREEN_WIDTH / 2,
                            AppConstants.SCREEN_HEIGHT / 2 + AppConstants.getBitmapBank().getBallHeight() * 3 / 2,
                            mPaint);
                    //Handler button continue event => Go back state 0
                    if (btnContinue.getX() < GameView.downX && GameView.downX < btnContinue.getX() + AppConstants.getBitmapBank().getBtnContinueWidth()
                            && btnContinue.getY() < GameView.downY && GameView.downY < btnContinue.getY() + AppConstants.getBitmapBank().getBtnContinueHeight()
                            && GameView.isTouchDown) {
                        GameView.isTouchDown = false;
                        gameState = 0;
                    }
                }
                //Draw button
                canvas.drawBitmap(AppConstants.getBitmapBank().getBtnContinue(), btnContinue.getX(),
                        btnContinue.getY(), null);
            }

            //Delay 3 seconds and get result
            timer = new Thread(new Runnable() {
                @Override
                public void run() {
                    while (isRunning){
                        SystemClock.sleep(3000);
                        isCatching = false;
                        if (touchCount < pokemon.getCatchPoint()) {
                            catchResult = false;
                        } else catchResult = true;
                        isRunning = false;
                    }
                }
            });
            timer.start();
        }
    }

    public void updateAndDrawPokemon(Canvas canvas) {
        //In state 0 and 1: pokemon move left and right
        if (gameState == 1 || gameState == 0) {
            if (pokemon.getX() <= 0) {
                pokemon.setX(pokemon.getX() + pokemon.getPokemonSpeed());
                pokemonState = 1;
            } else if (pokemon.getX() > 0 && pokemon.getX() < AppConstants.SCREEN_WIDTH -
                    AppConstants.getBitmapBank().getPokemonWidth()) {
                switch (pokemonState) {
                    case 1:
                        pokemon.setX(pokemon.getX() + pokemon.getPokemonSpeed());
                        break;
                    case 0:
                        pokemon.setX(pokemon.getX() - pokemon.getPokemonSpeed());
                        break;
                }
            } else {
                pokemon.setX(pokemon.getX() - pokemon.getPokemonSpeed());
                pokemonState = 0;
            }

            //Draw btnRun
            canvas.drawBitmap(AppConstants.getBitmapBank().getBtnRun(), btnRun.getX(), btnRun.getY(), null);
            //Handler btnRun click => Close activity
            if (btnRun.getX() < GameView.downX && GameView.downX < btnRun.getX() + AppConstants.getBitmapBank().getBtnRunWidth()
                    && btnRun.getY() < GameView.downY && GameView.downY < btnRun.getY() + AppConstants.getBitmapBank().getBtnRunHeight()
                    && GameView.isTouchDown) {
                GameView.isTouchDown = false;
                Activity activity = (Activity) AppConstants.gameActivityContext;
                activity.finish();
            }
        }
        //In sate 2: Hide the pokemon
        if (gameState == 2) {
            pokemon.setX(AppConstants.SCREEN_WIDTH * 2);
        }
        canvas.drawBitmap(AppConstants.getBitmapBank().getPokemon(), pokemon.getX(), pokemon.getY(), null);
    }
}
