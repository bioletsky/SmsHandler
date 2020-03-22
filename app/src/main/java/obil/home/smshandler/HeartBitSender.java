package obil.home.smshandler;

import android.content.Context;
import android.util.Log;

public class HeartBitSender implements Runnable {
    private static final String TAG = "HeartBitSender";

    private HeartBitListener heartBitListener;
    private final Context applicationContext;

    public HeartBitSender(HeartBitListener heartBitListener, Context applicationContext) {
        this.heartBitListener = heartBitListener;
        this.applicationContext = applicationContext;
    }

    @Override
    public void run() {
        while (!Thread.currentThread().isInterrupted()) {
            try {
                Thread.sleep(30000);
                heartBitListener.heartBit(applicationContext);
                Log.i(TAG, "HeartBit sent bit");
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
}
