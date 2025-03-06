package com.budgy;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Start är den första aktiviteten i budgetappen.
 * Den visar en startskärm med en knapp som låter användaren påbörja budgetprocessen.
 */
public class Start extends AppCompatActivity {

    /**
     * Initialiserar aktiviteten och sätter layouten.
     * Hämtar referens till startknappen och sätter en lyssnare för att navigera vidare.
     *
     * @param savedInstanceState Om aktiviteten återställs sparas tidigare tillstånd här.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start);

        Button startButton = findViewById(R.id.start_button);
        startButton.setOnClickListener(v -> {
            //Navigera vidare till nästa
            Intent intent = new Intent(Start.this, Income.class);
            startActivity(intent);
        });
    }
}
