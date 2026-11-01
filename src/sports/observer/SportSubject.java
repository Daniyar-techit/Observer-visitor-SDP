package sports.observer;

import java.util.Collection;

public interface SportSubject {
    void registerObserver(SportEventType type, SportObserver observer);
    void removeObserver(SportEventType type, SportObserver observer);
    void notifyObservers(SportEvent event);
    Collection<SportObserver> getObserversForType(SportEventType type);
}