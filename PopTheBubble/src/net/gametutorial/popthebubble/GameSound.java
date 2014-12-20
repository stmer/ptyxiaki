package net.gametutorial.popthebubble;

import java.io.IOException;

import android.app.Activity;
import android.content.res.AssetFileDescriptor;
import android.content.res.AssetManager;
import android.media.AudioManager;
import android.media.SoundPool;

public class GameSound {
	SoundPool soundPool;
	int explosionId = -1;
	int popId = -1;
	//@SuppressWarnings("deprecation")
	
	public GameSound(Activity activity){
		activity.setVolumeControlStream(AudioManager.STREAM_MUSIC);
		soundPool = new SoundPool(20, AudioManager.STREAM_MUSIC, 0);
		try {
				AssetManager assetManager = activity.getAssets();
				AssetFileDescriptor descriptor = assetManager.openFd("laser.ogg");
				explosionId = soundPool.load(descriptor, 1);

				AssetFileDescriptor descriptor2 = assetManager.openFd("pop.ogg");
				popId = soundPool.load(descriptor2, 1);
				
			} catch(IOException e) {
				//textView.setText("Couldn't load sound effect from asset, "
					//	+ e.getMessage());
		}
	}
	
	public void playLaser() {
		if(explosionId != -1) {
			soundPool.play(explosionId, 1, 1, 0, 0, 1);
		}
	}
	
	public void playPop(){
		if(popId != -1) {
			soundPool.play(popId, 1, 1, 0, 0, 1);
		}
		
	}
}
