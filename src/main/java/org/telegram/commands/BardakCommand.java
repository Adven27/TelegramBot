package org.telegram.commands;

import org.telegram.fluent.Answer;
import org.telegram.mamot.services.BardakMenu;
import org.telegram.mamot.services.DAO;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commands.BotCommand;

import static java.time.LocalDateTime.now;

public class BardakCommand extends BotCommand {

    private final org.telegram.mamot.services.DAO DAO;

    public BardakCommand(DAO dao) {
        super("bardak","Bardak menu");
        DAO = dao;
    }

    @Override
    public void execute(AbsSender sender, User user, Chat chat, String[] strings) {
        String msg = new BardakMenu(DAO).menu(now());
        new Answer(sender).to(chat)
                .message(msg).disableWebPagePreview()
                .send();
    }
}