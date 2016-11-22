package org.telegram.commands;

import org.telegram.services.impl.MessageFromURL;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commands.BotCommand;

import static org.telegram.services.Stickers.LOL;

public class JokeCommand extends BotCommand {

    private final MessageFromURL messageFromURL;

    public JokeCommand(MessageFromURL messageFromURL) {
        super("ha","Print cool joke");
        this.messageFromURL = messageFromURL;
    }

    @Override
    public void execute(AbsSender sender, User user, Chat chat, String[] strings) {
        new Answer(sender).to(chat)
                .sticker(LOL)
                .message(messageFromURL.print()).disableWebPagePreview().enableHtml()
                .send();
    }
}