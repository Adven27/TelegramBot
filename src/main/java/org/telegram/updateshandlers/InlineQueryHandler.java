package org.telegram.updateshandlers;

import org.telegram.services.RaeService;
import org.telegram.telegrambots.api.methods.AnswerInlineQuery;
import org.telegram.telegrambots.api.objects.inlinequery.InlineQuery;
import org.telegram.telegrambots.api.objects.inlinequery.inputmessagecontent.InputTextMessageContent;
import org.telegram.telegrambots.api.objects.inlinequery.result.InlineQueryResult;
import org.telegram.telegrambots.api.objects.inlinequery.result.InlineQueryResultArticle;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.logging.BotLogger;

import java.util.ArrayList;
import java.util.List;

public class InlineQueryHandler {
    private static final Integer CACHETIME = 86400;
    private static final String THUMBNAILBLUE = "https://lh5.ggpht.com/-kSFHGvQkFivERzyCNgKPIECtIOELfPNWAQdXqQ7uqv2xztxqll4bVibI0oHJYAuAas=w300";
    private static final String LOGTAG = "INLINEQUERYHANDLER";
    private final AbsSender sender;

    public InlineQueryHandler(AbsSender sender) {
        this.sender = sender;
    }

    void handleIncomingInlineQuery(InlineQuery inlineQuery) {
        String query = inlineQuery.getQuery();
        BotLogger.debug(LOGTAG, "Searching: " + query);
        try {
            List<RaeService.RaeResult> results = !query.isEmpty() ? new RaeService().getResults(query)
                                                                  : new ArrayList<>();
            sender.answerInlineQuery(converteResultsToResponse(inlineQuery, results));
        } catch (TelegramApiException e) {
            BotLogger.error(LOGTAG, e);
        }
    }

    static AnswerInlineQuery converteResultsToResponse(InlineQuery inlineQuery, List<RaeService.RaeResult> results) {
        AnswerInlineQuery a = new AnswerInlineQuery();
        a.setInlineQueryId(inlineQuery.getId());
        a.setCacheTime(CACHETIME);
        a.setResults(convertRaeResults(results));
        return a;
    }

    static List<InlineQueryResult> convertRaeResults(List<RaeService.RaeResult> raeResults) {
        List<InlineQueryResult> results = new ArrayList<InlineQueryResult>();
        for (int i = 0; i < raeResults.size(); i++) {
            RaeService.RaeResult raeResult = raeResults.get(i);
            InputTextMessageContent messageContent = new InputTextMessageContent();
            messageContent.disableWebPagePreview();
            messageContent.enableMarkdown(true);
            messageContent.setMessageText(raeResult.getDefinition());
            InlineQueryResultArticle article = new InlineQueryResultArticle();
            article.setInputMessageContent(messageContent);
            article.setId(Integer.toString(i));
            article.setTitle(raeResult.getTitle());
            article.setDescription(raeResult.getDescription());
            article.setThumbUrl(THUMBNAILBLUE);
            results.add(article);
        }
        return results;
    }
}