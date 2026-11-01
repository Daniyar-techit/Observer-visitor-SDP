package sports.visitor.operations;

import sports.visitor.SportVisitor;
import sports.visitor.composite.Club;
import sports.visitor.composite.League;
import sports.visitor.leaf.Coach;
import sports.visitor.leaf.Player;
import sports.visitor.leaf.StaffMember;
import sports.visitor.leaf.Team;
import sports.visitor.SportElement;

public class RatingAccumulatorVisitor implements SportVisitor<Void> {
    private double totalRating = 0.0;
    private int playerCount = 0;

    @Override
    public Void visit(League league) {
        for (Club club : league.getClubs()) {
            club.accept(this);
        }
        return null;
    }

    @Override
    public Void visit(Club club) {
        for (SportElement element : club.getChildren()) {
            element.accept(this);
        }
        return null;
    }

    @Override
    public Void visit(Team team) {
        return null;
    }

    @Override
    public Void visit(Player player) {
        totalRating += player.getRating();
        playerCount++;
        return null;
    }

    @Override
    public Void visit(Coach coach) {
        return null;
    }

    @Override
    public Void visit(StaffMember staffMember) {
        return null;
    }

    public double getAverageRating() {
        return playerCount == 0 ? 0.0 : totalRating / playerCount;
    }
}