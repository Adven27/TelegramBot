package org.telegram.commands;

import org.telegram.services.Stickers;
import org.telegram.services.impl.MessageFromURL;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.send.SendSticker;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commands.BotCommand;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.logging.BotLogger;

public class JokeCommand extends BotCommand {

    private static final String LOGTAG = "JOKECOMMAND";
    private final MessageFromURL messageFromURL;

    public JokeCommand(MessageFromURL messageFromURL) {
        super("ha","Print cool joke");
        this.messageFromURL = messageFromURL;
    }

    @Override
    public void execute(AbsSender sender, User user, Chat chat, String[] strings) {
        try {
            SendSticker st = new SendSticker();
            st.setChatId(chat.getId().toString());
            //st.setReplyToMessageId(msg.getMessageId());
            st.setSticker(Stickers.LOL.getId());
            sender.sendSticker(st);

            SendMessage m = new SendMessage();
            m.disableWebPagePreview();
            m.enableHtml(true);
            m.setText(messageFromURL.print());
            m.setChatId(chat.getId().toString());
            sender.sendMessage(m);
        } catch (TelegramApiException e) {
            BotLogger.error(LOGTAG, e);
        }
    }
}