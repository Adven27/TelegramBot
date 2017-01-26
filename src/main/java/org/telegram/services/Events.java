package org.telegram.services;

import java.time.LocalTime;

import static org.telegram.services.Stickers.DRINK;
import static org.telegram.services.Stickers.EAT;
import static org.telegram.services.Stickers.RUN;

public enum Events {
    EVENING("В бардак???", DRINK, LocalTime.parse("19:00:00")),
    LUNCH("Обед!!!", EAT, LocalTime.parse("13:00:00")),
    TEA("Чай!!!", DRINK, LocalTime.parse("17:00:00")),
    MORNING("На работу!!!", RUN, LocalTime.parse("09:00:00"));

    private final String msg;
    private final Stickers sticker;
    private final LocalTime time;

    Events(String msg, Stickers sticker, LocalTime time) {
        this.msg = msg;
        this.sticker = sticker;
        this.time = time;
    }

    public String msg() {
        return msg;
    }

    public Stickers sticker() {
        return sticker;
    }

    public LocalTime time() {
        return time;
    }
}
