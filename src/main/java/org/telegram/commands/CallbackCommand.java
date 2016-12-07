package org.telegram.commands;

import org.telegram.fluent.Answer;
import org.telegram.telegrambots.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.api.objects.CallbackQuery;
import org.telegram.telegrambots.bots.commands.BotCommand;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;
import org.telegram.telegrambots.logging.BotLogger;
import org.telegram.updateshandlers.CommandsHandler;

import static org.telegram.telegrambots.logging.BotLogger.error;

abstract public class CallbackCommand extends BotCommand {
    private static final String LOGTAG = "CALLBACKCOMMAND";
    private int messageId;

    protected CallbackCommand(String id, String desc) {
        super(id, desc);
    }

    protected void sendAndWaitForCallback(Answer a) {
        messageId = a.send().getMessageId();
    }

    public final boolean handle(CallbackQuery cb, CommandsHandler sender) {
        if (cb.getMessage().getMessageId() == messageId) {
            try {
                handleCallback(cb, sender);
            } catch (TelegramApiRequestException e) {
                handleReqLimitOrUnmodifiedMsgException(cb, sender, e);
                BotLogger.error(LOGTAG, e);
            } catch (Exception e) {
                BotLogger.error(LOGTAG, e);
            }
            return true;
        }
        return false;
    }


    private void handleReqLimitOrUnmodifiedMsgException(CallbackQuery cb, CommandsHandler sender, TelegramApiRequestException e) {
        if (e.getErrorCode().equals(429)) {
            AnswerCallbackQuery acb = new AnswerCallbackQuery();
            acb.setText("Чот приуныл...\n" + e.getApiResponse());
            acb.setCallbackQueryId(cb.getId());
            try {
                sender.answerCallbackQuery(acb);
            } catch (TelegramApiException e1) {
                error(LOGTAG, e1);
            }
        } else if(e.getErrorCode().equals(400)) {
            AnswerCallbackQuery acb = new AnswerCallbackQuery();
            acb.setCallbackQueryId(cb.getId());
            try {
                sender.answerCallbackQuery(acb);
            } catch (TelegramApiException e1) {
                BotLogger.error(LOGTAG, e1);
            }
        }
    }

    abstract protected void handleCallback(CallbackQuery cb, CommandsHandler sender) throws TelegramApiException;
}