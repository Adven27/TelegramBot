package org.telegram.commands;

import org.telegram.fluent.Answer;
import org.telegram.telegrambots.api.objects.CallbackQuery;
import org.telegram.telegrambots.bots.commands.BotCommand;
import org.telegram.updateshandlers.CommandsHandler;

abstract public class CallbackCommand extends BotCommand {
    private int messageId;

    protected CallbackCommand(String id, String desc) {
        super(id, desc);
    }

    protected void say(Answer a) {
        messageId = a.send().getMessageId();
    }

    public final boolean handle(CallbackQuery cb, CommandsHandler sender) {
        if (cb.getMessage().getMessageId() == messageId) {
            handleCallback(cb, sender);
            return true;
        }
        return false;
    }

    abstract protected void handleCallback(CallbackQuery cb, CommandsHandler sender);
}