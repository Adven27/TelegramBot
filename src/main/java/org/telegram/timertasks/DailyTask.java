package org.telegram.timertasks;

import java.time.Duration;
import java.time.LocalDateTime;

import static java.time.LocalDateTime.now;

abstract public class DailyTask extends CustomTimerTask {

    public DailyTask(String taskName, int times) {
        super(taskName, times);
    }

    @Override
    public long computeDelay() {
        final LocalDateTime now = now();
        LocalDateTime nextTime = startAt();
        while (now.compareTo(nextTime) > 0) {
            nextTime = nextTime.plusDays(1);
        }
        return Duration.between(now, nextTime).getSeconds();
    }

    protected abstract LocalDateTime startAt();
}
