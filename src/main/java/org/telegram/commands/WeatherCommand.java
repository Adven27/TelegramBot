package org.telegram.commands;

import org.telegram.fluent.Answer;
import org.telegram.services.Weather;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commands.BotCommand;
import org.telegram.telegrambots.logging.BotLogger;

public class WeatherCommand extends BotCommand {

    private static final String LOGTAG = "WEATHERCOMMAND";
    private final Weather weather;

    public WeatherCommand(Weather weather) {
        super("weather", "current weather");
        this.weather = weather;
    }
    @Override
    public void execute(AbsSender sender, User user, Chat chat, String[] strings) {
        new Answer(sender).to(chat)
            .message(print()).disableWebPagePreview().enableMarkdown()
            .send();
    }

    public String print() {
        try {
            return weather.printCurrentFor(39.888599, 59.2187, "ru", "metric");
        } catch (Exception e) {
            BotLogger.error(LOGTAG, e);
            return "Связь с атмосферой потеряна...";
        }
    }
}