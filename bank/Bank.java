package bank;

import players.Player;
import java.util.ArrayList;

public class Bank {
    private double reserve;
    private double inflation;
    private ArrayList<Credit> credits = new ArrayList<Credit>();

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
        Credit credit = new Credit(player, interest, loan);
        this.credits.add(credit);
        return credit;
    }

    public boolean payBackLoan(Credit credit, double money) {
        // TODO create HashMap from Player name to loan. All credits from a player get
        // sumed up
        boolean success = credit.player.substractLiquidWealth(money);
        if (!success) {
            return false;
        }
        credit.loan -= money;
        credit.interest = credit.interestRate * credit.loan;
        for (int i = 0; i < credits.size(); i++) {
            Credit element = credits.get(i);
            if (element == credit) {
                credits.set(i, credit);
            }
        }

    }

    public void printMoney(double money) {
        reserve += money;
        System.out.println("Printing $" + money);
    }

}