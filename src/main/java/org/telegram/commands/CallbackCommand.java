package org.telegram.commands;

import org.telegram.fluent.Answer;
import org.telegram.telegrambots.api.methods.AnswerCallbackQuery;
import org.telegram.telegrambots.api.objects.CallbackQuery;
import org.telegram.telegrambots.bots.commands.BotCommand;
import org.telegram.telegrambots.exceptions.TelegramApiException;
import org.telegram.telegrambots.exceptions.TelegramApiRequestException;
import org.telegram.updateshandlers.CommandsHandler;

import static org.telegram.telegrambots.logging.BotLogger.error;
import static org.telegram.telegrambots.logging.BotLogger.warn;

abstract public class CallbackCommand extends BotCommand {
    private static final String LOGTAG = "CALLBACKCOMMAND";
    private static final int REQ_LIMIT_REACHED = 429;
    private static final int MSG_UNMODIFIED = 400;
    private Integer messageId;

    protected CallbackCommand(String id, String desc) {
        super(id, desc);
    }

    protected void sendAndWaitForCallback(Answer a) {
        messageId = a.send().getMessageId();
    }

    public final boolean handle(CallbackQuery cb, CommandsHandler sender) {
        if (cb.getMessage().getMessageId() == messageId) {
            AnswerCallbackQuery acb = new AnswerCallbackQuery();
            acb.setCallbackQueryId(cb.getId());
            try {
                handleCallback(cb, acb, sender);
                sender.answerCallbackQuery(acb);
            } catch (TelegramApiRequestException e) {
                handleReqLimitOrUnmodifiedMsgException(acb, sender, e);
                error(LOGTAG, e);
            } catch (Exception e) {
                error(LOGTAG, e);
            }
            return true;
        }
        return false;
    }

    private void handleReqLimitOrUnmodifiedMsgException(AnswerCallbackQuery acb, CommandsHandler sender, TelegramApiRequestException e) {
        if (isError(e, REQ_LIMIT_REACHED)) {
            acb.setText("Чот приуныл...\n" + e.getApiResponse());
            try {
                sender.answerCallbackQuery(acb);
            } catch (TelegramApiException e1) {
                warn(LOGTAG, e1);
            }
        } else if(isError(e, MSG_UNMODIFIED)) {
            try {
                sender.answerCallbackQuery(acb);
            } catch (TelegramApiException e1) {
                warn(LOGTAG, e1);
            }
        }
    }

    private boolean isError(TelegramApiRequestException e, int o) {
        return e.getErrorCode().equals(o);
    }

    abstract protected void handleCallback(CallbackQuery cb, AnswerCallbackQuery acb, CommandsHandler sender) throws TelegramApiException;
}