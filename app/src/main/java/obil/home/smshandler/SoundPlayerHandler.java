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

import java.lang.reflect.Field;

public class SoundPlayerHandler extends BroadcastReceiver implements HeartBitListener {
    private static final String TAG = "SoundPlayerHandler";
    private static final String PLAY_AUDIO_MONSTER = "pam";
    private static final String PLAY_STOP = "s";
    private static final String PLAY_PIG_EATING = "pea";
    private static final String PLAY_PIG_TWO = "pi2";
    private static final String PLAY_SOVA_1 = "sv1";
    private static final String PLAY_SOVA_2 = "sv2";
    private static final String PLAY_WOLFS = "wolf";
    private static final String PLAY_SHELEST = "she";


    private MediaPlayer audio = null;


    public SoundPlayerHandler() {
        Log.i(TAG, "xxxxxxxxxx");
    }


    @Override
    public synchronized void heartBit(Context applicationContext) {
        if (audio == null || !audio.isPlaying()) {
            Log.i(TAG, "Receiver playing heartbit");
            play(applicationContext, R.raw.silence);
        } else {
            Log.i(TAG, "Receiver get heartbit signal but music is playing");
        }
    }

    private synchronized void stop() {
        if (audio != null) {
            audio.stop();
            audio.reset();
            audio = null;
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals(Telephony.Sms.Intents.SMS_RECEIVED_ACTION)) {
            String smsSender = "";
            String smsBody = "";
            for (SmsMessage smsMessage : Telephony.Sms.Intents.getMessagesFromIntent(intent)) {
                smsSender = smsMessage.getDisplayOriginatingAddress();
                smsBody += smsMessage.getMessageBody();
            }

            if (PLAY_STOP.equalsIgnoreCase(smsBody)) {
                stop();
            } else {
                Field[] fields = R.raw.class.getFields();
                for (int i = 0; i < fields.length - 1; i++) {
                    String name = fields[i].getName();
                    //Log.i(TAG, "name " + fields[i].getName());
                    try {
                        if (name.equalsIgnoreCase(smsBody)) {
                            //Log.i(TAG, "Play " + fields[i].getName());
                            play(context, fields[i].getInt(null));
                        }
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
            }

      /*      switch (smsBody.toLowerCase()) {
                case PLAY_STOP:
                    stop();
                    break;
                case PLAY_AUDIO_MONSTER:
                    play(context, R.raw.audio);
                    break;
                case PLAY_PIG_EATING:
                    play(context, R.raw.pig_eating);
                    break;
                case PLAY_PIG_TWO:
                    play(context, R.raw.pigs2);
                    break;
                case PLAY_SOVA_1:
                    play(context, R.raw.sova1);
                    break;
                case PLAY_SOVA_2:
                    play(context, R.raw.sova2);
                    break;
                case PLAY_SHELEST:
                    play(context, R.raw.shelest);
                    break;
                case PLAY_WOLFS:
                    play(context, R.raw.wolves);
                    break;
            }
      */
            Log.i(TAG, smsBody);
        }
    }

    private synchronized void play(Context context, int audioId) {
        stop();
        audio = MediaPlayer.create(context, audioId);
        audio.start();
    }
}
