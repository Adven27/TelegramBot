package org.telegram.services;

import org.telegram.telegrambots.logging.BotLogger;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

import static java.time.LocalDateTime.now;
import static java.util.concurrent.TimeUnit.SECONDS;

public class TimerExecutor {
    private static final String LOGTAG = "TIMEREXECUTOR";
    private static volatile TimerExecutor instance;
    private static final ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1); ///< Thread to execute operations

    /**
     * Private constructor due to singleton
     */
    private TimerExecutor() {
    }

    /**
     * Singleton pattern
     *
     * @return Instance of the executor
     */
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

    /**
     * Add a new CustomTimerTask to be executed
     *
     * @param task       Task to execute
     * @param targetHour Hour to execute it
     * @param targetMin  Minute to execute it
     * @param targetSec  Second to execute it
     */
    public void startExecutionEveryDayAt(CustomTimerTask task, int targetHour, int targetMin, int targetSec) {
        BotLogger.warn(LOGTAG, "Posting new task " + task.getTaskName() + " " + executorService.toString());
        final Runnable taskWrapper = () -> {
            try {
                task.execute();
                task.reduceTimes();
                startExecutionEveryDayAt(task, targetHour, targetMin, targetSec);
                BotLogger.info(LOGTAG, "execute " + executorService.toString());
            } catch (Exception e) {
                BotLogger.severe(LOGTAG, "Bot threw an unexpected exception at TimerExecutor", e);
            }
        };
        if (task.getTimes() != 0) {
            executorService.schedule(taskWrapper, computeNextDelay(targetHour, targetMin, targetSec), SECONDS);
            BotLogger.warn(LOGTAG, "Schedule at " + targetHour + ":" + targetMin + ":" + targetSec + " " + task.getTaskName() + " " + executorService.toString());
        }
    }

    public void startExecutionOnRandomHourAt(CustomTimerTask task, int targetHour, int targetMin, int targetSec) {
        BotLogger.warn(LOGTAG, "Posting new task " + task.getTaskName() + " " + executorService.toString());
        final Runnable taskWrapper = () -> {
            try {
                task.execute();
                task.reduceTimes();
                startExecutionOnRandomHourAt(task, new Random().nextInt(12) + 9 , new Random().nextInt(59), 0);
                BotLogger.info(LOGTAG, "execute " + executorService.toString());
            } catch (Exception e) {
                BotLogger.severe(LOGTAG, "Bot threw an unexpected exception at TimerExecutor", e);
            }
        };
        if (task.getTimes() != 0) {
            long nextHour = computeNextHour(targetHour, targetMin, targetSec);
            executorService.schedule(taskWrapper, nextHour, SECONDS);
            BotLogger.warn(LOGTAG, "Schedule at " + targetHour + ":" + targetMin + ":" + targetSec + " " + task.getTaskName() + " " + executorService.toString());
        }
    }

    private long computeNextDelay(int targetHour, int targetMin, int targetSec) {
        final LocalDateTime now = now();
        LocalDateTime nextTime = now.withHour(targetHour).withMinute(targetMin).withSecond(targetSec);
        while (now.compareTo(nextTime) > 0) {
            nextTime = nextTime.plusDays(1);
        }
        return Duration.between(now, nextTime).getSeconds();
    }

    private long computeNextHour(int targetHour, int targetMin, int targetSec) {
        final LocalDateTime now = now();
        LocalDateTime nextTime = now.withHour(targetHour).withMinute(targetMin).withSecond(targetSec);
        while (now.compareTo(nextTime) > 0 && nextTime.getHour() > 8 && nextTime.getHour() < 22) {
            nextTime = nextTime.plusHours(new Random().nextInt(3) + 1);
        }
        return Duration.between(now, nextTime).getSeconds();
    }

    @Override
    public void finalize() {
        this.stop();
    }

    public void stop() {
        BotLogger.warn(LOGTAG, "Rejected tasks: " + executorService.shutdownNow().size());
    }

    @Override
    public String toString() {
        return executorService.toString();
    }
}
