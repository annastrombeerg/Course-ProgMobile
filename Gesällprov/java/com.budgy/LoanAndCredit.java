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
 * LoanAndCredit hanterar användarens inmatning av lån och krediter i budgetappen.
 * Användaren kan lägga till, visa och ta bort utgifter kopplade till lån/krediter.
 * Utgifterna sparas i en lista och visas i en RecyclerView.
 */
public class LoanAndCredit extends AppCompatActivity implements ExpenseList.OnExpenseDeleteListener{
    EditText expenseName, expenseAmount;
    Button addExpense, nextButton;
    RecyclerView expenseList;
    TextView totalExpense;
    ExpenseList adapter;
    List<Expense> expenses;

    /**
     * Körs när aktiviteten skapas. Initierar layout och komponenter,
     * samt sätter upp RecyclerView för att visa en lista med lån/kreditutgifter.
     * Hanterar knapptryckningar för att lägga till nya utgifter och navigera till nästa sida.
     *
     * @param savedInstanceState Sparad instansdata vid återställning av aktiviteten.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.loanandcredit);

        expenseName = findViewById(R.id.expense_name);
        expenseAmount = findViewById(R.id.expense_amount);
        addExpense = findViewById(R.id.add_expense_button);
        nextButton = findViewById(R.id.next_button);
        expenseList = findViewById(R.id.expense_list);
        totalExpense = findViewById(R.id.total_expense);

        //Hämta befintliga (om de finns) och koppla till RecyclerView-adaptern
        expenses = Expense.getLoanCreditExpenses();
        adapter = new ExpenseList(expenses, this);
        expenseList.setLayoutManager(new LinearLayoutManager(this)); //Sätter upp RecyclerView med en linjär layout
        expenseList.setAdapter(adapter);
        updateTotalExpense(); //Uppdaterar den totala summan av lån/kreditutgifter

        /**
         * Hanterar klick på "Next"-knappen.
         * Lägger till en nya lån- eller kreditutgifter i listan.
         * Om både namn och belopp är angivna, läggs utgiften till i Expense-klassen
         * och listan uppdateras i RecyclerView.
         */
        addExpense.setOnClickListener(v -> {
            String name = expenseName.getText().toString().trim();
            String amount = expenseAmount.getText().toString().trim();

            if (!name.isEmpty() && !amount.isEmpty()) {
                double addAmount = Double.parseDouble(amount);
                Expense.addLoanCredit(name, addAmount);

                //Uppdatera listan
                expenses.clear();
                expenses.addAll(Expense.getLoanCreditExpenses());
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
            Intent intent = new Intent(LoanAndCredit.this, VariableExpense.class);
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
        Expense.removeLoanCreditExpense(position);

        //Uppdatera listan efter borttagning
        expenses.clear();
        expenses.addAll(Expense.getLoanCreditExpenses());
        adapter.notifyDataSetChanged();
        updateTotalExpense();
    }

    /**
     * Uppdaterar TextView som visar summan av lån/kreditutgifter.
     */
    private void updateTotalExpense() {
        totalExpense.setText("TOTAL: " + Expense.getTotalLoanCredits() + " KR");
    }
}
