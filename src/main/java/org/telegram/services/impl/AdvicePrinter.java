package org.telegram.services.impl;

import org.telegram.services.MessagePrinter;

import java.util.Map;

public class AdvicePrinter implements MessagePrinter {

    public String print(Map<String, String> data) {
        return String.format("%s", data.get("text").replace("&nbsp;"," "));
    }
}