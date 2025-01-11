package properties;

public class Property {
    enum Category {
        BLUE, // Blue properties (e.g., Zürich Paradeplatz)
        GREEN, // Green properties (e.g., Bern Bundesplatz)
        YELLOW, // Yellow properties (e.g., Chur Altstadt)
        RED, // Red properties (e.g., Zermatt Matterhorn)
        ORANGE, // Orange properties (e.g., St. Moritz Skigebiet)
        PINK, // Pink properties (e.g., Schaffhausen Rheinfall)
        LIGHT_BLUE, // Light Blue properties (e.g., Biel Altstadt)
        RAILROAD, // Railroad stations (e.g., Bahnhof Luzern)
    }

    private String name;
    private String category;
    private double value;
    private int player_id;
    private double price_bought;
    private int id;

    public Property(String name, String category, double value, int player_id, double price_bought, int id) {
        this.name = name;
        this.category = category;
        this.value = value;
        this.player_id = player_id;
        this.price_bought = price_bought;
        this.id = id;
    }

    // Getters
    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public double getValue() {
        return value;
    }

    public int getPlayerId() {
        return player_id;
    }

    public double getPriceBought() {
        return price_bought;
    }

    public int getId() {
        return id;
    }

    public void setValue(double value) {
        this.value = value;
    }

    public void setPlayerId(int player_id) {
        this.player_id = player_id;
    }

    public void setPriceBought(double price_bought) {
        this.price_bought = price_bought;
    }
}