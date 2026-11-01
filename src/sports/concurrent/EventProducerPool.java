package sports.concurrent;

import sports.observer.SportEvent;
import sports.observer.SportEventType;
import sports.observer.SportSubject;

import java.time.Instant;
import java.util.UUID;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public class EventProducerPool {
    private final int threadCount;
    private final SportSubject matchStreamSource;
    private final SportSubject varSystemSource;
    private ExecutorService executorService;
    private final AtomicInteger eventCounter = new AtomicInteger(0);

    public EventProducerPool(int threadCount, SportSubject matchStreamSource, SportSubject varSystemSource) {
        this.threadCount = threadCount;
        this.matchStreamSource = matchStreamSource;
        this.varSystemSource = varSystemSource;
    }

    public void startProducing(int eventsPerThread) {
        this.executorService = Executors.newFixedThreadPool(threadCount);

        for (int i = 0; i < threadCount; i++) {
            final int threadId = i;
            executorService.submit(() -> {
                for (int j = 0; j < eventsPerThread; j++) {
                    if (Thread.currentThread().isInterrupted()) {
                        break;
                    }
                    int currentId = eventCounter.incrementAndGet();
                    SportEventType type = (currentId % 2 == 0) ? SportEventType.GOAL : SportEventType.FOUL;
                    SportEvent event = new SportEvent(
                            UUID.randomUUID().toString(),
                            type,
                            "MATCH-10" + threadId,
                            "PLAYER-" + currentId,
                            "Payload data " + currentId,
                            Instant.now()
                    );

                    if (currentId % 3 == 0) {
                        varSystemSource.notifyObservers(event);
                    } else {
                        matchStreamSource.notifyObservers(event);
                    }
                }
            });
        }
    }

    public void shutdownGracefully() {
        if (executorService != null) {
            executorService.shutdown();
            try {
                if (!executorService.awaitTermination(5, TimeUnit.SECONDS)) {
                    executorService.shutdownNow();
                }
            } catch (InterruptedException e) {
                executorService.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }

    public int getTotalEventsCount() {
        return eventCounter.get();
    }
}