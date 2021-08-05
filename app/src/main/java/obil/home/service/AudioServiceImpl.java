package obil.home.service;

import android.media.MediaPlayer;
import android.util.Log;

import obil.home.controller.SmsController;

public class AudioServiceImpl implements AudioService {
    private static final String TAG = SmsController.class.getName();

    private MediaPlayer mediaPlayer =  new MediaPlayer();
    private BluetoothTickler bluetoothTickler;

    public AudioServiceImpl(BluetoothTickler bluetoothTickler) {

        this.bluetoothTickler = bluetoothTickler;

    }

    public void postConstruct() {
        bluetoothTickler.run(this);
    }

    @Override
    public synchronized void stop() {
        mediaPlayer.stop();
    }

    @Override
    public synchronized void play(String file) {
        stop();
        try {
            bluetoothTickler.resetExecuteTime();

            mediaPlayer.setDataSource(file);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (Exception e) {
            Log.e(TAG, "Ошибка при проигрывании файла " + file);
        }
    }

    @Override
    public synchronized void playIfStopped(String file) {
        if (!mediaPlayer.isPlaying()) {
            play(file);
        }
    }
}
