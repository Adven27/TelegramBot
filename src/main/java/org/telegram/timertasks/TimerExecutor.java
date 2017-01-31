package org.telegram.timertasks;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.telegram.telegrambots.logging.BotLogger.severe;
import static org.telegram.telegrambots.logging.BotLogger.warn;

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
        if (task.getTimes() != 0) {
            long delay = task.computeDelay();
            executorService.schedule(getWrapped(task), delay, MILLISECONDS);
        }
    }

    private Runnable getWrapped(CustomTimerTask task) {
        return () -> {
                try {
                    task.execute();
                    task.reduceTimes();
                    schedule(task);
                } catch (Exception e) {
                    severe(LOGTAG, "Bot threw an unexpected exception at TimerExecutor", e);
                }
            };
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
