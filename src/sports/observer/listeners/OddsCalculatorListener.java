package sports.observer.listeners;

import sports.observer.SportEvent;
import sports.observer.SportObserver;

public class OddsCalculatorListener implements SportObserver {
    @Override
    public void onEvent(SportEvent event) {
        System.out.println("[ODDS CALCULATOR] Recalculating live odds for match " + event.matchId() + " due to event: " + event.type());
    }
}