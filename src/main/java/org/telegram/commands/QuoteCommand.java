package org.telegram.commands;

import org.telegram.fluent.Answer;
import org.telegram.services.impl.MessageFromURL;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commands.BotCommand;

import static org.telegram.services.Stickers.THINK;

public class QuoteCommand extends BotCommand {

    private final MessageFromURL messageFromURL;

    public QuoteCommand(MessageFromURL messageFromURL) {
        super("quote", "Print cool quote");
        this.messageFromURL = messageFromURL;
    }

    @Override
    public void execute(AbsSender sender, User user, Chat chat, String[] strings) {
        new Answer(sender).to(chat)
                .sticker(THINK)
                .message(messageFromURL.print()).enableHtml().disableWebPagePreview()
                .send();
    }
}