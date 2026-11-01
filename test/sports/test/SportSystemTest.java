package sports.test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import sports.concurrent.EventProducerPool;
import sports.observer.*;
import sports.observer.listeners.MatchAggregatorListener;
import sports.visitor.SportElement;
import sports.visitor.composite.Club;
import sports.visitor.composite.League;
import sports.visitor.leaf.Coach;
import sports.visitor.leaf.Player;
import sports.visitor.leaf.StaffMember;
import sports.visitor.leaf.Team;
import sports.visitor.operations.ElementFilterVisitor;
import sports.visitor.operations.JsonExportVisitor;
import sports.visitor.operations.RatingAccumulatorVisitor;
import sports.visitor.operations.SalaryCalculationVisitor;

import java.time.Instant;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

import static org.junit.jupiter.api.Assertions.*;

public class SportSystemTest {

    private League league;
    private Club club;
    private Player player1;
    private Player player2;
    private Coach coach;
    private StaffMember staff;
    private Team team;

    @BeforeEach
    void setUp() {
        league = new League("L1", "Test League");
        club = new Club("C1", "Test Club");
        team = new Team("T1", "First Team", 1000.0);
        player1 = new Player("P1", "Alice", "Forward", 500.0, 8.0);
        player2 = new Player("P2", "Bob", "Defender", 300.0, 6.0);
        coach = new Coach("CH1", "Charlie", "Manager", 800.0);
        staff = new StaffMember("S1", "David", "Med", 200.0);

        club.addElement(team);
        club.addElement(player1);
        club.addElement(player2);
        club.addElement(coach);
        club.addElement(staff);
        league.addClub(club);
    }

    @Test
    void testMatchStreamSourceRegistration() {
        MatchStreamSource source = new MatchStreamSource();
        AtomicInteger counter = new AtomicInteger(0);
        SportObserver observer = event -> counter.incrementAndGet();

        source.registerObserver(SportEventType.GOAL, observer);
        source.notifyObservers(new SportEvent("1", SportEventType.GOAL, "M1", "P1", "test", Instant.now()));

        assertEquals(1, counter.get());
    }

    @Test
    void testMatchStreamSourceUnsubscription() {
        MatchStreamSource source = new MatchStreamSource();
        AtomicInteger counter = new AtomicInteger(0);
        SportObserver observer = event -> counter.incrementAndGet();

        source.registerObserver(SportEventType.GOAL, observer);
        source.removeObserver(SportEventType.GOAL, observer);
        source.notifyObservers(new SportEvent("1", SportEventType.GOAL, "M1", "P1", "test", Instant.now()));

        assertEquals(0, counter.get());
    }

    @Test
    void testVarSystemSourceNotification() {
        VarSystemSource source = new VarSystemSource();
        AtomicInteger counter = new AtomicInteger(0);
        SportObserver observer = event -> counter.incrementAndGet();

        source.registerObserver(SportEventType.RED_CARD, observer);
        source.notifyObservers(new SportEvent("1", SportEventType.RED_CARD, "M1", "P1", "test", Instant.now()));

        assertEquals(1, counter.get());
    }

    @Test
    void testDynamicUnsubscriptionInsideHandler() {
        ThreadSafeRegistry registry = new ThreadSafeRegistry();
        AtomicInteger counter = new AtomicInteger(0);

        SportObserver selfRemovingObserver = new SportObserver() {
            @Override
            public void onEvent(SportEvent event) {
                counter.incrementAndGet();
                registry.removeObserver(SportEventType.FOUL, this);
            }
        };

        registry.registerObserver(SportEventType.FOUL, selfRemovingObserver);
        registry.notifyObservers(new SportEvent("1", SportEventType.FOUL, "M1", "P1", "test", Instant.now()));
        registry.notifyObservers(new SportEvent("2", SportEventType.FOUL, "M1", "P1", "test", Instant.now()));

        assertEquals(1, counter.get());
    }

    @Test
    void testAggregatorChainRepublishing() {
        MatchAggregatorListener aggregator = new MatchAggregatorListener();
        AtomicInteger chainedCounter = new AtomicInteger(0);
        SportObserver chainedObserver = event -> chainedCounter.incrementAndGet();

        aggregator.registerObserver(SportEventType.MATCH_SUMMARY_READY, chainedObserver);
        aggregator.onEvent(new SportEvent("1", SportEventType.GOAL, "M1", "P1", "test", Instant.now()));

        assertEquals(1, chainedCounter.get());
    }

    @Test
    void testSalaryCalculationVisitorManualCheck() {
        SalaryCalculationVisitor visitor = new SalaryCalculationVisitor();
        double total = league.accept(visitor);
        assertEquals(2800.0, total, 0.001);
    }

    @Test
    void testRatingAccumulatorVisitorManualCheck() {
        RatingAccumulatorVisitor visitor = new RatingAccumulatorVisitor();
        league.accept(visitor);
        assertEquals(7.0, visitor.getAverageRating(), 0.001);
    }

    @Test
    void testElementFilterVisitorByPosition() {
        ElementFilterVisitor visitor = new ElementFilterVisitor(element -> {
            if (element instanceof Player p) {
                return "Forward".equals(p.getPosition());
            }
            return false;
        });
        List<SportElement> result = league.accept(visitor);
        assertEquals(1, result.size());
        assertEquals("Alice", ((Player) result.get(0)).getName());
    }

    @Test
    void testElementFilterVisitorByLeague() {
        ElementFilterVisitor visitor = new ElementFilterVisitor(element -> element instanceof League);
        List<SportElement> result = league.accept(visitor);
        assertEquals(1, result.size());
    }

    @Test
    void testJsonExportVisitorFormat() {
        JsonExportVisitor visitor = new JsonExportVisitor();
        String json = league.accept(visitor);
        assertNotNull(json);
        assertTrue(json.contains("Test League"));
        assertTrue(json.contains("Test Club"));
        assertTrue(json.contains("Alice"));
    }

    @Test
    void testThreadSafeRegistryConcurrentAccess() {
        ThreadSafeRegistry registry = new ThreadSafeRegistry();
        SportObserver observer = event -> {};
        registry.registerObserver(SportEventType.GOAL, observer);
        assertFalse(registry.getObserversForType(SportEventType.GOAL).isEmpty());
        registry.removeObserver(SportEventType.GOAL, observer);
        assertTrue(registry.getObserversForType(SportEventType.GOAL).isEmpty());
    }

    @Test
    void testEventProducerPoolStressTest() throws InterruptedException {
        MatchStreamSource matchSource = new MatchStreamSource();
        VarSystemSource varSource = new VarSystemSource();

        AtomicInteger totalReceived = new AtomicInteger(0);
        SportObserver countingObserver = event -> totalReceived.incrementAndGet();

        matchSource.registerObserver(SportEventType.GOAL, countingObserver);
        matchSource.registerObserver(SportEventType.FOUL, countingObserver);
        varSource.registerObserver(SportEventType.GOAL, countingObserver);

        EventProducerPool pool = new EventProducerPool(4, matchSource, varSource);
        pool.startProducing(1000);

        Thread.sleep(3000);
        pool.shutdownGracefully();

        assertEquals(4000, pool.getTotalEventsCount());
    }

    @Test
    void testImmutableEventValidation() {
        SportEvent event = new SportEvent("E1", SportEventType.GOAL, "M1", "P1", "payload", null);
        assertNotNull(event.timestamp());
        assertEquals("E1", event.eventId());
        assertEquals(SportEventType.GOAL, event.type());
    }

    @Test
    void testLeagueAndClubHierarchy() {
        assertEquals(1, league.getClubs().size());
        assertEquals(5, league.getClubs().get(0).getChildren().size());
    }

    @Test
    void testEmptyVisitorResult() {
        League emptyLeague = new League("L2", "Empty");
        SalaryCalculationVisitor visitor = new SalaryCalculationVisitor();
        assertEquals(0.0, emptyLeague.accept(visitor), 0.001);
    }
}