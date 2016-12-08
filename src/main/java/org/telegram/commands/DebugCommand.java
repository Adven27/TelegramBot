package org.telegram.commands;

import org.telegram.fluent.Answer;
import org.telegram.timertasks.TimerExecutor;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commands.BotCommand;

public class DebugCommand extends BotCommand {

    public DebugCommand() {
        super("debug","some tech shit");
    }

    @Override
    public void execute(AbsSender sender, User user, Chat chat, String[] strings) {
        String msg = chat.toString() + "\n" + user.toString() + "\n" + TimerExecutor.getInstance().toString();


        new Answer(sender).to(chat)
                .message(msg).disableWebPagePreview()
                .send();
    }
}