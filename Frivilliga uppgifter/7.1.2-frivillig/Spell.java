package com.spell;

import android.content.Context;
import android.os.Bundle;
import android.view.textservice.SpellCheckerSession;
import android.view.textservice.SuggestionsInfo;
import android.view.textservice.TextInfo;
import android.view.textservice.TextServicesManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Spell är en Android-app som kontrollerar stavningen av ett enskilt ord.
 * Användaren skriver ett ord i en EditText och får ett förslag om ordet är felstavat.
 *
 * OBS! Denna är prövad på en Pixel 8 Pro API 35.
 */
public class Spell extends AppCompatActivity implements SpellCheckerSession.SpellCheckerSessionListener {
    private EditText inputWord;
    private TextView suggestionView;
    private Button checkSpellButton;
    private SpellCheckerSession spellCheckerSession;

    /**
     * onCreate-metoden körs när aktiviteten startas. Denna metod sätter layouten
     * för aktiviteten och gör initialiseringar för UI-komponenterna.
     * Den hanterar också knapptryck och att starta stavningskontrollen.
     *
     * @param savedInstanceState En Bundle som innehåller tidigare tillstånd för aktiviteten om appen återställs efter att ha stängts.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.spell);

        inputWord = findViewById(R.id.inputWord);
        suggestionView = findViewById(R.id.suggestionView);
        checkSpellButton = findViewById(R.id.checkSpellButton);

        //Starta stavningskontrollen
        TextServicesManager textServicesManager = (TextServicesManager) getSystemService(Context.TEXT_SERVICES_MANAGER_SERVICE);
        if (textServicesManager != null) {
            spellCheckerSession = textServicesManager.newSpellCheckerSession(null, null, this, true);
        }

        //Hantera knapptryck för att kontrollera stavning
        checkSpellButton.setOnClickListener(v -> {
            String word = inputWord.getText().toString().trim();
            if (!word.isEmpty() && spellCheckerSession != null) {
                spellCheckerSession.getSuggestions(new TextInfo(word), 5);
            }
        });
    }

    /**
     * Hanterar stavningsförslag från stavningskontrollen.
     * Om det finns förslag på rätt stavning, visas dessa i en TextView.
     * Om inga förslag hittas, visas ett meddelande om att ordet kan vara korrekt.
     *
     * @param results Array med stavningsförslag från SpellCheckerSession.
     */
    @Override
    public void onGetSuggestions(SuggestionsInfo[] results) {
        runOnUiThread(() -> {
            if (results.length > 0 && results[0].getSuggestionsCount() > 0) {
                StringBuilder suggestionsText = new StringBuilder();
                suggestionsText.append("Did you mean: ");

                for (int i = 0; i < results[0].getSuggestionsCount(); i++) {
                    suggestionsText.append(results[0].getSuggestionAt(i));
                    if (i < results[0].getSuggestionsCount() - 1) {
                        suggestionsText.append(", ");
                    }
                }
                suggestionView.setText(suggestionsText.toString());
            } else {
                suggestionView.setText("No suggestions, the word might be correct!");
            }
        });
    }

    @Override
    public void onGetSentenceSuggestions(android.view.textservice.SentenceSuggestionsInfo[] results) {
        //Inte använd i denna app
    }

    /**
     * Stänger stavningssessionen när aktiviteten förstörs.
     * Förhindrar minnesläckor genom att frigöra resurser.
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (spellCheckerSession != null) {
            spellCheckerSession.close();
        }
    }
}
