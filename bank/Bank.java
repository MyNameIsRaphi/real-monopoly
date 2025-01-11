package bank;

import players.Player;
import java.util.HashMap;
import config.Config;
import game.Game;
import database.game.GameDB;

public class Bank {
    private double reserve;
    private double inflation;
    private HashMap<Integer, Credit> credits = new HashMap<Integer, Credit>();
    private int highest_credit_id = 0;
    @SuppressWarnings("unused")
    private int id;
    @SuppressWarnings("unused")
    private int game_id;
    private Config config = new Config("./config/config.json");

    private int generateCreditID() {

        int id = highest_credit_id;
        highest_credit_id++;
        return id;
    }

    public Bank(double reserve, int game_id) {
        this.reserve = reserve;
        this.game_id = game_id;
    }

    public Bank(double reserve, double inflation, int highest_credit_id, int id, int game_id) {
        this.reserve = reserve;
        this.inflation = inflation;
        this.highest_credit_id = highest_credit_id;
        this.id = id;
        this.game_id = game_id;
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

    public Credit giveCredit(Player player, double loan) throws RuntimeException {
        double wealth = player.getWealth();
        double income = player.calculateIncome();
        double interest = calculateInterest(loan, wealth, income);
        if (interest > 0 || loan * 0.1 > reserve) {
            throw new RuntimeException("Not enough reserve or player is not ledigable for a credit");
        }

        double newMoney = loan * 0.9;

        inflation = newMoney / reserve;
        printMoney(newMoney);
        int id = generateCreditID();
        Credit credit = new Credit(player.id, interest, loan, id);
        this.credits.put(id, credit);
        return credit;
    }

    public boolean payBackLoan(Credit credit, double money) {
        // check if credit exists
        if (!credits.containsKey(credit.getId())) {
            return false;
        }
        // get player
        GameDB gameDB = new GameDB(config.getDbAddress(), config.getDbUser(), config.getDbPassword(), game_id);
        Player player = gameDB.getPlayer(credit.getPlayerId());
        player.substractLiquidWealth(money);
        boolean success = player.substractLiquidWealth(money);
        // update player
        gameDB.updatePlayer(player);
        if (!success) {
            return false;
        }
        // update credit
        credit.loan -= money;
        credit.interest = credit.interestRate * credit.loan;
        credits.put(credit.getId(), credit);
        reserve += money;
        return true;
    }

    public void printMoney(double money) {
        reserve += money;
        System.out.println("Printing $" + money);
    }

    public double getReserve() {
        return reserve;
    }

    public double getInflation() {
        return inflation;
    }

    public int getHighestCreditId() {
        return highest_credit_id;
    }

    public int getGameId() {
        return game_id;
    }
}