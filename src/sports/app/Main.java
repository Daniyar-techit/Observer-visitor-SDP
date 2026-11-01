package sports.app;

import sports.concurrent.EventProducerPool;
import sports.integration.MatchIntegrationService;
import sports.observer.MatchStreamSource;
import sports.observer.SportEventType;
import sports.observer.VarSystemSource;
import sports.observer.listeners.FantasyPointsListener;
import sports.observer.listeners.MatchAggregatorListener;
import sports.observer.listeners.OddsCalculatorListener;
import sports.observer.listeners.PushNotifierListener;
import sports.observer.listeners.ScoreboardListener;
import sports.visitor.composite.Club;
import sports.visitor.composite.League;
import sports.visitor.leaf.Coach;
import sports.visitor.leaf.Player;
import sports.visitor.leaf.StaffMember;
import sports.visitor.leaf.Team;
import sports.visitor.operations.JsonExportVisitor;
import sports.visitor.operations.RatingAccumulatorVisitor;
import sports.visitor.operations.SalaryCalculationVisitor;

public class Main {
    public static void main(String[] args) {
        League league = new League("LEAGUE-1", "Premier Live Sports League");
        Club club = new Club("CLUB-1", "Real Astana FC");

        Team team = new Team("TEAM-1", "Main Squad", 5000000.0);
        Player player1 = new Player("PLAYER-1", "Daniyar", "Forward", 1200000.0, 9.2);
        Player player2 = new Player("PLAYER-2", "Asset", "Midfielder", 900000.0, 8.5);
        Coach coach = new Coach("COACH-1", "Ancelotti", "Head Coach", 2000000.0);
        StaffMember staff = new StaffMember("STAFF-1", "Dr. House", "Medical", 300000.0);

        club.addElement(team);
        club.addElement(player1);
        club.addElement(player2);
        club.addElement(coach);
        club.addElement(staff);
        league.addClub(club);

        MatchStreamSource matchStreamSource = new MatchStreamSource();
        VarSystemSource varSystemSource = new VarSystemSource();

        ScoreboardListener scoreboard = new ScoreboardListener();
        OddsCalculatorListener oddsCalculator = new OddsCalculatorListener();
        FantasyPointsListener fantasyPoints = new FantasyPointsListener();
        PushNotifierListener pushNotifier = new PushNotifierListener();
        MatchAggregatorListener aggregator = new MatchAggregatorListener();

        MatchIntegrationService integrationService = new MatchIntegrationService(league);

        matchStreamSource.registerObserver(SportEventType.GOAL, scoreboard);
        matchStreamSource.registerObserver(SportEventType.GOAL, oddsCalculator);
        matchStreamSource.registerObserver(SportEventType.GOAL, fantasyPoints);
        matchStreamSource.registerObserver(SportEventType.GOAL, pushNotifier);
        matchStreamSource.registerObserver(SportEventType.GOAL, aggregator);

        matchStreamSource.registerObserver(SportEventType.FOUL, scoreboard);
        matchStreamSource.registerObserver(SportEventType.FOUL, pushNotifier);

        varSystemSource.registerObserver(SportEventType.GOAL, scoreboard);
        varSystemSource.registerObserver(SportEventType.RED_CARD, pushNotifier);

        aggregator.registerObserver(SportEventType.MATCH_SUMMARY_READY, integrationService);

        SalaryCalculationVisitor salaryVisitor = new SalaryCalculationVisitor();
        double totalSalaries = league.accept(salaryVisitor);
        System.out.println("Total league salaries and budgets: " + totalSalaries);

        RatingAccumulatorVisitor ratingVisitor = new RatingAccumulatorVisitor();
        league.accept(ratingVisitor);
        System.out.println("Average player rating: " + ratingVisitor.getAverageRating());

        JsonExportVisitor jsonVisitor = new JsonExportVisitor();
        String jsonOutput = league.accept(jsonVisitor);
        System.out.println("JSON Export preview:\n" + jsonOutput);

        EventProducerPool producerPool = new EventProducerPool(3, matchStreamSource, varSystemSource);
        producerPool.startProducing(100);

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        producerPool.shutdownGracefully();
        System.out.println("System shut down gracefully. Total events processed: " + producerPool.getTotalEventsCount());
    }
}