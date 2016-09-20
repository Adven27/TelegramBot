package org.telegram.updateshandlers;

import org.telegram.BotConfig;
import org.telegram.mamot.services.BardakMenu;
import org.telegram.mamot.services.DAO;
import org.telegram.mamot.services.Mamorator;
import org.telegram.services.Stickers;
import org.telegram.telegrambots.TelegramApiException;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.send.SendSticker;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.Sticker;
import org.telegram.telegrambots.api.objects.Update;
import org.telegram.telegrambots.bots.TelegramLongPollingBot;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static java.time.LocalDateTime.now;
import static org.telegram.services.Stickers.DRINK;

public class SbertlHandlers extends TelegramLongPollingBot {

    /*
sup - sup nigga
bardak - bardak menu
who - who buys cookies?

     */

    public static final String EVENING_MSG = "В бардак???";
    public static final String MORNING_MSG = "На работу!!!";
    public static final String LUNCH_MSG = "Обед!!!";
    public static final String TEA_MSG = "Чай!!!";
    public static final String CHAT_ID = "-145229307";
    public static final DAO DAO = new DAO();
    public static final String SUP = "/sup";
    public static final String BARDAK = "/bardak";
    public static final String WHO = "/who";
    private int eveningMsgDay = -1;
    private int morningMsgDay = -1;
    private int lunchMsgDay = -1;
    private int teaMsgDay = -1;
    private Mamorator mamorator;
    private BardakMenu bardakMenu;

    public SbertlHandlers() {
        mamorator = new Mamorator(DAO);
        bardakMenu = new BardakMenu(DAO);
        exe.submit((Runnable) () -> {
            while (true) {
                LocalDateTime now = now();
                if (now.getHour() == 19 && eveningMsgDay != now.getDayOfYear()) {
                    try {
                        sendSticker(CHAT_ID, DRINK.getId());

                        SendMessage msgReq = new SendMessage();
                        msgReq.setChatId(CHAT_ID);
                        msgReq.setText(EVENING_MSG);
                        sendMessage(msgReq);
                        eveningMsgDay = now.getDayOfYear();
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                } else if (now.getHour() == 9 && morningMsgDay != now.getDayOfYear()) {
                    try {
                        sendSticker(CHAT_ID, Stickers.RUN.getId());

                        SendMessage msgReq = new SendMessage();
                        msgReq.setChatId(CHAT_ID);
                        msgReq.setText(MORNING_MSG);
                        sendMessage(msgReq);
                        morningMsgDay = now.getDayOfYear();
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                } else if (now.getHour() == 13 && lunchMsgDay != now.getDayOfYear()) {
                    try {
                        sendSticker(CHAT_ID, DRINK.getId());

                        SendMessage msgReq = new SendMessage();
                        msgReq.setChatId(CHAT_ID);
                        msgReq.setText(LUNCH_MSG);
                        sendMessage(msgReq);
                        lunchMsgDay = now.getDayOfYear();
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                } else if (now.getHour() == 17 && teaMsgDay != now.getDayOfYear()) {
                    try {
                        sendSticker(CHAT_ID, DRINK.getId());

                        SendMessage msgReq = new SendMessage();
                        msgReq.setChatId(CHAT_ID);
                        msgReq.setText(TEA_MSG);
                        sendMessage(msgReq);
                        teaMsgDay = now.getDayOfYear();
                    } catch (TelegramApiException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
    }

    private final ExecutorService exe = Executors.newSingleThreadExecutor();

    @Override
    public void onUpdateReceived(Update update) {
        if (update.hasMessage()) {
            Message msg = update.getMessage();
            String chatId = msg.getChatId().toString();
            Sticker recievedSticker = msg.getSticker();
            String recievedStickerFileId = recievedSticker != null ? recievedSticker.getFileId() : "";
            try {
                if (msg.hasText()) {
                    String text = msg.getText();
                    if (text.contains("/stickers")) {
                        showAllStickers(chatId);
                    } else if (text.contains(SUP)) {
                        sendSticker(chatId, Stickers.values()[new Random().nextInt(Stickers.values().length)].getId());
                        sendText(chatId, DAO.getComplement());
                    } else if (text.contains(BARDAK)) {
                        sendText(chatId, bardakMenu.menu(now()));
                    } else if (text.contains(WHO)) {
                        int beginIndex = text.indexOf(" ");
                        if (beginIndex == -1) {
                            sendText(chatId , "а кто есть?");
                        } else {
                            String vars = text.substring(beginIndex).trim();
                            String[] names = vars.split(" ");
                            sendText(chatId, names[new Random().nextInt(names.length)]);
                        }
                    } else {
                        sendText(chatId, mamorator.mamate(text));
                    }
                } else if (!"".equals(recievedStickerFileId)) {
                    sendText(chatId, recievedStickerFileId);
                    sendSticker(chatId, recievedStickerFileId);
                }
            } catch (TelegramApiException e) {
                //do some error handling
            }
        }
    }

    private void sendSticker(String chatId, String s) throws TelegramApiException {
        SendSticker sticker = new SendSticker();
        sticker.setChatId(chatId);
        sticker.setSticker(s);
        sendSticker(sticker);
    }

    private void sendText(String chatId, String text) throws TelegramApiException {
        SendMessage msgReq = new SendMessage();
        msgReq.setChatId(chatId);
        msgReq.setText(text);
        sendMessage(msgReq);
    }

    @Override
    public String getBotUsername() {
        return BotConfig.SBERTL_USER;
    }

    private void showAllStickers(String chatId) throws TelegramApiException {
        for (Stickers s : Stickers.values()) {
            sendSticker(chatId, s.getId());
        }
    }

    @Override
    public String getBotToken() {
        return BotConfig.SBERTL_TOKEN;
    }
}