package org.telegram.services.impl;

import org.telegram.services.MessagePrinter;

import java.util.Map;

public class QuotePrinter implements MessagePrinter {

    public String print(Map<String, String> data) {
        return String.format("%s \n\n %s (%s)", data.get("text"), data.get("author"), data.get("link"));
    }
}