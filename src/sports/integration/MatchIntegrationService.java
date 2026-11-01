package sports.integration;

import sports.observer.SportEvent;
import sports.observer.SportEventType;
import sports.observer.SportObserver;
import sports.visitor.composite.Club;
import sports.visitor.composite.League;
import sports.visitor.operations.SalaryCalculationVisitor;

public class MatchIntegrationService implements SportObserver {
    private final League rootLeague;

    public MatchIntegrationService(League rootLeague) {
        this.rootLeague = rootLeague;
    }

    @Override
    public void onEvent(SportEvent event) {
        if (event.type() == SportEventType.MATCH_SUMMARY_READY) {
            System.out.println("[INTEGRATION C1] Triggering Visitor on specific subtree for match: " + event.matchId());

            Club targetClub = findClubById(rootLeague, event.teamOrPlayerId());

            if (targetClub != null) {
                SalaryCalculationVisitor salaryVisitor = new SalaryCalculationVisitor();
                Double clubBudgetAndSalaries = targetClub.accept(salaryVisitor);
                System.out.println("[INTEGRATION C1] Subtree Visitor Result -> Club '" + targetClub.getName() +
                        "' total financial burden: " + clubBudgetAndSalaries);
            } else {
                SalaryCalculationVisitor salaryVisitor = new SalaryCalculationVisitor();
                Double totalLeagueSalaries = rootLeague.accept(salaryVisitor);
                System.out.println("[INTEGRATION C1] League-wide Visitor Result -> Total financial burden: " + totalLeagueSalaries);
            }
        }
    }

    private Club findClubById(League league, String clubId) {
        if (clubId == null) return null;
        for (Club club : league.getClubs()) {
            if (club.getId().equals(clubId)) {
                return club;
            }
        }
        return league.getClubs().isEmpty() ? null : league.getClubs().get(0);
    }
}