package obil.home.service;

import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import obil.home.utils.FileUtils;

public class BluetoothTicklerImpl implements BluetoothTickler {

    private static final String SOUND_DIR = "DCIM/HORROR/";
    private static final int DELAY = 10 * 60 * 1000;
    private static final String SILENT_FILE = "silence";

    private volatile boolean isInterrupted = false;
    private final ExecutorService executorService;
    private static String file;
    private volatile long executeTime;


    public BluetoothTicklerImpl() {
        executorService = Executors.newSingleThreadExecutor();
        file = FileUtils.getFilePathByFirstLetters(SOUND_DIR, SILENT_FILE);
        resetExecuteTime();
    }

    @Override
    public void run(AudioService audioService) {
        executorService.execute(() -> {
            while (!isInterrupted) {
                if (new Date().getTime() > executeTime) {
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
    }
}
