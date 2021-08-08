package obil.home.service;

import android.util.Log;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import obil.home.utils.FileUtils;

public class BluetoothTicklerImpl implements BluetoothTickler {
    private static final String TAG = BluetoothTicklerImpl.class.getName();

    private static final String SOUND_DIR = "DCIM/HORROR/";
    private static final int DELAY = 10 * 60 * 1000;
    private static final String SILENT_FILE = "silence";

    private volatile boolean isInterrupted = false;
    private ExecutorService executorService;
    private String file;
    private long executeTime;
    private AudioService audioService;

    public BluetoothTicklerImpl(AudioService audioService) {
        this.audioService = audioService;
    }

    @Override
    public void run() {
        file = FileUtils.getFilePathByFirstLetters(SOUND_DIR, SILENT_FILE);
        if (file == null) {
            Log.e(TAG, "Silence file not found");
            return;
        }
        resetExecuteTime();
        executorService = Executors.newSingleThreadExecutor();
        executorService.execute(() -> {
            while (!isInterrupted) {
                if (new Date().getTime() > executeTime) {
                    Log.i(TAG, "Ready to trickle bluetooth");
                    resetExecuteTime();
                    audioService.playIfStopped(file);
                }
            }
        });
    }

    @Override
    public void stop() {
        isInterrupted = true;
        executorService.shutdown();
    }

    private void resetExecuteTime() {
        executeTime = new Date().getTime() + DELAY;
        Log.i(TAG, "Set execute time " + new Date(executeTime));
    }
}
