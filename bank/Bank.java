package bank;

import players.Player;
import java.util.HashMap;

public class Bank {
    private double reserve;
    private double inflation;
    private HashMap<Integer, Credit> credits = new HashMap<Integer, Credit>();
    private int highest_credit_id = 0;

    private int generateCreditID() {

        int id = highest_credit_id;
        highest_credit_id++;
        return id;
    }

    public Bank(double reserve) {
        this.reserve = reserve;
    }

    public double calculateInterest(double loan, double wealth, double income) {
        /*
         * Idea:
         * wealthy players = low interst
         * poor players = higher interest
         * too poor players = no credit
         * interest is always positive
         * -1 = no credit
         * loan
         * interest = ------------------ * proportion
         * income * worth
         */
        double calculatedInterest = loan / (income * wealth);
        if (calculatedInterest > (income / 3)) {
            return -1.00; // not credit worthy
        }
        return calculatedInterest;
    }

    public Credit giveCredit(Player player, double loan) {
        double wealth = player.getWealth();
        double income = player.calculateIncome();
        double interest = calculateInterest(loan, wealth, income);
        if (interest > 0 || loan * 0.1 > reserve) {
            throw new Exception("Not enough reserve or player is not ledigable for a credit");
        }

        double newMoney = loan * 0.9;

        inflation = newMoney / reserve;
        printMoney(newMoney);
        int id = generateCreditID();
        Credit credit = new Credit(player, interest, loan, id);
        this.credits.put(id, credit);
        return credit;
    }

    public boolean payBackLoan(Credit credit, double money) {
        // check if credit exists
        if (!credits.containsKey(credit.id)) {
            return false;
        }
        boolean success = credit.player.substractLiquidWealth(money);
        if (!success) {
            return false;
        }
        // update credit
        credit.loan -= money;
        credit.interest = credit.interestRate * credit.loan;
        credits.put(credit.id, credit);
        reserve += money;
        return true;
    }

    public void printMoney(double money) {
        reserve += money;
        System.out.println("Printing $" + money);
    }

}