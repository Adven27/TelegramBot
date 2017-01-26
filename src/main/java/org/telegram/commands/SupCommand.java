package org.telegram.commands;

import org.telegram.fluent.Answer;
import org.telegram.services.DAO;
import org.telegram.services.Stickers;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commands.BotCommand;

public class SupCommand extends BotCommand {

    private final org.telegram.services.DAO DAO;

    public SupCommand(DAO dao) {
        super("sup","mamot loves you");
        DAO = dao;
    }

    @Override
    public void execute(AbsSender sender, User user, Chat chat, String[] strings) {
        new Answer(sender).to(chat)
            .sticker(Stickers.random())
            .message(DAO.getComplement()).disableWebPagePreview()
            .send();
    }
}