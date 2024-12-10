package bank;

import players.Player;

public class Credit {
    public Player player;
    public double interestRate;
    public double interest;

    public double loan;

    public Credit(Player player, double interestRate, double loan) {
        this.player = player;
        this.interestRate = interestRate;
        this.loan = loan;
        this.interest = loan * interestRate;
    }
}