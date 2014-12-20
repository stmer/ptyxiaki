package net.gametutorial.popthebubble;

import java.util.ArrayList;
import java.util.Random;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.MotionEvent;

/**
 * Actual game.
 * 
 */

public class Game {
	
	// Screen info
	public static int screenWidth;
	public static int screenHeight;
	public static float screenDensity;
	
	private boolean gameOver;
	
	// We will need this to draw background image full screen.
	private Rect destBackgroundImage;
	private Paint paintForImages;
	
	// We need this for the grass, so that it will be resized to the width of a screen when drawn.
	
	// Images
	private static Bitmap backgroundImage;
	public static Bitmap ballImage;
	
	// List of all ducks on a screen.
	private ArrayList<Ball> allBalls;
	
	// How many ducks were killed?
	private int ballsKilled;
	
	// Color and size for text.
	private Paint paintText;
	private int textSize;
	
	// Needed for new random coordinates.
	private Random random = new Random();
	
	// Position of text for restarting the game.
	private float textForRestart_x;
	private float textForRestart_y;
	private float backGround_x;
	private float backGroundVelosity=0.5f;
	
	private GameSound gamesound;
	
	public Game(int screenWidth, int screenHeight, Resources resources, GameSound gamesound){
		Game.screenWidth = screenWidth;
		Game.screenHeight = screenHeight;
		Game.screenDensity = resources.getDisplayMetrics().density;
		
		this.LoadContent(resources);
		
		destBackgroundImage = new Rect(0, 0, screenWidth, screenHeight);
		
		paintForImages = new Paint();
		paintForImages.setFilterBitmap(true);
		
		allBalls = new ArrayList<Ball>();
		
		textSize = 25;
		paintText = new Paint();
		paintText.setColor(Color.BLACK);
		paintText.setTextSize(textSize * Game.screenDensity);

		textForRestart_x = Game.screenWidth/2 - 95 * Game.screenDensity;
		textForRestart_y = Game.screenHeight / 2 - 20;
		this.gamesound = gamesound;
		
		this.ResetGame();
	}
	

	/**
	 * Load files.
	 */
	private void LoadContent(Resources resources){

		backgroundImage = BitmapFactory.decodeResource(resources, R.drawable.bg);
		ballImage = BitmapFactory.decodeResource(resources, R.drawable.bubble);
		
	}

	
	/**
	 * For (re)setting some game variables before game can start.
	 */
	private void ResetGame(){
		gameOver = false;
		
		allBalls.clear();
		
		Ball.speed = Ball.initSpeed;
		Ball.timeBetweenBalls = Ball.initTimeBetweenBalls;
		Ball.timeOfLastBall = 0;
		
		Ball.timeOfLastSpeedup = 0;
		
		ballsKilled = 0;
		
		// We create some starting balls.
		this.addNewBall();
		this.addNewBall();
	}
	
	
	/**
	 * Game update method.
	 * 
	 * @param gameTime Elapsed game time in milliseconds.
	 */
	public void Update(long gameTime) {
		if(gameOver){
			return;
		}
		
		// Create new ball, if time.
		if( (gameTime - Ball.timeOfLastBall) > Ball.timeBetweenBalls ){
			Ball.timeOfLastBall = gameTime;
			
			this.addNewBall();
		}
		
		// Update balls
		for(int i=0; i < allBalls.size(); i++){
			Ball ball = allBalls.get(i);
			
			ball.update();
			
			// Check if any ball got away and if did end game.
			if (ball.readyToBlowUp()){
				gameOver = true;
				
				if(ballsKilled > HighScore.highScore){
					// New high score
					HighScore.highScore = ballsKilled;
					// Save new high score to file.
					HighScore.saveHighScore();
				}
			}
		}
		
		// Speedup the game, if time
		if( (gameTime - Ball.timeOfLastSpeedup) > Ball.timeBetweenSpeedups ){
			Ball.timeOfLastSpeedup = gameTime;
			
			Ball.speed += 0.03;
			if(Ball.timeBetweenBalls > (0.5 * 1000))
				Ball.timeBetweenBalls -= 90;
		}
	}
	
	private Rect getBackGoundImage(){
		backGround_x+=backGroundVelosity;
		if (backGround_x+Game.screenWidth>Game.backgroundImage.getWidth())  {
			backGroundVelosity=-0.5f;
			backGround_x+=backGroundVelosity;
		}
		if (backGround_x<=0){
			backGroundVelosity=0.5f;
			backGround_x+=backGroundVelosity;
		}
		return new Rect((int) backGround_x, 0, Game.screenWidth + (int) backGround_x, 
				Game.backgroundImage.getHeight());
	}
	/**
	 * Draw the game to the screen.
	 * 
	 * @param canvas Canvas on which we will draw.
	 */
	public void Draw(Canvas canvas) {
		// First we need to erase everything we draw before.
		canvas.drawColor(Color.BLACK);
		/*
		Paint paint = new Paint();
		Shader shader = new LinearGradient(0, 0,0, Game.screenHeight, Color.rgb(137, 211, 235), Color.rgb(225, 242, 250) , Shader.TileMode.REPEAT);
		paint.setShader(shader);
		canvas.drawRect(0, 0, Game.screenWidth, Game.screenHeight, paint);
		*/
		// Draw background image.
		canvas.drawBitmap(Game.backgroundImage, getBackGoundImage(), 
				this.destBackgroundImage, this.paintForImages);
		
		// Draw balls
		for(int i=0; i < allBalls.size(); i++){
			allBalls.get(i).draw(canvas);
		}
		
		// Draw grass
		
		// Draw how many ducks was killed.
		canvas.drawText("Score: "      + Integer.toString(this.ballsKilled), 8.0f, 25.0f * Game.screenDensity, paintText);
		canvas.drawText("High score: " + Integer.toString(HighScore.highScore),  8.0f, textSize * 2 * Game.screenDensity, paintText);
		
		if(gameOver){
			canvas.drawText("Game over", Game.screenWidth/2 - 65 * Game.screenDensity, Game.screenHeight / 3, paintText);
			canvas.drawText("Touch to restart", textForRestart_x, textForRestart_y, paintText);
		}
	}
	
	
    /**
     * When touch on screen is detected.
     * 
     * @param event MotionEvent
     */
    public void touchEvent_actionDown(MotionEvent event){
    	if(!gameOver){
    		this.checkIfAnyBallShooted(event.getX(), event.getY());
    	} else {
    		// You can check if this coordinates are correct by drawing rectangle with this coordinates.
    		// They are not exactly over the text but it's close enough :)
    		if(event.getX() > textForRestart_x && event.getX() < textForRestart_x + 280 &&
    		   event.getY() > textForRestart_y - 50 && event.getY() < textForRestart_y + 50){
    			this.ResetGame();
    		}
    	}
    }
    
    /**
     * When moving on screen is detected.
     * 
     * @param event MotionEvent
     */
    public void touchEvent_actionMove(MotionEvent event){
    	
    }
    
    /**
     * When touch on screen is released.
     * 
     * @param event MotionEvent
     */
    public void touchEvent_actionUp(MotionEvent event){
    	
    }
    
    
    /**
     * Checks if there is any duck on given coordinates.
     * If any ball was shoot it is removed from the array list.
     * 
     * @param touchX X coordinate of the touch.
     * @param touchY Y coordinate of the touch.
     */
    private void checkIfAnyBallShooted(float touchX, float touchY){
    	for(int i=0; i < allBalls.size(); i++){
    		Ball ball = allBalls.get(i);
    		
    		if(ball.wasItShoot((int)touchX, (int)touchY)){
    			allBalls.remove(i);
    			ballsKilled++;
    			gamesound.playPop();
    		}
    	}
    }
    

    /**
     * Creates a new ball and add it on array list.
     */
    private void addNewBall(){
    	
    	Ball newBall = new Ball((float) random.nextInt(Game.screenWidth), 
    			(float) random.nextInt(Game.screenHeight));
    	
    	this.allBalls.add(newBall);
    }
    
}
