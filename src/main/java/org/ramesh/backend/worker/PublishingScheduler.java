package org.ramesh.backend.worker;

import org.ramesh.backend.service.PublishingWorkerService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class PublishingScheduler {
    private final PublishingWorkerService workerService;

    public PublishingScheduler(PublishingWorkerService workerService) {
        this.workerService = workerService;
    }

    @Scheduled(fixedDelay = 60_000)
    public void run() {
        workerService.runOnce();
    }
}
