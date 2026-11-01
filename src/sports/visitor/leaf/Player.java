package sports.visitor.leaf;

import sports.visitor.SportElement;
import sports.visitor.SportVisitor;

public class Player implements SportElement {
    private final String id;
    private final String name;
    private final String position;
    private final double salary;
    private final double rating;

    public Player(String id, String name, String position, double salary, double rating) {
        this.id = id;
        this.name = name;
        this.position = position;
        this.salary = salary;
        this.rating = rating;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getPosition() { return position; }
    public double getSalary() { return salary; }
    public double getRating() { return rating; }

    @Override
    public <R> R accept(SportVisitor<R> visitor) {
        return visitor.visit(this);
    }
}