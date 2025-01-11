package bank;

import players.Player;

public class Credit {
    public double interestRate;
    public double interest;
    private int id; // unique id
    public double loan;
    private int player_id;

    public Credit(int player_id, double loan, double interestRate, int id) {
        this.player_id = player_id;
        this.interestRate = interestRate;
        this.loan = loan;
        this.interest = loan * interestRate;
        this.id = id;
    }

    public double getInterestRate() {
        return interestRate;
    }

    public double getInterest() {
        return interest;
    }

    public int getId() {
        return id;
    }

    public double getLoan() {
        return loan;
    }

    public int getPlayerId() {
        return player_id;
    }
}