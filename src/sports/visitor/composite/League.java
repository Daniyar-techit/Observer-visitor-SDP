package sports.visitor.composite;

import sports.visitor.SportElement;
import sports.visitor.SportVisitor;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class League implements SportElement {
    private final String id;
    private final String name;
    private final List<Club> clubs = new ArrayList<>();

    public League(String id, String name) {
        this.id = Objects.requireNonNull(id, "League ID cannot be null");
        this.name = Objects.requireNonNull(name, "League name cannot be null");
    }

    public void addClub(Club club) {
        if (club != null) {
            clubs.add(club);
        }
    }

    public String getId() { return id; }
    public String getName() { return name; }
    public List<Club> getClubs() {
        return Collections.unmodifiableList(clubs);
    }

    @Override
    public <R> R accept(SportVisitor<R> visitor) {
        return visitor.visit(this);
    }
}