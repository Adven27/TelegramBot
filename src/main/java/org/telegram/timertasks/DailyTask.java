package org.telegram.timertasks;

import java.time.LocalDateTime;

import static java.time.LocalDateTime.now;
import static org.telegram.timertasks.DateUtils.getDurationInMillis;

abstract public class DailyTask extends CustomTimerTask {

    public DailyTask(String taskName, int times) {
        super(taskName, times);
    }

    @Override
    public long computeDelay() {
        final LocalDateTime now = now();
        LocalDateTime nextTime = startAt();
        while (getDurationInMillis(now, nextTime) < 0) {
            nextTime = nextTime.plusDays(1);
        }
        return getDurationInMillis(now, nextTime);
    }

    protected abstract LocalDateTime startAt();
}
