package org.telegram.fluent;

import org.telegram.telegrambots.exceptions.TelegramApiException;

public class ApiException extends RuntimeException {
    private TelegramApiException e;

    public ApiException(TelegramApiException e) {
        this.e = e;
    }
}
