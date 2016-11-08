package org.telegram.updateshandlers;

import org.telegram.services.*;
import org.telegram.services.impl.JokePrinter;
import org.telegram.services.impl.QuoteService;
import org.telegram.telegrambots.TelegramApiException;

import java.time.LocalTime;

import static org.telegram.services.Stickers.DRINK;
import static org.telegram.services.Stickers.RUN;
import static org.telegram.services.Stickers.THINK;

public class TimeHandlers extends SbertlHandlers {

    public TimeHandlers(final WeatherService weatherService, final QuoteService quoteService, final JokePrinter jokePrinter) {
        super(weatherService, quoteService, jokePrinter);
        for (Events e : Events.values()) {
            startAlertTimers(e);
        }
        TimerExecutor.getInstance().startExecutionOnRandomHourAt(new CustomTimerTask("Quotes", -1) {
            @Override
            public void execute() {
                sendAlerts(quoteService.fetchQuote(), THINK);
            }
        }, 18 , 23, 0);
    }

    private void startAlertTimers(Events e) {
        TimerExecutor.getInstance().startExecutionEveryDayAt(new CustomTimerTask(e.name(), -1) {
            @Override
            public void execute() {
                sendAlerts(e.msg, e.sticker);
            }
        }, e.time.getHour(), e.time.getMinute(), e.time.getSecond());
    }

    private void sendAlerts(String msg, Stickers sticker) {
        try {
            sendSticker(CHAT_ID, sticker);
            sendText(CHAT_ID, msg);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
    }

    public enum Events {
        EVENING("В бардак???", DRINK, LocalTime.parse("19:00:00")),
        LUNCH("Обед!!!", DRINK, LocalTime.parse("13:00:00")),
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
    }
}
