package sports.observer;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

public class ThreadSafeRegistry implements SportSubject {

    private final Map<SportEventType, Set<SportObserver>> registry = new ConcurrentHashMap<>();

    @Override
    public void registerObserver(SportEventType type, SportObserver observer) {
        Objects.requireNonNull(type, "Event type cannot be null");
        Objects.requireNonNull(observer, "Observer cannot be null");

        registry.computeIfAbsent(type, k -> new CopyOnWriteArraySet<>()).add(observer);
    }

    @Override
    public void removeObserver(SportEventType type, SportObserver observer) {
        Objects.requireNonNull(type, "Event type cannot be null");
        Objects.requireNonNull(observer, "Observer cannot be null");

        Set<SportObserver> observers = registry.get(type);
        if (observers != null) {
            observers.remove(observer);
        }
    }

    @Override
    public void notifyObservers(SportEvent event) {
        Objects.requireNonNull(event, "Event cannot be null");

        Set<SportObserver> observers = registry.get(event.type());
        if (observers != null) {
            for (SportObserver observer : observers) {
                observer.onEvent(event);
            }
        }
    }

    @Override
    public Collection<SportObserver> getObserversForType(SportEventType type) {
        Set<SportObserver> observers = registry.get(type);
        if (observers == null) {
            return Collections.emptySet();
        }
        return Collections.unmodifiableSet(observers);
    }
}