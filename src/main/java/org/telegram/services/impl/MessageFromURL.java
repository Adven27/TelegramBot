package org.telegram.services.impl;

import org.telegram.services.MessagePrinter;
import org.telegram.services.URLResource;
import org.telegram.telegrambots.logging.BotLogger;

public class MessageFromURL {

    private static final String LOGTAG = "MESSAGEFROMURL";
    private final URLResource resource;
    private final MessagePrinter printer;

    public MessageFromURL(URLResource resource, MessagePrinter printer) {
        this.resource = resource;
        this.printer = printer;
    }

    public String print() {
        try {
            return printer.print(resource.fetch());
        } catch (Exception e) {
            BotLogger.error(LOGTAG, e);
            return "Связь с ноосферой потеряна...";
        }
    }
}