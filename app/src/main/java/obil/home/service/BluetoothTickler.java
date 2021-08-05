package obil.home.service;

public interface BluetoothTickler {
    void run(AudioService audioService);
    void stop();
    void resetExecuteTime();
}
