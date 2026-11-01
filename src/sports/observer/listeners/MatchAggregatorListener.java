package sports.observer.listeners;

import sports.observer.SportEvent;
import sports.observer.SportEventType;
import sports.observer.SportObserver;
import sports.observer.SportSubject;

import java.time.Instant;
import java.util.Collection;
import java.util.UUID;
import java.util.concurrent.CopyOnWriteArrayList;

public class MatchAggregatorListener implements SportObserver, SportSubject {

    private final Collection<SportObserver> chainedObservers = new CopyOnWriteArrayList<>();

    @Override
    public void onEvent(SportEvent event) {
        System.out.println("[AGGREGATOR] Aggregating live data for match: " + event.matchId());

        if (event.type() == SportEventType.GOAL || event.type() == SportEventType.RED_CARD) {
            SportEvent aggregatedEvent = new SportEvent(
                    UUID.randomUUID().toString(),
                    SportEventType.MATCH_SUMMARY_READY,
                    event.matchId(),
                    event.teamOrPlayerId(),
                    "Aggregated summary payload for " + event.type(),
                    Instant.now()
            );
            notifyObservers(aggregatedEvent);
        }
    }

    @Override
    public void registerObserver(SportEventType type, SportObserver observer) {
        chainedObservers.add(observer);
    }

    @Override
    public void removeObserver(SportEventType type, SportObserver observer) {
        chainedObservers.remove(observer);
    }

    @Override
    public void notifyObservers(SportEvent event) {
        for (SportObserver observer : chainedObservers) {
            observer.onEvent(event);
        }
    }

    @Override
    public Collection<SportObserver> getObserversForType(SportEventType type) {
        return chainedObservers;
    }
}