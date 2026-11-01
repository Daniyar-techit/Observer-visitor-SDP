package sports.observer;

import java.time.Instant;
import java.util.Objects;

public record SportEvent(
        String eventId,
        SportEventType type,
        String matchId,
        String teamOrPlayerId,
        String payload,
        Instant timestamp
) {
    public SportEvent {
        Objects.requireNonNull(eventId, "Event ID cannot be null");
        Objects.requireNonNull(type, "Event type cannot be null");
        Objects.requireNonNull(matchId, "Match ID cannot be null");
        timestamp = (timestamp == null) ? Instant.now() : timestamp;
    }
}