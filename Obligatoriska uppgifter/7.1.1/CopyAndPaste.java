package com.copynpaste;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

/*
 * CopyAndPaste är en Android-app som tillåter användaren att kopiera text från ett EditText
 * och klistra in den i ett annat EditText. Appen använder Androids ClipboardManager
 * för att hantera copy-paste-funktionen. Användaren kan skriva in text i en ruta, kopiera
 * den till systemets clipboard och sedan klistra in den i en annan ruta.
 *
 * OBS! Denna app är prövad på en Pixel 8 Pro API 35.
 */
public class CopyAndPaste extends AppCompatActivity {
    private EditText textSource, textDestination;
    private Button btnCopy, btnPaste;

    /**
     * onCreate-metoden körs när aktiviteten startas. Denna metod sätter layouten
     * för aktiviteten och gör initialiseringar för UI-komponenterna.
     * Den hanterar skapande och konfigurering av en ClipboardManager för att hantera copy-paste, samt lägga till onClickListeners för kopiering
     * och inklistring av text från och till EditText-komponenterna.
     *
     * @param savedInstanceState En Bundle som innehåller tidigare tillstånd för aktiviteten om appen återställs efter att ha stängts.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.copynpaste);

        //Hitta UI-komponenterna
        textSource = findViewById(R.id.textSource);
        textDestination = findViewById(R.id.textDestination);
        btnCopy = findViewById(R.id.btnCopy);
        btnPaste = findViewById(R.id.btnPaste);

        //Skapa ClipboardManager för att hantera copy-paste
        ClipboardManager clipboard = (ClipboardManager) getSystemService(CLIPBOARD_SERVICE);

        //Lägg till en onClickListener på knappen med lambda
        btnCopy.setOnClickListener(v -> {
            //Hämta texten från source EditText
            String textToCopy = textSource.getText().toString();

            if (!textToCopy.isEmpty()) {
                //Skapa ClipData (data som ska kopieras)
                ClipData clip = ClipData.newPlainText("copiedText", textToCopy);

                //Sätt ClipData i ClipboardManager
                clipboard.setPrimaryClip(clip);

                //Visa en Toast för att informera användaren
                Toast.makeText(CopyAndPaste.this, "Text kopierad!", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(CopyAndPaste.this, "Skriv något i fältet för att kopiera", Toast.LENGTH_SHORT).show();
            }
        });

        //Lägg till en onClickListener för klistra in-knappen
        btnPaste.setOnClickListener(v -> {
            //Kontrollera om clipboard har något data
            if (clipboard.hasPrimaryClip()) {
                //Hämta texten från clipboard
                ClipData clipData = clipboard.getPrimaryClip();
                ClipData.Item item = clipData.getItemAt(0);
                String pastedText = item.getText().toString();

                //Klistra in texten i destination EditText
                textDestination.setText(pastedText);

                //Visa en Toast för att informera användaren
                Toast.makeText(CopyAndPaste.this, "Text klistrad!", Toast.LENGTH_SHORT).show();
            } else {
                //Visa en Toast om inget finns att klistra in från clipboard
                Toast.makeText(CopyAndPaste.this, "Ingen text i clipboard att klistra in", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
