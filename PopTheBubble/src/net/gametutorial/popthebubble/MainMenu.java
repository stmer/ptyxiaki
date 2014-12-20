package net.gametutorial.popthebubble;

import net.gametutorial.popthebubble.R;
import android.os.Bundle;
import android.app.Activity;
import android.view.View;

/**
 * Main menu of the game.
 * 
 */

public class MainMenu extends Activity {
	
	private boolean showingMainMenu;
	private GamePanel gamePanel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        
        showingMainMenu = true;
    }

    // Start game on click
    public void onClickStartGame(View v){
    	showingMainMenu = false;
    	
    	// High score
        HighScore.ctx = this.getBaseContext();
        HighScore.loadHighScore();
        
        // Start and show game.
        gamePanel = new GamePanel(this, new GameSound(this));
    	setContentView(gamePanel);
    }
    
    @Override
    public void onBackPressed(){
    	if(!showingMainMenu){
    		showingMainMenu = true;
    		// Stop game loop
    		gamePanel.surfaceDestroyed(null);
    		setContentView(R.layout.activity_main_menu);
    	}else{
    		// Quit
    		super.onBackPressed();
    	}
    }
    


}
