package sports.observer;

import java.util.Collection;

public class VarSystemSource implements SportSubject {
    private final ThreadSafeRegistry registry = new ThreadSafeRegistry();

    @Override
    public void registerObserver(SportEventType type, SportObserver observer) {
        registry.registerObserver(type, observer);
    }

    @Override
    public void removeObserver(SportEventType type, SportObserver observer) {
        registry.removeObserver(type, observer);
    }

    @Override
    public void notifyObservers(SportEvent event) {
        System.out.println("[VAR SYSTEM] Referee notification " + event.type() + " on match " + event.matchId());
        registry.notifyObservers(event);
    }

    @Override
    public Collection<SportObserver> getObserversForType(SportEventType type) {
        return registry.getObserversForType(type);
    }
}