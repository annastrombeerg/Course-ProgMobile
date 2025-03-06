package com.budgy;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import org.eazegraph.lib.charts.PieChart;
import org.eazegraph.lib.models.PieModel;

/**
 * Summary är en aktivitet som visar en sammanfattning av användarens budget.
 * Den presenterar en PieChart som visar fördelningen av inkomster och utgifter
 * samt en balansräkning. Användaren kan navigera tillbaka till föregående steg
 * eller börja om processen.
 */
public class Summary extends AppCompatActivity {
    LinearLayout startOver, income, expenses;
    PieChart pieChart;
    TextView balanceView;

    /**
     * Initialiserar aktiviteten, sätter layouten och hämtar referenser till UI-komponenter.
     * Beräknar och visar användarens balans samt skapar PieChart för budgetfördelning.
     * Hanterar knappar för att navigera mellan olika sektioner i appen.
     *
     * @param savedInstanceState Om aktiviteten återställs sparas tidigare tillstånd här.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.summary);

        pieChart = findViewById(R.id.piechart);
        setupPieChart(pieChart);

        startOver = findViewById(R.id.start_over_button);
        income = findViewById(R.id.income_button);
        expenses = findViewById(R.id.expenses_button);
        balanceView = findViewById(R.id.balance);

        /**
         * Beräknar och visar balans genom att subtrahera totala utgifter från inkomsten.
         * Visar balansen i en TextView.
         */
        double balance = Expense.getIncome() -
                (Expense.getTotalFixedExpenses() +
                        Expense.getTotalLoanCredits() +
                        Expense.getTotalVariableExpenses());
        balanceView.setText((balance >= 0 ? "+" : "") + balance + " KR");

        /**
         * Navigerar tillbaka till startskärmen och återställer alla budgetdata.
         * Anropas när användaren trycker på "Start Over"-knappen.
         */
        startOver.setOnClickListener(v -> {
            Expense.resetData();
            startActivity(new Intent(Summary.this, Start.class));
        });

        /**
         * Navigerar till Income-aktiviteten när användaren trycker på "Income"-knappen.
         */
        income.setOnClickListener(v -> {
            Intent intent = new Intent(Summary.this, Income.class);
            intent.putExtra("cameFromSummary", true); //Skickar flagga att användaren kom från Summary
            startActivity(intent);
        });

        /**
         * Navigerar till FixedExpense-aktiviteten när användaren trycker på "Expenses"-knappen.
         */
        expenses.setOnClickListener(v -> startActivity(new Intent(Summary.this, FixedExpense.class)));
    }

    /**
     * Beräknar och visar PieChart med fördelning av inkomster och utgifter.
     * Hämtar de aktuella budgetvärdena och beräknar procentandelar för varje kategori.
     * Uppdaterar PieChart och TextViews med procentandelen av varje kategori.
     *
     * @param pieChart PieChart som ska uppdateras med budgetfördelning.
     */
    private void setupPieChart(PieChart pieChart) {
        //Hämta totala inkomst och utgifter från Expense.java
        double totalIncome = Expense.getIncome();
        double totalFixed = Expense.getTotalFixedExpenses();
        double totalLoan = Expense.getTotalLoanCredits();
        double totalVariable = Expense.getTotalVariableExpenses();
        double totalBudget = totalIncome + totalFixed + totalLoan + totalVariable;

        //Hämta TextViews från layouten
        TextView incomePercentage = findViewById(R.id.income_percentage);
        TextView fixedPercentage = findViewById(R.id.fixed_percentage);
        TextView loanPercentage = findViewById(R.id.loanncred_percentage);
        TextView variablePercentage = findViewById(R.id.variable_percentage);

        //Rensar PieChart för att ta bort tidigare data innan nya värden läggs till
        pieChart.clearChart();

        /**
         * Beräknar procentandelen av varje kategori i budgeten och uppdaterar TextViews samt PieChart.
         * Procentvärdena visas bredvid varje kategori och PieChart-sektionerna skapas dynamiskt.
         */
        if (totalBudget > 0) {
            float incomePercent = (float) ((totalIncome / totalBudget) * 100);
            float fixedPercent = (float) ((totalFixed / totalBudget) * 100);
            float loanPercent = (float) ((totalLoan / totalBudget) * 100);
            float variablePercent = (float) ((totalVariable / totalBudget) * 100);

            //Uppdatera TextViews med procent
            incomePercentage.setText("Income: " + String.format("%.1f", incomePercent) + "%");
            fixedPercentage.setText("Fixed: " + String.format("%.1f", fixedPercent) + "%");
            loanPercentage.setText("Loan/Credit: " + String.format("%.1f", loanPercent) + "%");
            variablePercentage.setText("Variable: " + String.format("%.1f", variablePercent) + "%");

            //Lägg till "slices" i PieChart
            if (totalIncome > 0)
                pieChart.addPieSlice(new PieModel("Income", (float) totalIncome, Color.parseColor("#B8E1FF")));
            if (totalFixed > 0)
                pieChart.addPieSlice(new PieModel("Fixed", (float) totalFixed, Color.parseColor("#087E8B")));
            if (totalLoan > 0)
                pieChart.addPieSlice(new PieModel("Loan/Credit", (float) totalLoan, Color.parseColor("#FFF275")));
            if (totalVariable > 0)
                pieChart.addPieSlice(new PieModel("Variable", (float) totalVariable, Color.parseColor("#C492B1")));

        }
        //Slutligen startas en animation för PieChart.
        pieChart.startAnimation();
    }
}
