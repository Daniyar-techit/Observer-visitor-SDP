package sports.visitor.leaf;

import sports.visitor.SportElement;
import sports.visitor.SportVisitor;

public class Team implements SportElement {
    private final String id;
    private final String name;
    private final double budget;

    public Team(String id, String name, double budget) {
        this.id = id;
        this.name = name;
        this.budget = budget;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public double getBudget() { return budget; }

    @Override
    public <R> R accept(SportVisitor<R> visitor) {
        return visitor.visit(this);
    }
}