package net.gametutorial.popthebubble;

import android.graphics.Bitmap;
import android.graphics.Canvas;
/**
 * Bubble 
 * 
 * 
 */
public class Ball {
	public final static float BALL_MIN_RAD = 10f;
	public final static float BALL_MAX_RAD = 100f;
	public final static float BALL_SPEED = 30f;
	public final static float BLOW_UP_SPEED = 1.3F;
	
	// This are starting data.
	public static final float initSpeed = 5;
	public static final long initTimeBetweenBalls = 1000; // in milliseconds
	
	// This is current speed that will be increased and current time that will be decreased.
	public static float speed;
	public static long timeBetweenBalls; // in milliseconds
	
	public static long timeOfLastBall;
	
	
	// Needed for speeding up the game
	public static long timeBetweenSpeedups = 250; // in milliseconds
	public static long timeOfLastSpeedup;
	
	
	// Duck position on the screen.
	public float x;
	public float y;
	public float rad;
	
	
    /**
     * Ball Constructor
     * 
     * @param X X coordinate of the ball.
     * @param Y Y coordinate of the ball.
     */
	public Ball(float x, float y){
		
		this.x = x;
		this.y = y;
		
	}
	
	
	/**
	 * Blow up the Ball.
	 */
	public void update(){
		if (rad<BALL_MAX_RAD) 
			rad+=BLOW_UP_SPEED;
	}
	
	/**
	 * Draw the Ball to a screen.
	 * 
	 * @param canvas Canvas to draw on.
	 */
	public void draw(Canvas canvas){
		int size = (int) rad * 2;
		Bitmap resizedBitmap = Bitmap.createScaledBitmap(Game.ballImage, 
				size, size, false);
	    
		canvas.drawBitmap(resizedBitmap, x-rad, y-rad, null );
		
	}
	

	/**
	 * Checks if the ball was touched/shoot.
	 * 
	 * @param touchX X coordinate of the touch.
	 * @param touchY Y coordinate of the touch.
	 * 
	 * @return True if touch coordinates are in the coordinates of ball rectangle, false otherwise.
	 */
	public boolean wasItShoot(float touchX, float touchY){
		return 	(((touchX-x)*(touchX-x))+((touchY-y)*(touchY-y))) <= (rad*rad);
	}
	
	/**
	 * Checks if the ball is ready to blow up
	 * 
	 * @return True if ball reached maximum radius 
	 */
	public boolean readyToBlowUp(){
		
		return rad>=BALL_MAX_RAD;
	
	}

}
