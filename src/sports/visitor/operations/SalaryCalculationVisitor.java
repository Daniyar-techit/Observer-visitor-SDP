package sports.visitor.operations;

import sports.visitor.SportVisitor;
import sports.visitor.composite.Club;
import sports.visitor.composite.League;
import sports.visitor.leaf.Coach;
import sports.visitor.leaf.Player;
import sports.visitor.leaf.StaffMember;
import sports.visitor.leaf.Team;
import sports.visitor.SportElement;

public class SalaryCalculationVisitor implements SportVisitor<Double> {

    @Override
    public Double visit(League league) {
        double total = 0.0;
        for (Club club : league.getClubs()) {
            total += club.accept(this);
        }
        return total;
    }

    @Override
    public Double visit(Club club) {
        double total = 0.0;
        for (SportElement element : club.getChildren()) {
            total += element.accept(this);
        }
        return total;
    }

    @Override
    public Double visit(Team team) {
        return team.getBudget();
    }

    @Override
    public Double visit(Player player) {
        return player.getSalary();
    }

    @Override
    public Double visit(Coach coach) {
        return coach.getSalary();
    }

    @Override
    public Double visit(StaffMember staffMember) {
        return staffMember.getSalary();
    }
}