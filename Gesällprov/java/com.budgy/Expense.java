package com.budgy;

import java.util.ArrayList;
import java.util.List;

/**
 * Expense-klassen hanterar inkomster och utgifter i olika kategorier:
 * fasta utgifter, lån och krediter samt rörliga utgifter.
 * Den möjliggör att lägga till, hämta, beräkna totaler och ta bort utgifter.
 */
public class Expense {
    private static double income = 0;
    private static final List<Expense> fixedExpenses = new ArrayList<>();
    private static final List<Expense> loanCredits = new ArrayList<>();
    private static final List<Expense> variableExpenses = new ArrayList<>();
    private String name;
    private double amount;

    /**
     * Konstruktor som skapar en ny Expense-instans med ett namn och ett belopp.
     *
     * @param name   Namnet på utgiften.
     * @param amount Beloppet för utgiften.
     */
    public Expense(String name, double amount) {
        this.name = name;
        this.amount = amount;
    }

    /**
     * Hämtar namnet på utgiften.
     *
     * @return Namnet på utgiften.
     */
    public String getName() {
        return name;
    }

    /**
     * Hämtar beloppet för utgiften.
     *
     * @return Beloppet för utgiften.
     */
    public double getAmount() {
        return amount;
    }

    /**
     * Sätter inkomsten till ett nytt belopp.
     *
     * @param newIncome Det nya inkomstbeloppet.
     */
    public static void setIncome(double newIncome) {
        income = newIncome;
    }

    /**
     * Hämtar den aktuella inkomsten.
     *
     * @return Det aktuella inkomstbeloppet.
     */
    public static double getIncome() {
        return income;
    }

    /**
     * Lägger till en fast utgift i listan över fasta utgifter.
     *
     * @param name   Namnet på den fasta utgiften.
     * @param amount Beloppet för den fasta utgiften.
     */
    public static void addFixedExpense(String name, double amount) {
        fixedExpenses.add(new Expense(name, amount));
    }

    /**
     * Lägger till en låne- eller kreditutgift i listan över lån och krediter.
     *
     * @param name   Namnet på lånet eller krediten.
     * @param amount Beloppet för lånet eller krediten.
     */
    public static void addLoanCredit(String name, double amount) {
        loanCredits.add(new Expense(name, amount));
    }

    /**
     * Lägger till en rörlig utgift i listan över rörliga utgifter.
     *
     * @param name   Namnet på den rörliga utgiften.
     * @param amount Beloppet för den rörliga utgiften.
     */
    public static void addVariableExpense(String name, double amount) {
        variableExpenses.add(new Expense(name, amount));
    }

    /**
     * Beräknar och returnerar den totala summan av alla fasta utgifter.
     *
     * @return Summan av alla fasta utgifter.
     */
    public static double getTotalFixedExpenses() {
        return fixedExpenses.stream().mapToDouble(e -> e.amount).sum();
    }

    /**
     * Beräknar och returnerar den totala summan av alla lån och krediter.
     *
     * @return Summan av alla lån och krediter.
     */
    public static double getTotalLoanCredits() {
        return loanCredits.stream().mapToDouble(e -> e.amount).sum();
    }

    /**
     * Beräknar och returnerar den totala summan av alla rörliga utgifter.
     *
     * @return Summan av alla rörliga utgifter.
     */
    public static double getTotalVariableExpenses() {
        return variableExpenses.stream().mapToDouble(e -> e.amount).sum();
    }

    /**
     * Hämtar en lista över alla fasta utgifter.
     *
     * @return En ny lista med alla fasta utgifter.
     */
    public static List<Expense> getFixedExpenses() {
        return new ArrayList<>(fixedExpenses);
    }

    /**
     * Hämtar en lista över alla lån och krediter.
     *
     * @return En ny lista med alla lån och krediter.
     */
    public static List<Expense> getLoanCreditExpenses() {
        return new ArrayList<>(loanCredits);
    }

    /**
     * Hämtar en lista över alla rörliga utgifter.
     *
     * @return En ny lista med alla rörliga utgifter.
     */
    public static List<Expense> getVariableExpenses() {
        return new ArrayList<>(variableExpenses);
    }

    /**
     * Återställer alla data i Expense-klassen, inklusive inkomster och utgifter.
     */
    public static void resetData() {
        income = 0;
        fixedExpenses.clear();
        loanCredits.clear();
        variableExpenses.clear();
    }

    /**
     * Tar bort en fast utgift från listan baserat på dess position.
     *
     * @param position Indexet för den utgift som ska tas bort.
     */
    public static void removeFixedExpense(int position) {
        if (position >= 0 && position < fixedExpenses.size()) {
            fixedExpenses.remove(position);
        }
    }

    /**
     * Tar bort en låne- eller kreditutgift från listan baserat på dess position.
     *
     * @param position Indexet för den utgift som ska tas bort.
     */
    public static void removeLoanCreditExpense(int position) {
        if (position >= 0 && position < loanCredits.size()) {
            loanCredits.remove(position);
        }
    }

    /**
     * Tar bort en rörlig utgift från listan baserat på dess position.
     *
     * @param position Indexet för den utgift som ska tas bort.
     */
    public static void removeVariableExpense(int position) {
        if (position >= 0 && position < variableExpenses.size()) {
            variableExpenses.remove(position);
        }
    }
}
