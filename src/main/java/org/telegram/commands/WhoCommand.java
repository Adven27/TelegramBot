package org.telegram.commands;

import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commands.BotCommand;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.logging.BotLogger;

import java.util.Random;

public class WhoCommand extends BotCommand {

    private static final String LOGTAG = "WHOCOMMAND";

    public WhoCommand() {
        super("who","who buys cookies..?");
    }

    @Override
    public void execute(AbsSender sender, User user, Chat chat, String[] strings) {
        try {
            if (strings.length == 0) {
                SendMessage msgReq = new SendMessage();
                msgReq.setChatId(chat.getId().toString());
                msgReq.setText("перечисли имена через пробел: /who эники беники покупали печеники");
                sender.sendMessage(msgReq);
            } else {
                SendMessage msgReq = new SendMessage();
                msgReq.setChatId(chat.getId().toString());
                msgReq.setText(strings[new Random().nextInt(strings.length)]);
                sender.sendMessage(msgReq);
            }
        } catch (TelegramApiException e) {
            BotLogger.error(LOGTAG, e);
        }
    }
}