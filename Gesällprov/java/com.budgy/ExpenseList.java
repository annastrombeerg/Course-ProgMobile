package com.budgy;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

/**
 * ExpenseList är en RecyclerView-adapter som hanterar visningen av en lista med utgifter.
 * Den möjliggör även borttagning av enskilda utgifter via en delete-knapp.
 */
public class ExpenseList extends RecyclerView.Adapter<ExpenseList.ExpenseViewHolder> {
    private List<Expense> expenseList;
    private OnExpenseDeleteListener deleteListener;

    /**
     * Konstruktor för ExpenseList.
     *
     * @param expenseList   Listan med utgifter som ska visas i RecyclerView.
     * @param deleteListener En lyssnare för borttagning av utgifter.
     */
    public ExpenseList(List<Expense> expenseList, OnExpenseDeleteListener deleteListener) {
        this.expenseList = expenseList;
        this.deleteListener = deleteListener;
    }

    /**
     * Skapar en ny ViewHolder när en ny view behöver skapas.
     *
     * @param parent   Föräldervyn där den nya vyn kommer att placeras.
     * @param viewType Typen av view.
     * @return En ny ExpenseViewHolder som innehåller view.
     */
    @NonNull
    @Override
    public ExpenseViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.expense_list, parent, false);
        return new ExpenseViewHolder(view);
    }

    /**
     * Fyller en ViewHolder med data från en specifik position i listan.
     *
     * @param holder   ViewHolder som ska uppdateras.
     * @param position Positionen för det aktuella objektet i listan.
     */
    @Override
    public void onBindViewHolder(@NonNull ExpenseViewHolder holder, int position) {
        Expense expense = expenseList.get(position);
        holder.expenseName.setText(expense.getName());
        holder.expenseAmount.setText(expense.getAmount() + " KR");

        holder.deleteButton.setOnClickListener(v -> {
            if (deleteListener != null) {
                deleteListener.onDeleteExpense(position);
            }
        });
    }

    /**
     * Hämtar det totala antalet objekt i listan.
     *
     * @return Antalet utgifter i listan.
     */
    @Override
    public int getItemCount() {
        return expenseList.size();
    }

    /**
     * ExpenseViewHolder är en inre klass som hanterar view för en enskild utgift.
     */
    public static class ExpenseViewHolder extends RecyclerView.ViewHolder {
        TextView expenseName, expenseAmount;
        ImageButton deleteButton;

        /**
         * Konstruktor för ExpenseViewHolder.
         *
         * @param itemView Vyn som representerar en enskild utgift i listan.
         */
        public ExpenseViewHolder(@NonNull View itemView) {
            super(itemView);
            expenseName = itemView.findViewById(R.id.expense_name);
            expenseAmount = itemView.findViewById(R.id.expense_amount);
            deleteButton = itemView.findViewById(R.id.delete_button);
        }
    }

    /**
     * OnExpenseDeleteListener är ett interface för att hantera borttagning av en utgift.
     */
    public interface OnExpenseDeleteListener {
        /**
         * Metod som anropas när en utgift tas bort.
         *
         * @param position Positionen av den utgift som ska tas bort.
         */
        void onDeleteExpense(int position);
    }
}
