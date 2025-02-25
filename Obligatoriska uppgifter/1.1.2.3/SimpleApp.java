package com.simpleapp;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Button;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

/*
 * SimpleApp är en Android-app som låter användaren skriva text i ett textfält
 * och visa det i en annan vy samt visa en bekräftelse via ett Toast-meddelande.
 */

public class SimpleApp extends AppCompatActivity {

    /**
     * onCreate-metoden körs när aktiviteten startas. Denna metod sätter layouten
     * för aktiviteten och gör initialiseringar för UI-komponenterna.
     *
     * @param savedInstanceState En Bundle som innehåller tidigare tillstånd för aktiviteten om appen återställs efter att ha stängts.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Sätt layouten för appen från XML-filen (simple_app.xml)
        setContentView(R.layout.simple_app);

        //Hämta referenser från simple_app.xml
        TextView textView = findViewById(R.id.textView);
        EditText editText = findViewById(R.id.editText);
        Button button = findViewById(R.id.button);

        //OnClickListener för knappen så användaren kan trycka på knappen
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Hämta texten som användaren skrivit
                String userInput = editText.getText().toString();
                //Visa bekräftelsemeddelande med Toast
                Toast.makeText(SimpleApp.this, "Du skrev detta: " + userInput, Toast.LENGTH_SHORT).show();
                //Uppdatera TextView med texten som användaren skrev
                textView.setText("Text: " + userInput);
            }
        });
    }
}
