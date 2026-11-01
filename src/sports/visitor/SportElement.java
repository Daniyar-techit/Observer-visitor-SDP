package sports.visitor;

import sports.visitor.SportVisitor;

public interface SportElement {
    <R> R accept(SportVisitor<R> visitor);
}