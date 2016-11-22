package org.telegram.commands;

import org.telegram.mamot.services.DAO;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commands.BotCommand;

import static org.telegram.services.Stickers.THINK;

public class ITQuoteCommand extends BotCommand {

    private final DAO dao;

    public ITQuoteCommand(DAO dao) {
        super("it","Print IT quote");
        this.dao = dao;
    }

    @Override
    public void execute(AbsSender sender, User user, Chat chat, String[] strings) {
        new Answer(sender).to(chat)
                .sticker(THINK)
                .message(dao.getQuote()).disableWebPagePreview().enableHtml()
                .send();
    }
}