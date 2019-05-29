package com.hunglq.map1.catchpokemon;

//Ball object
public class Ball {
    private int ballX, ballY, velocity, currentFrame;
    public static int maxFrame;

    public Ball() {
        ballX = AppConstants.SCREEN_WIDTH / 2 - AppConstants.getBitmapBank().getBallWidth() / 2;
        ballY = AppConstants.SCREEN_HEIGHT * 3 / 4;
        velocity = 0;
        maxFrame = 8;
    }

    public int getCurrentFrame() {
        return currentFrame;
    }

    public void setCurrentFrame(int currentFrame) {
        this.currentFrame = currentFrame;
    }

    public int getVelocity() {
        return velocity;
    }

    public void setVelocity(int velocity) {
        this.velocity = velocity;
    }

    public int getX() {
        return ballX;
    }

    public int getY() {
        return ballY;
    }

    public void setX(int ballX) {
        this.ballX = ballX;
    }

    public void setY(int ballY) {
        this.ballY = ballY;
    }

}
