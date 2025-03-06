package com.recieve;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsMessage;
import android.widget.Toast;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

/**
 * SmsReceiver är en BroadcastReceiver som lyssnar på inkommande SMS-meddelanden.
 * När ett SMS tas emot, extraherar den avsändare och meddelandeinnehåll och
 * skickar en lokal broadcast för att uppdatera användargränssnittet i Receive-aktiviteten.
 */
public class SmsReceiver extends BroadcastReceiver {

    /**
     * Denna metod anropas när ett SMS tas emot.
     * Den extraherar SMS-data (avsändare och meddelande), visar en Toast för att informera användaren
     * och skickar en lokal broadcast för att uppdatera UI i Receive-aktiviteten.
     *
     * @param context Kontextet där BroadcastReceiver körs.
     * @param intent Intent som innehåller SMS-data från systemet.
     */
    @Override
    public void onReceive(Context context, Intent intent) {
        //Extrahera SMS data från intent
        Bundle bundle = intent.getExtras();
        if (bundle != null) {
            Object[] pdus = (Object[]) bundle.get("pdus");
            String format = bundle.getString("format");
            SmsMessage[] messages = new SmsMessage[pdus.length];

            for (int i = 0; i < pdus.length; i++) {
                messages[i] = SmsMessage.createFromPdu((byte[]) pdus[i], format);
            }

            String sender = messages[0].getOriginatingAddress();
            String messageBody = messages[0].getMessageBody();

            //Visa SMS-meddelandet via en Toast
            Toast.makeText(context, "Från: " + sender + "\nMeddelande: " + messageBody, Toast.LENGTH_LONG).show();

            //Skicka en lokal broadcast för att uppdatera UI i Receive-aktiviteten
            Intent smsReceivedIntent = new Intent("com.recieve.SMS_RECEIVED");
            smsReceivedIntent.putExtra("sender", sender);
            smsReceivedIntent.putExtra("message", messageBody);

            //Skicka broadcasten via LocalBroadcastManager
            LocalBroadcastManager.getInstance(context).sendBroadcast(smsReceivedIntent);
        }
    }
}
