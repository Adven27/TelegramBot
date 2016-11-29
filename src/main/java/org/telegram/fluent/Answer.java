package org.telegram.fluent;

import org.telegram.services.Stickers;
import org.telegram.telegrambots.api.methods.send.SendMessage;
import org.telegram.telegrambots.api.methods.send.SendSticker;
import org.telegram.telegrambots.api.objects.Chat;
import org.telegram.telegrambots.api.objects.replykeyboard.ReplyKeyboard;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.logging.BotLogger;

public class Answer {
    private static final String LOGTAG = "ANSWER";

    private final AbsSender sender;
    private String chat;
    private Stickers sticker;
    private String msg;
    private boolean enableMarkdown;
    private boolean disableWebPagePreview;
    private boolean enableHtml;
    private ReplyKeyboard replyKeyboard;

    public Answer(AbsSender sender) {
        this.sender = sender;
    }

    public Answer to(Chat chat) {
        this.chat = chat.getId().toString();
        return this;
    }

    public Answer to(String chatId) {
        this.chat = chatId;
        return this;
    }

    public Answer sticker(Stickers sticker) {
        this.sticker = sticker;
        return this;
    }

    public Answer message(String msg) {
        this.msg = msg;
        return this;
    }

    public Answer enableMarkdown() {
        enableMarkdown = true;
        return this;
    }

    public Answer disableWebPagePreview() {
        disableWebPagePreview = true;
        return this;
    }

    public Answer enableHtml() {
        enableHtml = true;
        return this;
    }

    public void send() {
        try {
            if (sticker != null) {
                sendSticker();
            }
            if (msg != null) {
                sendMsg();
            }
        } catch (TelegramApiException e) {
            BotLogger.error(LOGTAG, e);
        }
    }

    private void sendMsg() throws TelegramApiException {
        SendMessage m = new SendMessage();
        m.setChatId(chat);
        m.enableMarkdown(enableMarkdown);
        m.enableHtml(enableHtml);
        if (disableWebPagePreview) {
            m.disableWebPagePreview();
        }
        if (replyKeyboard != null) {
            m.setReplyMarkup(replyKeyboard);
        }
        if (replyKeyboard != null) {
            m.setReplyMarkup(replyKeyboard);
        }
        m.setText(msg);
        sender.sendMessage(m);
    }

    private void sendSticker() throws TelegramApiException {
        SendSticker st = new SendSticker();
        st.setChatId(chat);
        st.setSticker(sticker.getId());
        sender.sendSticker(st);
    }

    public Answer replyKeyboard(ReplyKeyboard keyboard) {
        this.replyKeyboard = keyboard;
        return this;
    }
}