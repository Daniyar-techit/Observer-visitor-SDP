package sports.visitor.leaf;

import sports.visitor.SportElement;
import sports.visitor.SportVisitor;

public class Coach implements SportElement {
    private final String id;
    private final String name;
    private final String role;
    private final double salary;

    public Coach(String id, String name, String role, double salary) {
        this.id = id;
        this.name = name;
        this.role = role;
        this.salary = salary;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getRole() { return role; }
    public double getSalary() { return salary; }

    @Override
    public <R> R accept(SportVisitor<R> visitor) {
        return visitor.visit(this);
    }
}