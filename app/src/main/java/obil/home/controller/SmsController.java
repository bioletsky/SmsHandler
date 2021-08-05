package obil.home.controller;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;

import androidx.annotation.RequiresApi;

import obil.home.service.AudioService;
import obil.home.utils.FileUtils;

public class SmsController extends BroadcastReceiver {
    private static final String TAG = SmsController.class.getName();

    private static final String SOUND_DIR = "DCIM/HORROR/";

    private static final String STOP_SMS_TEXT = "s";


    private final AudioService audioService;

    public SmsController(AudioService audioService) {

        this.audioService = audioService;
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onReceive(Context context, Intent intent) {
        String smsText = "";
        for (SmsMessage smsMessage : Telephony.Sms.Intents.getMessagesFromIntent(intent)) {
            String smsSender = smsMessage.getDisplayOriginatingAddress();
            Log.i(TAG, "Message received form address " + smsSender);
            smsText += smsMessage.getMessageBody();
        }
        Log.i(TAG, "Received message text " + smsText);
        if (STOP_SMS_TEXT.equalsIgnoreCase(smsText)) {
            audioService.stop();
        } else {
            String file = FileUtils.getFilePathByFirstLetters(SOUND_DIR, smsText);
            if (file == null) {
                Log.i(TAG, "Media file not found");
                return;
            }
            audioService.play(smsText);
        }
    }
}
