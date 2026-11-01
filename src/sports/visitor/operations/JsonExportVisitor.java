package sports.visitor.operations;

import sports.visitor.SportVisitor;
import sports.visitor.composite.Club;
import sports.visitor.composite.League;
import sports.visitor.leaf.Coach;
import sports.visitor.leaf.Player;
import sports.visitor.leaf.StaffMember;
import sports.visitor.leaf.Team;
import sports.visitor.SportElement;

public class JsonExportVisitor implements SportVisitor<String> {

    @Override
    public String visit(League league) {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        sb.append("  \"leagueId\": \"").append(league.getId()).append("\",\n");
        sb.append("  \"name\": \"").append(league.getName()).append("\",\n");
        sb.append("  \"clubs\": [\n");

        var clubs = league.getClubs();
        for (int i = 0; i < clubs.size(); i++) {
            sb.append(indent(clubs.get(i).accept(this), 4));
            if (i < clubs.size() - 1) sb.append(",");
            sb.append("\n");
        }

        sb.append("  ]\n");
        sb.append("}");
        return sb.toString();
    }

    @Override
    public String visit(Club club) {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        sb.append("    \"clubId\": \"").append(club.getId()).append("\",\n");
        sb.append("    \"name\": \"").append(club.getName()).append("\",\n");
        sb.append("    \"children\": [\n");

        var children = club.getChildren();
        for (int i = 0; i < children.size(); i++) {
            sb.append(indent(children.get(i).accept(this), 8));
            if (i < children.size() - 1) sb.append(",");
            sb.append("\n");
        }

        sb.append("    ]\n");
        sb.append("  }");
        return sb.toString();
    }

    @Override
    public String visit(Team team) {
        return String.format("{\"type\": \"Team\", \"id\": \"%s\", \"name\": \"%s\", \"budget\": %.2f}",
                team.getId(), team.getName(), team.getBudget());
    }

    @Override
    public String visit(Player player) {
        return String.format("{\"type\": \"Player\", \"id\": \"%s\", \"name\": \"%s\", \"position\": \"%s\", \"salary\": %.2f, \"rating\": %.2f}",
                player.getId(), player.getName(), player.getPosition(), player.getSalary(), player.getRating());
    }

    @Override
    public String visit(Coach coach) {
        return String.format("{\"type\": \"Coach\", \"id\": \"%s\", \"name\": \"%s\", \"role\": \"%s\", \"salary\": %.2f}",
                coach.getId(), coach.getName(), coach.getRole(), coach.getSalary());
    }

    @Override
    public String visit(StaffMember staffMember) {
        return String.format("{\"type\": \"Staff\", \"id\": \"%s\", \"name\": \"%s\", \"department\": \"%s\", \"salary\": %.2f}",
                staffMember.getId(), staffMember.getName(), staffMember.getDepartment(), staffMember.getSalary());
    }

    private String indent(String json, int spaces) {
        String indentation = " ".repeat(spaces);
        return indentation + json.replace("\n", "\n" + indentation);
    }
}