package com.recieve;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import android.Manifest;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

/**
 * Receive är en Android-app som hanterar inkommande SMS.
 * Den tar emot SMS via en BroadcastReceiver och uppdaterar en TextView för att visa meddelandet.
 *
 * OBS! Denna är prövad på en Pixel 8 Pro API 35.
 */
public class Receive extends AppCompatActivity {

    private static final int REQUEST_SMS_PERMISSION = 200;
    private TextView incomingSms;

    /**
     * onCreate-metoden körs när aktiviteten startas. Denna metod sätter layouten
     * för aktiviteten och gör initialiseringar för UI-komponenterna.
     * Den registrerar även en BroadcastReceiver för att ta emot inkommande SMS och uppdaterar
     * användargränssnittet (TextView) med de mottagna meddelandena.
     *
     * @param savedInstanceState En Bundle som innehåller tidigare tillstånd för aktiviteten om appen återställs efter att ha stängts.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.receiver);

        incomingSms = findViewById(R.id.incomingSmsTextView);

        //Kontrollera om behörighet för att ta emot SMS är beviljad
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS) != PackageManager.PERMISSION_GRANTED) {
            //Be om behörigheten om den inte är beviljad
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.RECEIVE_SMS}, REQUEST_SMS_PERMISSION);
        }

        //Registrera en mottagare för lokal broadcasten
        LocalBroadcastManager.getInstance(this).registerReceiver(smsReceiver,
                new android.content.IntentFilter("com.recieve.SMS_RECEIVED"));
    }

    /**
     * onDestroy-metoden körs när aktiviteten förstörs eller tas bort från skärmen.
     * Denna metod används för att frigöra resurser och avregistrera eventuella
     * mottagare, såsom BroadcastReceiver, för att undvika minnesläckor.
     * Här avregistreras den lokala BroadcastReceiver som lyssnar på inkommande SMS.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        //Avregistrera när aktiviteten förstörs
        LocalBroadcastManager.getInstance(this).unregisterReceiver(smsReceiver);
    }

    /**
     * smsReceiver är en BroadcastReceiver som lyssnar på lokal broadcast när ett SMS tas emot.
     * När ett SMS tas emot, uppdateras TextView i aktivitets-UI:t.
     */
    private final BroadcastReceiver smsReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            String sender = intent.getStringExtra("sender");
            String message = intent.getStringExtra("message");

            //Uppdatera TextView med SMS
            incomingSms.setText("Från: " + sender + "\nMeddelande: " + message);
        }
    };

    /**
     * Hantera resultat från begäran om behörighet för att ta emot SMS.
     * Om användaren beviljar behörigheten, fortsätter appen att ta emot SMS.
     * Om användaren nekade behörigheten, visas ett meddelande.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_SMS_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Behörighet beviljad
            } else {
                //Behörighet nekad
                Toast.makeText(this, "Behörighet nekad. Appen kan inte ta emot SMS.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
