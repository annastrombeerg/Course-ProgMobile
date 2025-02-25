package com.notification;

import android.app.AlertDialog;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.content.ContextCompat;

/*
 * NotificationApp är en Android-app som demonstrerar användning av notifikationer, dialogfönster och toast-meddelanden.
 *
 * OBS! Denna app är prövad på en Pixel 8 Pro API 35.
 */
public class NotificationApp extends AppCompatActivity {
    private Button btnToast, btnDialog, btnNotification;

    /**
     * onCreate-metoden körs när aktiviteten startas. Denna metod sätter layouten
     * för aktiviteten och gör initialiseringar för UI-komponenterna.
     * Den hanterar skapande och konfigurering av en notifikationskanal för att hantera notifikationer,
     * samt lägg till onClickListeners för att visa Toast, Dialog och Notifikationer.
     *
     * @param savedInstanceState En Bundle som innehåller tidigare tillstånd för aktiviteten om appen återställs efter att ha stängts.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.notification);

        //Be om behörigheten att posta notifikationer
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 100);
            }
        }

        //Skapa en notifikationskanal för Android 8.0 och senare
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            //Definiera kanalens ID, namn och beskrivning
            CharSequence name = "Default Channel";
            String description = "Channel for basic notifications";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel("default", name, importance);
            channel.setDescription(description);

            //Registrera kanalen med systemet
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(channel);
            }
        }

        //Hitta UI-komponenterna
        btnToast = findViewById(R.id.btnToast);
        btnDialog = findViewById(R.id.btnDialog);
        btnNotification = findViewById(R.id.btnNotification);

        //Lägg till en onClickListener för Toast-knappen
        btnToast.setOnClickListener(v -> {
            //Visa en Toast-meddelande
            Toast.makeText(NotificationApp.this, "Detta är en Toast!", Toast.LENGTH_SHORT).show();
        });

        //Lägg till en onClickListener för Dialog-knappen
        btnDialog.setOnClickListener(v -> {
            //Skapa och visa ett dialogfönster
            new AlertDialog.Builder(NotificationApp.this)
                    .setTitle("Dialog")
                    .setMessage("Detta är ett dialogfönster")
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            //Användaren trycker på OK
                            Toast.makeText(NotificationApp.this, "Dialog stängd", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .setNegativeButton("Avbryt", null)
                    .show();
        });

        //Lägg till en onClickListener för Notifikation-knappen
        btnNotification.setOnClickListener(v -> {
            //Skapa en notifikation
            Notification notification = new NotificationCompat.Builder(NotificationApp.this, "default")
                    .setSmallIcon(android.R.drawable.ic_dialog_info)
                    .setContentTitle("Ny Notifikation")
                    .setContentText("Detta är en testnotifikation!")
                    .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                    .build();

            //Hämta NotificationManager och visa notifikationen
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.notify(1, notification);
        });
    }
}
