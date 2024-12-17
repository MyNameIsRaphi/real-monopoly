package bank;

import players.Player;

public class Credit {
    public double interestRate;
    public double interest;
    public int id; // unique id
    public double loan;
    public Player player;

    public Credit(Player player, double interestRate, double loan, int id) {
        this.player = player;
        this.interestRate = interestRate;
        this.loan = loan;
        this.interest = loan * interestRate;
        this.id = id;
    }
}