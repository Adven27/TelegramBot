package org.telegram.commands;

import org.telegram.services.Weather;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commands.BotCommand;
import org.telegram.telegrambots.exceptions.TelegramApiException;
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
        try {
            SendMessage m = new SendMessage();
            m.disableWebPagePreview();
            m.enableMarkdown(true);
            m.setText(print());
            m.setChatId(chat.getId().toString());
            sender.sendMessage(m);
        } catch (TelegramApiException e) {
            e.printStackTrace();
        }
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