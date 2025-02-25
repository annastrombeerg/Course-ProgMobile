package com.phone;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CallLog;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.util.ArrayList;

/*
 * Phone är en Android-app som tillåter användaren att se samtalshistorik
 * och ringa upp ett nummer genom att klicka på ett nummer i listan.
 * Appen använder tillstånd för att läsa samtalshistorik och ringa samtal.
 * OBS! Denna är prövad på en Pixel 8 Pro API 35.
 */
public class Phone extends AppCompatActivity {
    private static final int REQUEST_CALL_LOG = 200;
    private static final int REQUEST_CALL_PHONE = 200;
    private ListView callLog;

    /**
     * onCreate-metoden körs när aktiviteten startas. Denna metod sätter layouten
     * för aktiviteten och gör initialiseringar för UI-komponenterna.
     * Den hanterar också begäran om behörighet och laddar samtalshistorik om rätt tillstånd är beviljat.
     *
     * @param savedInstanceState En Bundle som innehåller tidigare tillstånd för aktiviteten om appen återställs efter att ha stängts.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.phone);

        //Hämta referensen till ListView
        callLog = findViewById(R.id.callLog);

        //Kontrollera om rätt behörigheter finns (för samtalshistorik och ringa samtal)
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.READ_CALL_LOG) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED) {

            //Begär båda behörigheterna
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.READ_CALL_LOG, android.Manifest.permission.CALL_PHONE},
                    REQUEST_CALL_LOG); //Använd samma kod för båda behörigheterna
        } else {
            loadCallHistory(); //Ladda samtalshistorik om rätt behörigheter beviljats
        }

        //Hantera klick på ett nummer i listan
        callLog.setOnItemClickListener((parent, view, position, id) -> {
            String phoneNumber = (String) parent.getItemAtPosition(position);
            dialPhoneNumber(phoneNumber); //Ring upp numret
        });

    }

    /**
     * Hämtar samtalshistorik från CallLog.Calls.CONTENT_URI och lägger till telefonnummer från samtalen i en lista.
     * Denna metod hämtar samtalsnummer och datum från samtalshistoriken, och sorterar resultaten i fallande ordning (nyaste först).
     * Telefonnumren läggs till i en lista som sedan används för att visa samtalshistoriken i en ListView.
     *
     * @return En lista med telefonnummer från samtalshistoriken som har hämtats och sorteras efter senaste samtalet.
     */
    private void loadCallHistory() {
        ArrayList<String> callLogs = new ArrayList<>();
        Uri callUri = CallLog.Calls.CONTENT_URI;
        String[] projection = new String[]{
                CallLog.Calls.NUMBER,
                CallLog.Calls.DATE
        };

        //Få åtkomst till samtalshistoriken
        try (Cursor cursor = getContentResolver().query(callUri, projection, null, null, CallLog.Calls.DATE + " DESC")) {
            if (cursor != null) {
                int numberColumnIndex = cursor.getColumnIndex(CallLog.Calls.NUMBER);

                //Kontroll om det existerar
                if (numberColumnIndex != -1) {
                    while (cursor.moveToNext()) {
                        String phoneNumber = cursor.getString(numberColumnIndex);
                        callLogs.add(phoneNumber);
                    }
                    //Använd ArrayAdapter för att visa samtalshistoriken i ListView
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, callLogs);
                    callLog.setAdapter(adapter);
                } else {
                    Toast.makeText(this, "Nummer kunde inte hittas.", Toast.LENGTH_SHORT).show();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            Toast.makeText(this, "Fel vid hämtning av samtalshistorik.", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * Startar en Intent för att ringa ett telefonnummer.
     * Om rätt behörigheter beviljats startar den aktiviteten.
     *
     * @param phoneNumber Det telefonnummer som ska ringas.
     */
    private void dialPhoneNumber(String phoneNumber) {
        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
            if (phoneNumber != null && !phoneNumber.isEmpty()) {
                Intent dialIntent = new Intent(Intent.ACTION_CALL, Uri.parse("Tel:" + phoneNumber));
                try {
                    startActivity(dialIntent);
                } catch (SecurityException e) {
                    e.printStackTrace();
                    Toast.makeText(this, "Tillstånd för att ringa samtal saknas.", Toast.LENGTH_SHORT).show();
                }
            }
        } else {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.CALL_PHONE}, REQUEST_CALL_PHONE);
        }
    }

    /**
     * Denna metod hanterar resultatet av användarens svar på behörighetsbegäran.
     * Den kollar om användaren har beviljat eller nekats behörigheterna för att läsa samtalshistorik och att ringa samtal.
     * Om båda behörigheterna har beviljats, laddas samtalshistoriken. Om någon behörighet nekas, visas ett toastmeddelande.
     *
     * @param requestCode Den kod som identifierar begäran om behörigheter. Den används för att särskilja olika begärningar om flera görs.
     * @param permissions En array som innehåller de behörigheter som begärts.
     * @param grantResults En array som innehåller resultatet för varje begärd behörighet (beviljad eller nekad).
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CALL_LOG) {
            if (grantResults.length > 0) {
                //Kontrollera om båda behörigheterna har beviljats
                boolean callLogPermissionGranted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                boolean callPhonePermissionGranted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                if (callLogPermissionGranted && callPhonePermissionGranted) {
                    loadCallHistory(); //Ladda samtalshistorik om båda behörigheterna beviljats
                } else {
                    Toast.makeText(this, "Behörighet för samtalshistorik eller att ringa samtal nekades", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
