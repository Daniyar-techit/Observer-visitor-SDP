package sports.observer.listeners;

import sports.observer.SportEvent;
import sports.observer.SportEventType;
import sports.observer.SportObserver;

public class ScoreboardListener implements SportObserver {
    @Override
    public void onEvent(SportEvent event) {
        if (event.type() == SportEventType.GOAL) {
            System.out.println("[SCOREBOARD] Goal scored! Match: " + event.matchId() + " | Details: " + event.payload());
        } else if (event.type() == SportEventType.PERIOD_END) {
            System.out.println("[SCOREBOARD] Period ended in match: " + event.matchId());
        }
    }
}