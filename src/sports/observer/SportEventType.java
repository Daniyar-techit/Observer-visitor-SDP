package sports.observer;

public enum SportEventType {
    GOAL,
    FOUL,
    YELLOW_CARD,
    RED_CARD,
    PERIOD_END,
    MATCH_SUMMARY_READY // Событие, порожденное интеграцией с Visitor
}