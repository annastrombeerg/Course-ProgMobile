package com.vibration;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.VibrationEffect;
import android.os.Vibrator;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import android.Manifest;
import android.widget.Toast;

/*
 * Vibration är en Android-app som använder vibrationer när användaren trycker på en knapp.
 *
 * OBS! Denna app är prövad på en Pixel 8 Pro API 35.
 */
public class Vibration extends AppCompatActivity {
    private Button btnVibrate;

    /**
     * onCreate-metoden körs när aktiviteten startas. Denna metod sätter layouten
     * för aktiviteten och gör initialiseringar för UI-komponenterna.
     * Den hanterar också begäran om behörighet för att använda vibrationer och hanterar knapptryckningar.
     *
     * @param savedInstanceState En Bundle som innehåller tidigare tillstånd för aktiviteten om appen återställs efter att ha stängts.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vibration);

        //Kontrollera om behörigheten är beviljad
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.VIBRATE) != PackageManager.PERMISSION_GRANTED) {
            //Om inte, be om behörigheten
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.VIBRATE}, 100);
        }

        //Hitta UI-komponenten för knappen
        btnVibrate = findViewById(R.id.btnVibrate);

        //Lägg till en onClickListener för knappen
        btnVibrate.setOnClickListener(v -> {
            //Hämta Vibrator-systemtjänsten
            Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);

            if (vibrator != null) {
                //Kontrollera om enheten har stöd för vibrationer
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
                    //För Android 8.0 (API 26) och högre, använd VibrationEffect
                    vibrator.vibrate(VibrationEffect.createOneShot(1000, VibrationEffect.DEFAULT_AMPLITUDE));
                    Toast.makeText(this, "Vibrerar!", Toast.LENGTH_SHORT).show();
                } else {
                    //För äldre versioner av Android (före API 26)
                    vibrator.vibrate(1000);
                    Toast.makeText(this, "Vibrerar!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * Denna metod hanterar resultatet av användarens svar på behörighetsbegäran.
     * Den kontrollerar om användaren har beviljat eller nekats behörigheten för att använda vibrationer.
     * Om behörigheten beviljas, visas ett bekräftande toastmeddelande och appen kan använda vibrationer.
     * Om behörigheten nekas, visas ett toastmeddelande som informerar användaren om att vibrationer inte kan användas.
     *
     * @param requestCode Den kod som identifierar begäran om behörigheter. Den används för att särskilja olika begärningar om flera görs.
     * @param permissions En array som innehåller de behörigheter som begärts.
     * @param grantResults En array som innehåller resultatet för varje begärd behörighet (beviljad eller nekad).
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 100) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //Behörighet beviljad
                Toast.makeText(this, "Behörighet för vibration beviljad!", Toast.LENGTH_SHORT).show();
            } else {
                //Behörighet nekad
                Toast.makeText(this, "Behörighet för vibration nekad!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
