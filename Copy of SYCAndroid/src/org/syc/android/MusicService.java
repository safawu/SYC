package org.syc.android;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;
import android.widget.Toast;

public class MusicService extends Service {

	private static final String TAG = "MusicService";
	private MediaPlayer player;
	private String filepath = Environment.getExternalStorageDirectory().getPath() +"/"+"Bach.mid";
	MusicBinder musicBinder = new MusicBinder();
	private int length = 0;
	
	public MusicService(){}
	
	@Override
	public IBinder onBind(Intent arg0) {
		Log.d(TAG, "Music Binder Created");
		return musicBinder;
	}
	
	@Override  
    public boolean onUnbind(Intent intent) {  
        Log.e(TAG, "Music Binder Destroyed");  
        return super.onUnbind(intent);  
    }  
	
	public void onCreate(){
		super.onCreate();
		player = new MediaPlayer();
		try {
			player.setDataSource(filepath);
			player.prepare();
			player.setLooping(false); 
			player.start();
			Log.d(TAG, "Music Service Created");
		} catch (Exception e) {
			Toast.makeText(this, "Music Service Failed to start", Toast.LENGTH_SHORT).show(); 
			e.printStackTrace();
		}

	}
	
	/*
	@Override
	public int onStartCommand (Intent intent, int flags, int startId){
         player.start();
         return START_STICKY;
	}
	
	@Override
	public void onStart(Intent intent, int startid){
		player.start();
	}*/
	
	@Override
	public void onDestroy(){
		super.onDestroy();
		player.stop();
	    player.release();
		Log.d(TAG, "Music Service Destroyed");
	}
	
	public void onStop(){
	    player.stop();
	    player.release();
	}

	public void pauseMusic(){
		if(player.isPlaying()){
			player.pause();
			length = player.getCurrentPosition();
		}	    
	}
	
	public void playMusic(){
		if(!player.isPlaying()){
			player.seekTo(length);
			player.start();
		}
	}
	
	public class MusicBinder extends Binder{  
		MusicService getService()  
        {  
            return MusicService.this;  
        }  
    }
	

}
