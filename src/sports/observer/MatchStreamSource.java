package sports.observer;

public class MatchStreamSource implements SportSubject {
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
        registry.notifyObservers(event);
    }

    @Override
    public java.util.Collection<SportObserver> getObserversForType(SportEventType type) {
        return registry.getObserversForType(type);
    }
}