package org.telegram.timertasks;

import org.telegram.telegrambots.bots.AbsSender;

public abstract class CustomTimerTask {
    private String taskName = "";
    private int times = 1;
    protected AbsSender sender;

    public CustomTimerTask(String taskName, int times) {
        this.taskName = taskName;
        this.times = times;
    }

    public String getTaskName() {
        return this.taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    /**
     * Getter for the times
     *
     * @return Remainint times the task must be executed
     */
    public int getTimes() {
        return this.times;
    }

    /**
     * Setter for the times
     *
     * @param times Number of times the task must be executed
     */
    public void setTimes(int times) {
        this.times = times;
    }

    public void reduceTimes() {
        if (this.times > 0) {
            this.times -= 1;
        }
    }

    public abstract void execute();

    public abstract long computeDelay();

    public void setSender(AbsSender sender) {
        this.sender = sender;
    }
}