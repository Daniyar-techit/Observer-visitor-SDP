package sports.visitor.composite;

import sports.visitor.SportElement;
import sports.visitor.SportVisitor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class Club implements SportElement {
    private final String id;
    private final String name;
    private final List<SportElement> children = new ArrayList<>();

    public Club(String id, String name) {
        this.id = Objects.requireNonNull(id, "Club ID cannot be null");
        this.name = Objects.requireNonNull(name, "Club name cannot be null");
    }

    public void addElement(SportElement element) {
        if (element != null) {
            children.add(element);
        }
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public List<SportElement> getChildren() {
        return Collections.unmodifiableList(children);
    }

    @Override
    public <R> R accept(SportVisitor<R> visitor) {
        return visitor.visit(this);
    }
}