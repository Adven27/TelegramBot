package org.telegram.commands;

import org.telegram.services.TimerExecutor;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commands.BotCommand;

public class DieCommand extends BotCommand {

    public DieCommand() {
        super("die","R.I.P. mamot");
    }

    @Override
    public void execute(AbsSender sender, User user, Chat chat, String[] strings) {
        TimerExecutor.getInstance().stop();
        System.exit(0);
    }
}