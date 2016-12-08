package org.telegram.fluent;

import org.telegram.telegrambots.api.methods.updatingmessages.EditMessageText;
import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.api.objects.replykeyboard.InlineKeyboardMarkup;
import org.telegram.telegrambots.bots.AbsSender;
import org.telegram.telegrambots.exceptions.TelegramApiException;

public class EditedMessage {
    private final AbsSender sender;
    private Message original;
    private String msg;
    private boolean enableMarkdown;
    private boolean disableWebPagePreview;
    private boolean enableHtml;
    private InlineKeyboardMarkup replyKeyboard;

    public EditedMessage(AbsSender sender, Message original) {
        this.sender = sender;
        this.original = original;
    }

    public EditedMessage newText(String msg) {
        this.msg = msg;
        return this;
    }

    public EditedMessage enableMarkdown() {
        enableMarkdown = true;
        return this;
    }

    public EditedMessage disableWebPagePreview() {
        disableWebPagePreview = true;
        return this;
    }

    public EditedMessage enableHtml() {
        enableHtml = true;
        return this;
    }

    public EditedMessage keyboard(InlineKeyboardMarkup keyboard) {
        this.replyKeyboard = keyboard;
        return this;
    }

    public Message send() {
        EditMessageText m = new EditMessageText();
        m.setMessageId(original.getMessageId());
        m.setChatId(original.getChatId().toString());
        m.setText(msg);
        m.enableMarkdown(enableMarkdown);
        m.enableHtml(enableHtml);
        if (disableWebPagePreview) {
            m.disableWebPagePreview();
        }
        if (replyKeyboard != null) {
            m.setReplyMarkup(replyKeyboard);
        }
        try {
            return sender.editMessageText(m);
        } catch (TelegramApiException e) {
            throw new ApiException(e);
        }
    }

}