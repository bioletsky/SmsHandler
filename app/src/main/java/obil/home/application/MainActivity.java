package obil.home.application;

import android.Manifest;
import android.app.Activity;
import android.app.Application;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Telephony;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import java.util.Arrays;

import obil.home.application.AppContainer;
import obil.home.application.ThisApplication;
import obil.home.smshandler.R;

public class MainActivity extends AppCompatActivity {

    //Permissions
    private final static int REQUEST_PERMISSION = 1;
    private final static String[] PERMISSIONS_STORAGE = {
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.RECEIVE_SMS
    };

    private static final String TAG = "MainActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        verifyPermissions(this, PERMISSIONS_STORAGE);
        AppContainer container = ((ThisApplication) this.getApplication()).container;
        container.postPermission();
        IntentFilter filter = new IntentFilter(Telephony.Sms.Intents.SMS_RECEIVED_ACTION);
        registerReceiver(container.smsController, filter);


      /* Button testButton = (Button) findViewById(R.id.button);
        testButton.setOnClickListener(v -> {
            soundPlayerHandler.handleSmsText(MainActivity.this, "m1");
        });*/
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ((ThisApplication) this.getApplication()).container.onDestroy();
    }

    private void verifyPermissions(Activity activity, String[] permissionsStorage) {
        Arrays.stream(permissionsStorage)
                .filter(p -> ActivityCompat.checkSelfPermission(activity, p) != PackageManager.PERMISSION_GRANTED)
                .forEach(p -> ActivityCompat.requestPermissions(activity, new String[]{p}, REQUEST_PERMISSION));
    }
}
