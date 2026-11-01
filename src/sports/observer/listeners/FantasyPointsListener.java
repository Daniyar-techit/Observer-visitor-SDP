package sports.observer.listeners;

import sports.observer.SportEvent;
import sports.observer.SportObserver;

public class FantasyPointsListener implements SportObserver {
    @Override
    public void onEvent(SportEvent event) {
        System.out.println("[FANTASY POINTS] Updating fantasy points for participant or player ID: " + event.teamOrPlayerId());
    }
}