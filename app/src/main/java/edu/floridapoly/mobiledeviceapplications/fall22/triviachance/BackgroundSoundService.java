package edu.floridapoly.mobiledeviceapplications.fall22.triviachance;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;

import androidx.annotation.Nullable;

public class BackgroundSoundService extends Service {
    public static MediaPlayer mediaPlayer;
    public static boolean isPlaying;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = MediaPlayer.create(this, R.raw.backgroundmusic);
        mediaPlayer.setLooping(true); // Set looping
        //mediaPlayer.setVolume(AudioManager.STREAM_MUSIC,AudioManager.STREAM_MUSIC);
        mediaPlayer.setVolume((float) 0.18,(float) 0.18);


    }

    public int onStartCommand(Intent intent, int flags, int startId) {
        mediaPlayer.start();
        isPlaying = true;
        return startId;
    }

    @Override
    public void onDestroy() {
        mediaPlayer.stop();
        isPlaying = false;
        mediaPlayer.release();
    }

    @Override
    public void onLowMemory() {
    }


}