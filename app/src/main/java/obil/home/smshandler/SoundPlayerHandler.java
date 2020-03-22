package obil.home.smshandler;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Build;
import android.provider.Telephony;
import android.telephony.SmsMessage;
import android.util.Log;

import androidx.annotation.RequiresApi;

public class SoundPlayerHandler extends BroadcastReceiver implements HeartBitListener {
    private static final String TAG = "SoundPlayerHandler";
    private static final int SILENT_RESOURCE = R.raw.silence;
    private static final String STOP_SMS_TEXT = "s";

    private MediaPlayer mediaPlayer = null;

    @Override
    public synchronized void heartBit(Context context) {
        Log.i(TAG, "Receiver get heartbit signal");
        if (mediaPlayer == null || !mediaPlayer.isPlaying()) {
            Log.i(TAG, "Receiver playing heartbit");
            playResourceId(context, SILENT_RESOURCE);
        }
    }

    private synchronized void stop() {
        if (mediaPlayer != null) {
            mediaPlayer.stop();
            mediaPlayer.release();
            mediaPlayer = null;
        }
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
        handleSmsText(context, smsText);
    }

    public void handleSmsText(Context context, String smsText) {
        if (STOP_SMS_TEXT.equalsIgnoreCase(smsText)) {
            stop();
            return;
        }

        String mediaFile = FileStorage.findMediaFileByFirstLetters(smsText);
        if (mediaFile == null) {
            Log.i(TAG, "Media file not found");
            return;
        }
        playFile(context, mediaFile);
    }

    private synchronized void playFile(Context context, String file) {
        stop();
        try {
            mediaPlayer = new MediaPlayer();
            mediaPlayer.setDataSource(file);
            mediaPlayer.prepare();
            mediaPlayer.start();
        } catch (Exception e) {
            Log.e(TAG, "Ошибка при проигрывании файла " + file);
        }
    }

    private synchronized void playResourceId(Context context, int resid) {
        stop();
        try {
            mediaPlayer = MediaPlayer.create(context, resid);
            mediaPlayer.start();
        } catch (Exception e) {
            Log.e(TAG, "Ошибка при проигрывании ресуура " + resid);
        }
    }
}
