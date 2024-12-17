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

    public double value;
    public String name;
    public Category category;
    public int id;
    public int player_id;
}