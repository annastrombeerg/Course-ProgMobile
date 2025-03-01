package com.ledmessage;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

/**
 * LedMessage är en Android-app som hanterar LED-notifikationer.
 * Användaren kan trycka på en knapp för att aktivera en notifikation
 * som triggar LED-ljus (om enheten stöder det).
 *
 * OBS! Denna är prövad på en Pixel 8 Pro API 35.
 */
public class LedMessage extends AppCompatActivity {
    private static final int REQUEST_PERMISSION = 200;
    private static final String CHANNEL_ID = "led_notification_channel";
    private static final int NOTIFICATION_ID = 1;

    /**
     * onCreate-metoden körs när aktiviteten startas. Denna metod sätter layouten
     * för aktiviteten och gör initialiseringar för UI-komponenterna.
     * Den hanterar även tillståndskontroll och skapar en notifikationskanal.
     *
     * @param savedInstanceState En Bundle som innehåller tidigare tillstånd för aktiviteten om appen återställs efter att ha stängts.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ledmessage);

        Button notifyButton = findViewById(R.id.notifyButton);

        //Kontrollera om notifikationstillstånd är beviljat, om inte begär tillstånd
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.POST_NOTIFICATIONS},
                    REQUEST_PERMISSION);
        }

        //Skapa notifikationskanal
        createNotificationChannel();

        notifyButton.setOnClickListener(v -> sendLedNotification());
    }

    /**
     * Skapar en notifikationskanal
     * Kanalen krävs för att hantera LED-notifikationer och definierar dess egenskaper
     */
    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(
                    CHANNEL_ID,
                    "LED Notifications",
                    NotificationManager.IMPORTANCE_HIGH
            );
            channel.enableLights(true); //Aktiverar LED-ljus
            channel.setLightColor(android.graphics.Color.BLUE); //Ställ in LED-färg
            NotificationManager manager = getSystemService(NotificationManager.class); //Hämta NotificationManager och skapa kanalen
            if (manager != null) {
                manager.createNotificationChannel(channel);
            }
        }
    }

    /**
     * Skickar en notifikation som aktiverar LED-ljus på enheten (om stöds).
     * Notifikationen innehåller en titel, text och LED-färg
     */
    private void sendLedNotification() {
        NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(android.R.drawable.ic_dialog_info)
                .setContentTitle("LED Notifiering")
                .setContentText("LED-meddelande aktiverat!")
                .setPriority(NotificationCompat.PRIORITY_HIGH)
                .setLights(android.graphics.Color.BLUE, 1000, 2000) //LED-färg, på-tid (1s), av-tid (2s)
                .setDefaults(Notification.DEFAULT_ALL)
                .setAutoCancel(true);
        //Skicka notifikationen om NotificationManager finns
        if (notificationManager != null) {
            notificationManager.notify(NOTIFICATION_ID, builder.build());
        }
    }

    /**
     * Hanterar resultatet från permission-begäran för notifikationer.
     * Visar en Toast beroende på om användaren gav tillåtelse för notifikationer.
     *
     * @param requestCode Kod för begäran (i detta fall notifikationstillstånd).
     * @param permissions De begärda rättigheterna (t.ex. POST_NOTIFICATIONS).
     * @param grantResults Resultatet av begäran (beviljad eller nekad).
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Tillstånd beviljat", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Tillstånd nekades", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
