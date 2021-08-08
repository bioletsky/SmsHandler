package obil.home.application;

import android.content.BroadcastReceiver;

import obil.home.controller.SmsController;
import obil.home.service.AudioService;
import obil.home.service.AudioServiceImpl;
import obil.home.service.BluetoothTickler;
import obil.home.service.BluetoothTicklerImpl;

public class AppContainer {
    public AudioService audioService = new AudioServiceImpl();
    public BluetoothTickler bluetoothTickler = new BluetoothTicklerImpl(audioService);
    public BroadcastReceiver smsController = new SmsController(audioService);

    public void postPermission() {
        bluetoothTickler.run();
    }

    public void onDestroy() {
        bluetoothTickler.stop();
        audioService.stop();
    }
}
