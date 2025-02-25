package com.send;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.Manifest;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

/**
 * Sender är en Android-app som gör det möjligt för användaren att skicka SMS.
 * Användaren kan skriva in ett telefonnummer och meddelande för att skicka ett SMS.
 * Denna app använder SmsManager för att skicka SMS.
 *
 * OBS! Denna app är prövad på en Pixel 8 Pro API 35.
 */
public class Sender extends AppCompatActivity {
    private static int REQUEST_SMS_PERMISSION = 200;
    private EditText phoneNumber, message;
    private Button sendButton;

    /**
     * onCreate-metoden körs när aktiviteten startas. Denna metod sätter layouten för aktiviteten,
     * initierar UI-komponenterna, kontrollerar om nödvändiga behörigheter för att skicka SMS är beviljade
     * och registrerar en onClickListener för att hantera SMS-sändning när knappen trycks.
     *
     * @param savedInstanceState En Bundle som innehåller tidigare tillstånd för aktiviteten om appen återställs efter att ha stängts.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.sender);

        phoneNumber = findViewById(R.id.phoneNumber);
        message = findViewById(R.id.message);
        sendButton = findViewById(R.id.sendButton);

        //Kontrollera behörigheter och be om dem om det behövs
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.SEND_SMS}, REQUEST_SMS_PERMISSION);
        }


        sendButton.setOnClickListener(v -> {
            String number = phoneNumber.getText().toString();
            String smsMessage = message.getText().toString();

            if (ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) == PackageManager.PERMISSION_GRANTED) {
                //Skicka SMS om behörigheten är beviljad
                SmsManager smsManager = SmsManager.getDefault();
                smsManager.sendTextMessage(number, null, smsMessage, null, null);
                Toast.makeText(this, "SMS skickat!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Behörighet inte beviljad!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Denna metod hanterar resultatet från begäran om behörighet. Om behörigheten beviljas,
     * kommer appen att kunna skicka SMS, annars kommer den att informera användaren om att behörigheten nekades.
     *
     * @param requestCode Begärkod som skickades när behörigheten begärdes.
     * @param permissions Lista över begärda behörigheter.
     * @param grantResults Resultat för varje begärd behörighet (beviljad eller nekad).
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_SMS_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Behörighet beviljad!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Behörighet inte beviljad. Appen kan inte skicka SMS.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
