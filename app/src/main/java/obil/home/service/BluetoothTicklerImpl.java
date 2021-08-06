package obil.home.service;

import android.util.Log;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import obil.home.controller.SmsController;
import obil.home.utils.FileUtils;

public class BluetoothTicklerImpl implements BluetoothTickler {
    private static final String TAG = BluetoothTicklerImpl.class.getName();

    private static final String SOUND_DIR = "DCIM/HORROR/";
    private static final int DELAY = 10 * 60 * 1000;
    private static final String SILENT_FILE = "silence";

    private volatile boolean isInterrupted = false;
    private final ExecutorService executorService = Executors.newSingleThreadExecutor();
    private static String file;
    private volatile long executeTime;

    @Override
    public void run(AudioService audioService) {
        file = FileUtils.getFilePathByFirstLetters(SOUND_DIR, SILENT_FILE);
        resetExecuteTime();
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

    @Override
    public void resetExecuteTime() {
        executeTime = new Date().getTime() + DELAY;
        Log.i(TAG, "Set execute time " + new Date(executeTime));
    }
}
