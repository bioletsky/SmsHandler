package obil.home.service;

import android.media.MediaPlayer;
import android.util.Log;

import obil.home.controller.SmsController;

public class AudioServiceImpl implements AudioService {
    private static final String TAG = AudioServiceImpl.class.getName();

    private MediaPlayer mediaPlayer = new MediaPlayer();
    private BluetoothTickler bluetoothTickler;

    public AudioServiceImpl(BluetoothTickler bluetoothTickler) {

        this.bluetoothTickler = bluetoothTickler;

    }

    @Override
    public synchronized void stop() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.stop();
        }
    }

    @Override
    public synchronized void play(String file) {
        Log.i(TAG, "Play file: " + file);
        stop();
        try {
            bluetoothTickler.resetExecuteTime();
            mediaPlayer.reset();
            mediaPlayer.setDataSource(file);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (Exception e) {
            Log.e(TAG, "Error playing file: " + e.getMessage());
        }
    }

    @Override
    public synchronized void playIfStopped(String file) {
        if (!mediaPlayer.isPlaying()) {
            play(file);
        }
    }
}
