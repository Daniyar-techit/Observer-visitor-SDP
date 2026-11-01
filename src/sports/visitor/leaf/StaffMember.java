package sports.visitor.leaf;

import sports.visitor.SportElement;
import sports.visitor.SportVisitor;

public class StaffMember implements SportElement {
    private final String id;
    private final String name;
    private final String department;
    private final double salary;

    public StaffMember(String id, String name, String department, double salary) {
        this.id = id;
        this.name = name;
        this.department = department;
        this.salary = salary;
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public String getDepartment() { return department; }
    public double getSalary() { return salary; }

    @Override
    public <R> R accept(SportVisitor<R> visitor) {
        return visitor.visit(this);
    }
}