package sports.visitor.operations;

import sports.visitor.SportVisitor;
import sports.visitor.composite.Club;
import sports.visitor.composite.League;
import sports.visitor.leaf.Coach;
import sports.visitor.leaf.Player;
import sports.visitor.leaf.StaffMember;
import sports.visitor.leaf.Team;
import sports.visitor.SportElement;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

public class ElementFilterVisitor implements SportVisitor<List<SportElement>> {
    private final Predicate<SportElement> predicate;

    public ElementFilterVisitor(Predicate<SportElement> predicate) {
        this.predicate = predicate;
    }

    @Override
    public List<SportElement> visit(League league) {
        List<SportElement> result = new ArrayList<>();
        if (predicate.test(league)) result.add(league);
        for (Club club : league.getClubs()) {
            result.addAll(club.accept(this));
        }
        return result;
    }

    @Override
    public List<SportElement> visit(Club club) {
        List<SportElement> result = new ArrayList<>();
        if (predicate.test(club)) result.add(club);
        for (SportElement element : club.getChildren()) {
            result.addAll(element.accept(this));
        }
        return result;
    }

    @Override
    public List<SportElement> visit(Team team) {
        List<SportElement> result = new ArrayList<>();
        if (predicate.test(team)) result.add(team);
        return result;
    }

    @Override
    public List<SportElement> visit(Player player) {
        List<SportElement> result = new ArrayList<>();
        if (predicate.test(player)) result.add(player);
        return result;
    }

    @Override
    public List<SportElement> visit(Coach coach) {
        List<SportElement> result = new ArrayList<>();
        if (predicate.test(coach)) result.add(coach);
        return result;
    }

    @Override
    public List<SportElement> visit(StaffMember staffMember) {
        List<SportElement> result = new ArrayList<>();
        if (predicate.test(staffMember)) result.add(staffMember);
        return result;
    }
}