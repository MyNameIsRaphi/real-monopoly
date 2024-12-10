package players;

public class Player {

    private String name;
    private double assets;
    private double liquid_wealth;
    private double income;

    public void adjustInflation(double rate) {
        income *= (1 + rate / 2); // salaries don't keep up with inflation
        assets *= (1 + rate);
    }

    public double calculateInterest(double loan) {
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
        double wealth = liquid_wealth + assets;
        double calculatedInterest = loan / (income * wealth);
        if (calculatedInterest > (income / 3)) {
            return -1.00; // not credit worthy
        }
        return calculatedInterest;
    }

    public double calculateIncome() {
        /*
         * Idea:
         * In monopoly every player gets $200, when he crosses the starting point
         * But in reality people have different incomes. Usually wealthier induviduals
         * have higher incomes.
         * Forumla:
         * 
         * income = proportion * worth + 200
         * 
         */
        double wealth = liquid_wealth + assets;
        this.income = (double) wealth * 0.05 + 200;

        return this.income;
    }

    public double getIncome() {
        return income;
    }

    public double getWealth() {
        return liquid_wealth + assets;
    }

    public double getAssets() {
        return assets;
    }

    public double getLiquidWealth() {
        return liquid_wealth;
    }

}
