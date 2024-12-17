package players;

import properties.Property;
import java.util.HashMap;

public class Player {

    public String name;

    private double assets_worth;
    private double liquid_wealth;
    private double income;
    private HashMap<String, Property> properties = new HashMap<String, Property>(28);
    public int id;

    public Player(String name, double money) {
        this.liquid_wealth = money;
        this.name = name;
    }

    public void adjustInflation(double rate) {
        income *= (1 + rate / 2); // salaries don't keep up with inflation
        assets_worth *= (1 + rate);
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
        double wealth = liquid_wealth + assets_worth;
        this.income = (double) wealth * 0.05 + 200;

        return this.income;
    }

    public double getIncome() {
        return income;
    }

    public double getWealth() {
        return liquid_wealth + assets_worth;
    }

    public double getAssetWealth() {
        return assets_worth;
    }

    public double getLiquidWealth() {
        return liquid_wealth;
    }

    public boolean addLiquidWealth(double money) {
        if (money < 0) {
            return false;
        }
        liquid_wealth += money;
        return true;
    }

    public boolean substractLiquidWealth(double money) {
        if (money < 0) {
            return false;
        }
        liquid_wealth -= money;
        return true;
    }

    public boolean addAsset(Property property) {
        if (properties.containsKey(property.name)) {
            // property is already added
            return false;
        }
        properties.put(property.name, property);
        return true;
    }

    public boolean removeAsset(Property property) {
        if (!properties.containsKey(property.name)) {
            // property not added
            return false;
        }
        properties.remove(property.name);
        return true;
    }

}
