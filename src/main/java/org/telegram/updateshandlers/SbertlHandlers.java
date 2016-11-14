package org.telegram.updateshandlers;

import org.telegram.BotConfig;
import org.telegram.mamot.services.BardakMenu;
import org.telegram.mamot.services.DAO;
import org.telegram.mamot.services.Mamorator;
import org.telegram.services.*;
import org.telegram.telegrambots.api.methods.AnswerInlineQuery;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.send.SendSticker;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.MessageEntity;
import org.telegram.telegrambots.api.objects.Sticker;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.api.objects.inlinequery.InlineQuery;
import org.telegram.telegrambots.api.objects.inlinequery.inputmessagecontent.InputTextMessageContent;
import org.telegram.telegrambots.api.objects.inlinequery.result.InlineQueryResult;
import org.telegram.telegrambots.api.objects.inlinequery.result.InlineQueryResultArticle;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.logging.BotLogger;

import java.util.ArrayList;
import java.util.List;

import static java.time.LocalDateTime.now;

public class SbertlHandlers extends TelegramLongPollingBot {

    /*
sup - sup nigga
bardak - bardak menu
who - who buys cookies?
weather - current weather...
quote - some random stuff from noosphere...
     */

    private static final String LOGTAG = "SBERTLHANDLERS";

    public static final String CHAT_ID = "-145229307";
    public static final DAO DAO = new DAO();
    public static final String BARDAK = "/bardak";

    private Mamorator mamorator;
    private BardakMenu bardakMenu;
    protected Weather weather;
    private final AnswerMessage[] answers;

    public SbertlHandlers(Weather weather, AnswerMessage... answers) {
        this.weather = weather;
        this.answers = answers;
        mamorator = new Mamorator(DAO);
        bardakMenu = new BardakMenu(DAO);
    }

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Message msg = update.getMessage();
            String chatId = msg.getChatId().toString();
            Sticker recievedSticker = msg.getSticker();
            String recievedStickerFileId = recievedSticker != null ? recievedSticker.getFileId() : "";
            try {
                if (msg.getEntities() == null || msg.getEntities().isEmpty()) {
                    if (!"".equals(recievedStickerFileId)) {
                        stickerHash(chatId, recievedStickerFileId);
                    } else {
                        sendText(chatId, mamorator.mamate(msg.getText()));
                    }
                } else {
                    for (MessageEntity e : msg.getEntities()) {
                        if (e.getType().equals("url")) {
                            sendText(chatId, "Link " + e.getText());
                        } else if (e.getType().equals("bot_command")) {
                            String cmd = e.getText();
                            for (AnswerMessage am : answers) {
                                if (am.is(cmd)) {
                                    am.answer(msg, this);
                                }
                            }
                            if (cmd.contains("/stickers")) {
                                showAllStickers(chatId);
                            } else if (cmd.equals(BARDAK)) {
                                sendText(chatId, bardakMenu.menu(now()));
                            } else if (cmd.startsWith("/die")) {
                                TimerExecutor.getInstance().stop();
                                System.exit(0);
                            }
                        }
                    }
                }
            } catch (TelegramApiException e) {
                BotLogger.error("SbertlHandlers", e);
            }
        } else if (update.hasInlineQuery()) {
            handleIncomingInlineQuery(update.getInlineQuery());
        }
    }

    private void handleIncomingInlineQuery(InlineQuery inlineQuery) {
        String query = inlineQuery.getQuery();
        BotLogger.debug(LOGTAG, "Searching: " + query);
        try {
            if (!query.isEmpty()) {
                List<RaeService.RaeResult> results = new RaeService().getResults(query);
                answerInlineQuery(converteResultsToResponse(inlineQuery, results));
            } else {
                answerInlineQuery(converteResultsToResponse(inlineQuery, new ArrayList<>()));
            }
        } catch (TelegramApiException e) {
            BotLogger.error(LOGTAG, e);
        }
    }

    private static AnswerInlineQuery converteResultsToResponse(InlineQuery inlineQuery, List<RaeService.RaeResult> results) {
        AnswerInlineQuery answer = new AnswerInlineQuery();
        answer.setInlineQueryId(inlineQuery.getId());
        answer.setCacheTime(CACHETIME);
        answer.setResults(convertRaeResults(results));
        return answer;
    }


    private static List<InlineQueryResult> convertRaeResults(List<RaeService.RaeResult> raeResults) {
        List<InlineQueryResult> results = new ArrayList<>();
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

    private static final Integer CACHETIME = 86400;
    private static final String THUMBNAILBLUE = "https://lh5.ggpht.com/-kSFHGvQkFivERzyCNgKPIECtIOELfPNWAQdXqQ7uqv2xztxqll4bVibI0oHJYAuAas=w300";


    private void stickerHash(String chatId, String recievedStickerFileId) throws TelegramApiException {
        sendText(chatId, recievedStickerFileId);
    }

    protected void sendSticker(String chatId, Stickers s) throws TelegramApiException {
        SendSticker sticker = new SendSticker();
        sticker.setChatId(chatId);
        sticker.setSticker(s.getId());
        sendSticker(sticker);
    }

    protected void sendText(String chatId, String text) throws TelegramApiException {
        SendMessage msgReq = new SendMessage();
        msgReq.setChatId(chatId);
        msgReq.setText(text);
        msgReq.disableWebPagePreview();
        sendMessage(msgReq);
    }

    private void showAllStickers(String chatId) throws TelegramApiException {
        for (Stickers s : Stickers.values()) {
            sendSticker(chatId, s);
        }
    }

    @Override
    public String getBotUsername() {
        return BotConfig.SBERTL_USER;
    }

    @Override
    public String getBotToken() {
        return BotConfig.SBERTL_TOKEN;
    }
}