package com.contact;

import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import android.Manifest;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import java.util.ArrayList;
import java.util.List;

/**
 * Contact är en Android-app som låter användaren söka efter kontakter
 * på enheten genom att mata in en söktext och visar matchande resultat.
 *
 * OBS! Denna är prövad på en Pixel 8 Pro API 35.
 */
public class Contact extends AppCompatActivity {
    private static final int REQUEST_CONTACT_PERMISSION = 200;
    private EditText searchField;
    private Button searchButton;
    private ListView contactsListView;
    private ArrayAdapter<String> adapter;
    private List<String> contactsList;

    /**
     * onCreate-metoden körs när aktiviteten startas. Denna metod sätter layouten
     * för aktiviteten och gör initialiseringar för UI-komponenterna.
     * Den hanterar också begäran av nödvändiga behörigheter.
     *
     * @param savedInstanceState En Bundle som innehåller tidigare tillstånd för aktiviteten om appen återställs efter att ha stängts.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact);

        searchField = findViewById(R.id.searchField);
        searchButton = findViewById(R.id.searchButton);
        contactsListView = findViewById(R.id.contactsListView);

        contactsList = new ArrayList<>();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, contactsList);
        contactsListView.setAdapter(adapter);

        //Kontrollera och begär behörighet för att läsa kontakter
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.READ_CONTACTS}, REQUEST_CONTACT_PERMISSION);
        }

        //Hantera klick på sökknappen
        searchButton.setOnClickListener(v -> searchContacts(searchField.getText().toString().trim()));
    }

    /**
     * Söker efter kontakter som matchar den angivna texten och uppdaterar listan.
     *
     * @param query Texten som ska matchas mot kontaktnamnen.
     */
    private void searchContacts(String query) {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
            Toast.makeText(this, "Behörighet krävs för att läsa kontakter", Toast.LENGTH_SHORT).show();
            return;
        }
        //Rensa listan inför ny sökning
        contactsList.clear();
        ContentResolver contentResolver = getContentResolver();
        Cursor cursor = contentResolver.query(
                ContactsContract.Contacts.CONTENT_URI,
                new String[]{ContactsContract.Contacts.DISPLAY_NAME},
                ContactsContract.Contacts.DISPLAY_NAME + " LIKE ?",
                new String[]{"%" + query + "%"},
                ContactsContract.Contacts.DISPLAY_NAME + " ASC"
        );
        //Lägg till matchande kontakter i listan
        if (cursor != null) {
            while (cursor.moveToNext()) {
                String contactName = cursor.getString(0);
                contactsList.add(contactName);
            }
            cursor.close();
        }

        if (contactsList.isEmpty()) {
            contactsList.add("Inga matchande kontakter hittades.");
        }

        adapter.notifyDataSetChanged();
    }

    /**
     * Hanterar resultatet av behörighetsförfrågningar.
     * Om behörighet beviljas kan kontakter visas, annars visas ett meddelande.
     *
     * @param requestCode   Koden för begäran.
     * @param permissions   De begärda behörigheterna.
     * @param grantResults  Resultaten för varje begärd behörighet.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_CONTACT_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Behörighet beviljad!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Behörighet nekad! Kan inte läsa kontakter.", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
