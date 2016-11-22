package org.telegram.commands;

import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commands.BotCommand;

import java.util.Random;

public class WhoCommand extends BotCommand {

    public WhoCommand() {
        super("who","who buys cookies..?");
    }

    @Override
    public void execute(AbsSender sender, User user, Chat chat, String[] strings) {
        Answer answer = new Answer(sender).to(chat);
        String m = strings.length == 0 ? "перечисли имена через пробел: /who эники беники покупали печеники"
                                         : strings[new Random().nextInt(strings.length)];
        answer.message(m).send();
    }
}