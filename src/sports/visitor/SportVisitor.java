package sports.visitor;

import sports.visitor.composite.Club;
import sports.visitor.composite.League;
import sports.visitor.leaf.Coach;
import sports.visitor.leaf.Player;
import sports.visitor.leaf.StaffMember;
import sports.visitor.leaf.Team;

public interface SportVisitor<R> {
    R visit(League league);
    R visit(Club club);
    R visit(Team team);
    R visit(Player player);
    R visit(Coach coach);
    R visit(StaffMember staffMember);
}