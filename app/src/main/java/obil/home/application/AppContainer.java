package obil.home.application;

import android.content.BroadcastReceiver;

import obil.home.controller.SmsController;
import obil.home.service.AudioService;
import obil.home.service.AudioServiceImpl;
import obil.home.service.BluetoothTickler;
import obil.home.service.BluetoothTicklerImpl;

public class AppContainer {
    public BluetoothTickler bluetoothTickler = new BluetoothTicklerImpl();
    public AudioService audioService = new AudioServiceImpl(bluetoothTickler);
    public BroadcastReceiver smsController = new SmsController(audioService);

    public void postPermission() {
        bluetoothTickler.run(audioService);
    }

    public void onDestroy() {
        bluetoothTickler.stop();
        audioService.stop();
    }
}
