package org.telegram.commands;

import org.telegram.fluent.Answer;
import org.telegram.services.TwitterService;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.User;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.bots.commands.BotCommand;


/**
 * Created by k1per on 24.02.2017.
 */
public class TwitterGirlCommand extends BotCommand {

    TwitterService twitterService;
    private static final String GIRL_NAME_IN_TWITTER = "besseifunction";

    public TwitterGirlCommand(TwitterService twitterService) {
        super("scalagirl", "Latest tweet of scala girl");
        this.twitterService = twitterService;
    }

    @Override
    public void execute(AbsSender absSender, User user, Chat chat, String[] strings) {
        String msg = twitterService.getLatestTweet(GIRL_NAME_IN_TWITTER);
        new Answer(absSender).to(chat)
                .message(msg).disableWebPagePreview()
                .send();
    }
}
