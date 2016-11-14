package org.telegram.services;

import org.telegram.telegrambots.api.objects.Message;
import org.telegram.telegrambots.bots.AbsSender;

public interface AnswerMessage {
    void answer(Message msg, AbsSender sender);
    boolean is(String cmd);
}
