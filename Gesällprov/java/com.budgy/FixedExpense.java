package com.budgy;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

/**
 * FixedExpense hanterar användarens inmatning av fasta utgifter.
 * Låter användaren lägga till och ta bort utgifter samt visar en total summa.
 */
public class FixedExpense extends AppCompatActivity implements ExpenseList.OnExpenseDeleteListener{
    EditText expenseName, expenseAmount;
    Button addExpense, nextButton;
    RecyclerView expenseList;
    TextView totalExpense;
    ExpenseList adapter;
    List<Expense> expenses;

    /**
     * Körs när aktiviteten skapas. Initierar layouten och komponenter
     * samt sätter upp RecyclerView för att visa en lista med fasta utgifter.
     * Hanterar knapptryckningar för att lägga till nya utgifter och navigera till sammanfattningssidan.
     *
     * @param savedInstanceState Sparad instansdata vid återställning av aktiviteten.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fixed);

        expenseName = findViewById(R.id.expense_name);
        expenseAmount = findViewById(R.id.expense_amount);
        addExpense = findViewById(R.id.add_expense_button);
        nextButton = findViewById(R.id.next_button);
        expenseList = findViewById(R.id.expense_list);
        totalExpense = findViewById(R.id.total_expense);

        //Hämta befintliga (om de finns) och koppla till RecyclerView-adaptern
        expenses = Expense.getFixedExpenses();
        adapter = new ExpenseList(expenses, this);
        expenseList.setLayoutManager(new LinearLayoutManager(this));
        expenseList.setAdapter(adapter);
        updateTotalExpense(); //Uppdaterar den totala summan av fasta utgifter

        /**
         * Hanterar klick på "Next"-knappen.
         * Lägger till en ny fast utgift i listan.
         * Om både namn och belopp är angivna, läggs utgiften till i Expense-klassen
         * och listan uppdateras i RecyclerView.
         */
        addExpense.setOnClickListener(v -> {
            String name = expenseName.getText().toString().trim();
            String amount = expenseAmount.getText().toString().trim();

            if (!name.isEmpty() && !amount.isEmpty()) {
                double addAmount = Double.parseDouble(amount);
                Expense.addFixedExpense(name, addAmount);

                //Uppdatera listan
                expenses.clear();
                expenses.addAll(Expense.getFixedExpenses());
                adapter.notifyDataSetChanged();

                //Rensa inputfält och uppdatera totalsumman
                expenseName.setText("");
                expenseAmount.setText("");
                updateTotalExpense();
            }
        });

        /**
         * Navigerar till nästa sida och rensar den aktuella listan i RecyclerView
         * innan övergången sker.
         */
        nextButton.setOnClickListener(v -> {
            expenses.clear(); //Rensar RecyclerView-listan
            adapter.notifyDataSetChanged(); //Uppdaterar listan i UI
            //Navigera till nästa sida
            Intent intent = new Intent(FixedExpense.this, LoanAndCredit.class);
            startActivity(intent);
        });
    }

    /**
     * Tar bort en utgift från listan baserat på angiven position.
     * Efter borttagning uppdateras listan i RecyclerView och den totala summan beräknas om.
     *
     * @param position Positionen för utgiften som ska tas bort.
     */
    @Override
    public void onDeleteExpense(int position) {
        Expense.removeFixedExpense(position);

        //Uppdatera listan efter borttagning
        expenses.clear();
        expenses.addAll(Expense.getFixedExpenses());
        adapter.notifyDataSetChanged();
        updateTotalExpense();
    }

    /**
     * Uppdaterar TextView som visar den totala summan av fasta utgifter.
     */
    private void updateTotalExpense() {
        totalExpense.setText("TOTAL: " + Expense.getTotalFixedExpenses() + " KR");
    }
}
