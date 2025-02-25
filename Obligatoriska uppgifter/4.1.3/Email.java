package com.email;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;

/*
 * Email är en Android-app som gör det möjligt för användaren att skicka e-post via en inbyggd e-postklient.
 * Användaren kan ange mottagare, ämne och meddelande samt bifoga en fil. Appen använder Androids Intent
 * för att skapa och skicka e-post via en standard e-postklient.
 * OBS! Denna är prövad på en Pixel 8 Pro API 35.
 */
public class Email extends AppCompatActivity {
    private EditText editTextTo, editTextSubject, editTextMessage;
    private Button buttonSend, buttonAttachFile;
    private Uri fileUri;
    private ActivityResultLauncher<Intent> activityResultLauncher;

    /**
     * onCreate-metoden körs när aktiviteten startas. Denna metod sätter layouten
     * för aktiviteten och gör initialiseringar för UI-komponenterna.
     * Den konfigurerar också ActivityResultLauncher för att hantera filval och knappar för att bifoga fil och skicka e-post.
     *
     * @param savedInstanceState En Bundle som innehåller tidigare tillstånd för aktiviteten om appen återställs efter att ha stängts.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.email);

        //Hämta referenser till EditText och knappar
        editTextTo = findViewById(R.id.editTextTo);
        editTextSubject = findViewById(R.id.editTextSubject);
        editTextMessage = findViewById(R.id.editTextMessage);
        buttonSend = findViewById(R.id.buttonSendEmail);
        buttonAttachFile = findViewById(R.id.buttonAttachFile);

        //Initiera ActivityResultLauncher för att hantera resultatet av filval
        activityResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                result -> {
                    if (result.getResultCode() == RESULT_OK && result.getData() != null) {
                        //Hämta den valda filens URI
                        fileUri = result.getData().getData();
                        Toast.makeText(this, "Fil valdes: " + fileUri.toString(), Toast.LENGTH_SHORT).show();
                    }
                });

        //Klicklyssnare för att öppna filväljare
        buttonAttachFile.setOnClickListener(v -> openFileChooser());

        //Klicklyssnare för att skicka e-post
        buttonSend.setOnClickListener(v -> sendEmail());
    }

    /**
     * Öppnar en filväljare så användaren kan välja en fil att bifoga till e-posten.
     * Filen som väljs sparas i filUri.
     */
    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");  //Tillåter alla filtyper
        activityResultLauncher.launch(intent);  //Starta aktivitet för att välja fil
    }

    /**
     * Skickar ett e-postmeddelande genom att använda en Intent. E-posten skickas till mottagaren
     * med det angivna ämnet, meddelandet och en eventuell bifogad fil.
     */
    private void sendEmail() {
        String to = editTextTo.getText().toString();
        String subject = editTextSubject.getText().toString();
        String message = editTextMessage.getText().toString();

        //Kontrollera att användaren har fyllt i alla fält
        if (to.isEmpty() || subject.isEmpty() || message.isEmpty()) {
            Toast.makeText(this, "Alla fält måste fyllas i", Toast.LENGTH_SHORT).show();
            return;
        }

        //Skapa en Intent för att skicka e-post
        Intent emailIntent = new Intent(Intent.ACTION_SEND);
        emailIntent.setType("message/rfc822");  //Skicka som e-post
        emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{to});  //Mottagare
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);  //Ämne
        emailIntent.putExtra(Intent.EXTRA_TEXT, message);  //Meddelande

        //Bifoga fil om en fil har valts
        if (fileUri != null) {
            emailIntent.putExtra(Intent.EXTRA_STREAM, fileUri);
        }

        try {
            startActivity(Intent.createChooser(emailIntent, "Skicka e-post"));
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(this, "Det finns ingen e-postklient installerad.", Toast.LENGTH_SHORT).show();
        }
    }
}
