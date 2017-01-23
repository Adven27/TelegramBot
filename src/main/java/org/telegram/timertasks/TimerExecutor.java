package org.telegram.timertasks;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.telegram.telegrambots.logging.BotLogger.*;

public class TimerExecutor {
    private static final String LOGTAG = "TIMEREXECUTOR";
    private static volatile TimerExecutor instance;
    private static final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

    private TimerExecutor() {
    }

    public static TimerExecutor getInstance() {
        final TimerExecutor currentInstance;
        if (instance == null) {
            synchronized (TimerExecutor.class) {
                if (instance == null) {
                    instance = new TimerExecutor();
                }
                currentInstance = instance;
            }
        } else {
            currentInstance = instance;
        }

        return currentInstance;
    }

    public void schedule(CustomTimerTask task) {
        final Runnable taskWrapper = () -> {
            try {
                task.execute();
                task.reduceTimes();
                schedule(task);
            } catch (Exception e) {
                severe(LOGTAG, "Bot threw an unexpected exception at TimerExecutor", e);
            }
        };
        if (task.getTimes() != 0) {
            long time = task.computeDelay();
            executorService.schedule(taskWrapper, time, SECONDS);
        }
    }

    public void stop() {
        warn(LOGTAG, "Rejected tasks: " + executorService.shutdownNow().size());
    }

    @Override
    public String toString() {
        return executorService.toString();
    }

    @Override
    public void finalize() throws Throwable {
        try {
            this.stop();
        } finally {
            super.finalize();
        }
    }
}
