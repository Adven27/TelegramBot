package org.telegram.commands;

import org.telegram.mamot.services.DAO;
import org.telegram.services.Stickers;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.send.SendSticker;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commands.BotCommand;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.logging.BotLogger;

import java.util.Random;

public class SupCommand extends BotCommand {

    private static final String LOGTAG = "SUPCOMMAND";
    private final org.telegram.mamot.services.DAO DAO;

    public SupCommand(DAO dao) {
        super("sup","mamot loves you");
        DAO = dao;
    }

    @Override
    public void execute(AbsSender sender, User user, Chat chat, String[] strings) {
        try {
            SendSticker sticker = new SendSticker();
            sticker.setChatId(chat.getId().toString());
            sticker.setSticker(Stickers.values()[new Random().nextInt(Stickers.values().length)].getId());
            sender.sendSticker(sticker);

            SendMessage msgReq = new SendMessage();
            msgReq.setChatId(chat.getId().toString());
            msgReq.setText(DAO.getComplement());
            msgReq.disableWebPagePreview();
            sender.sendMessage(msgReq);
        } catch (TelegramApiException e) {
            BotLogger.error(LOGTAG, e);
        }
    }
}