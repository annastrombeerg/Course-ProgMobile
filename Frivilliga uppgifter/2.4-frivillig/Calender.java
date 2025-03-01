package com.calender;

import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.icu.text.SimpleDateFormat;
import android.icu.util.Calendar;
import android.net.Uri;
import android.os.Bundle;
import android.provider.CalendarContract;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import android.Manifest;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

/**
 * Calender är en Android-app som hämtar och visar dagens händelser från Google Kalender.
 * Appen begär nödvändiga behörigheter och visar händelser i en lista.
 *
 * OBS! Appen hämtar endast "Events" från Google Kalender, inte "Tasks".
 */
public class Calender extends AppCompatActivity {
    private static final int PERMISSION_REQUEST = 200;
    private ListView eventListView;
    private ArrayAdapter<String> eventAdapter;
    private ArrayList<String> eventList;

    /**
     * onCreate-metoden körs när aktiviteten startas. Denna metod sätter layouten
     * för aktiviteten och gör initialiseringar för UI-komponenterna.
     * Den hanterar också begäran om behörighet och laddar kalenderhändelser om rätt tillstånd är beviljat.
     *
     * @param savedInstanceState En Bundle som innehåller tidigare tillstånd för aktiviteten om appen återställs efter att ha stängts.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.calender);

        eventListView = findViewById(R.id.eventListView);
        eventList = new ArrayList<>();
        eventAdapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, eventList);
        eventListView.setAdapter(eventAdapter);

        //Kolla och be om tillstånd att läsa kalendern
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALENDAR) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CALENDAR}, PERMISSION_REQUEST);
        } else {
            fetchCalendarEvents();
        }
    }

    /**
     * Hämtar och visar dagens kalenderhändelser från enhetens Google Kalender.
     * Filtrerar händelser baserat på dagens datum.
     */
    private void fetchCalendarEvents() {
        Uri uri = CalendarContract.Events.CONTENT_URI;
        ContentResolver contentResolver = getContentResolver();

        //Hämta dagens datum
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
        long startOfDay = calendar.getTimeInMillis();

        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
        long endOfDay = calendar.getTimeInMillis();

        //Skapa en fråga för att hämta händelser
        String selection = "((" + CalendarContract.Events.DTSTART + " >= ?) AND (" + CalendarContract.Events.DTSTART + " <= ?)) AND (" + CalendarContract.Events.VISIBLE + " = ?)";
        String[] selectionArgs = new String[]{String.valueOf(startOfDay), String.valueOf(endOfDay), "1"};

        Cursor cursor = contentResolver.query(uri, new String[]{
                CalendarContract.Events.TITLE,
                CalendarContract.Events.DTSTART,
                CalendarContract.Events.DESCRIPTION,
                CalendarContract.Events.CALENDAR_DISPLAY_NAME
        }, selection, selectionArgs, null);

        eventList.clear();

        if (cursor == null || cursor.getCount() == 0) {
            Toast.makeText(this, "Inga händelser hittades för idag", Toast.LENGTH_SHORT).show();
            return;
        }
        //Loopa igenom alla hämtade händelser och lägg till dem i listan
        while (cursor.moveToNext()) {
            String eventTitle = cursor.getString(0);
            long eventTimeMillis = cursor.getLong(1);
            String eventDescription = cursor.getString(2);
            String calendarName = cursor.getString(3);

            String eventTime = new SimpleDateFormat("HH:mm", Locale.getDefault()).format(new Date(eventTimeMillis));

            eventList.add("Kalender: " + calendarName + "\n" + eventTitle + " - " + eventTime + "\nBeskrivning: " + (eventDescription != null ? eventDescription : "Ingen beskrivning"));
        }
        cursor.close();

        eventAdapter.notifyDataSetChanged();
        Toast.makeText(this, "Hämtade " + eventList.size() + " händelser", Toast.LENGTH_SHORT).show();
    }

    /**
     * Hanterar användarens svar på behörighetsförfrågan.
     * Om behörighet beviljas hämtas kalenderhändelser, annars visas ett meddelande.
     *
     * @param requestCode   Koden som identifierar behörighetsbegäran.
     * @param permissions   En array som innehåller de behörigheter som begärts.
     * @param grantResults  En array som innehåller resultatet för varje begärd behörighet (beviljad eller nekad).
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                fetchCalendarEvents();
            } else {
                Toast.makeText(this, "Behörighet nekad. Kan inte läsa kalendern.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
