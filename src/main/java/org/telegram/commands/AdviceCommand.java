package org.telegram.commands;

import org.telegram.fluent.Answer;
import org.telegram.services.impl.MessageFromURL;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commands.BotCommand;

import static org.telegram.services.Stickers.BLA;

public class AdviceCommand extends BotCommand {

    private final MessageFromURL messageFromURL;

    public AdviceCommand(MessageFromURL messageFromURL) {
        super("advice", "Fucking great advice");
        this.messageFromURL = messageFromURL;
    }

    @Override
    public void execute(AbsSender sender, User user, Chat chat, String[] strings) {
        new Answer(sender).to(chat)
                .sticker(BLA)
                .message(messageFromURL.print()).enableHtml().disableWebPagePreview()
                .send();
    }
}