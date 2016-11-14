package org.telegram.commands;

import org.telegram.mamot.services.BardakMenu;
import org.telegram.mamot.services.DAO;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commands.BotCommand;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.logging.BotLogger;

import static java.time.LocalDateTime.now;

public class BardakCommand extends BotCommand {

    private static final String LOGTAG = "BARDAKCOMMAND";
    private final org.telegram.mamot.services.DAO DAO;

    public BardakCommand(DAO dao) {
        super("bardak","Акциииии");
        DAO = dao;
    }

    @Override
    public void execute(AbsSender sender, User user, Chat chat, String[] strings) {
        try {
            SendMessage msgReq = new SendMessage();
            msgReq.setChatId(chat.getId().toString());
            msgReq.setText(new BardakMenu(DAO).menu(now()));
            msgReq.disableWebPagePreview();
            sender.sendMessage(msgReq);
        } catch (TelegramApiException e) {
            BotLogger.error(LOGTAG, e);
        }
    }
}