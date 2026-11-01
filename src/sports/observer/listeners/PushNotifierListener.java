package sports.observer.listeners;

import sports.observer.SportEvent;
import sports.observer.SportObserver;

public class PushNotifierListener implements SportObserver {
    @Override
    public void onEvent(SportEvent event) {
        System.out.println("[PUSH NOTIFIER] Sending push notification to users: event " + event.type() + " in match " + event.matchId());
    }
}