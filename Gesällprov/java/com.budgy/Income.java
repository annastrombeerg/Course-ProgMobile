package com.budgy;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Income är en aktivitet där användaren kan ange sin inkomst.
 * Den hanterar inmatning, formatering av text samt navigering till nästa steg i budgetprocessen.
 */
public class Income extends AppCompatActivity {
    Button nextButton, backToChartButton;
    EditText income;
    boolean cameFromSummary = false;

    /**
     * Körs när aktiviteten skapas. Initierar layout och komponenter,
     * samt hämtar tidigare sparad inkomst om den finns.
     *
     * @param savedInstanceState Sparad instansdata vid återställning av aktiviteten.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.income);

        nextButton = findViewById(R.id.next_button);
        income = findViewById(R.id.income_amount);
        backToChartButton = findViewById(R.id.back_to_chart);

        if (Expense.getIncome() > 0) {
            income.setText(String.valueOf(Expense.getIncome()));
        }

        //Hämta flagga från Intent (om användaren kom från Summary)
        cameFromSummary = getIntent().getBooleanExtra("cameFromSummary", false);

        //Sätt knappen osynlig i början
        backToChartButton.setVisibility(cameFromSummary ? View.VISIBLE : View.GONE);

        /**
         * Hanterar klick på "Next"-knappen.
         * Sparar den inmatade inkomsten efter att ha rensat eventuella extra tecken
         * och navigerar sedan till nästa aktivitet.
         */
        nextButton.setOnClickListener(v -> {
            saveIncome();
            //Navigera till nästa sida
            Intent intent = new Intent(Income.this, FixedExpense.class);
            startActivity(intent);
        });

        /**
         * Hanterar klick på "Back to Chart"-knappen.
         * Sparar inkomsten och navigerar direkt tillbaka till PieChart-sidan.
         */
        backToChartButton.setOnClickListener(v -> {
            saveIncome();
            Intent intent = new Intent(Income.this, Summary.class);
            startActivity(intent);
        });

        /**
         * Lyssnar på förändringar i inmatningsfältet för att säkerställa att valutan
         * "KR" alltid finns med i texten.
         */
        income.addTextChangedListener(new TextWatcher() {
            private boolean isEditing = false;

            /**
             * Anropas innan texten i EditText ändras.
             * I denna implementering används den inte, men måste inkluderas eftersom den är en del av TextWatcher-gränssnittet.
             */
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            /**
             * Anropas när texten ändras i EditText.
             * I denna implementering används den inte, men måste inkluderas eftersom den är en del av TextWatcher-gränssnittet.
             */
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            /**
             * Anropas efter att texten har ändrats.
             * Säkerställer att " KR" alltid läggs till i slutet av inmatningen och att markören placeras korrekt.
             */
            @Override
            public void afterTextChanged(Editable editable) {
                if (isEditing) return; //Undviker rekursiv ändring av text
                isEditing = true;
                //Tar bort eventuell befintlig " KR" text och rensar mellanslag
                String text = editable.toString().replace(" KR", "").trim();
                if (!text.isEmpty()) { //Om texten inte är tom, lägg till " KR" och uppdatera texten i EditText
                    text += " KR";
                    income.setText(text);
                    income.setSelection(text.length() - 3);
                }
                isEditing = false;
            }
        });
    }

    /**
     * Sparar inkomsten från EditText till Expense-klassen.
     */
    private void saveIncome() {
        double incomeValue = 0;
        if (!income.getText().toString().isEmpty()) {
            incomeValue = Double.parseDouble(income.getText().toString().replace(" KR", "").trim());
        }
        Expense.setIncome(incomeValue);
    }
}
