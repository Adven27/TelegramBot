package org.telegram.services.impl;

import org.jsoup.safety.Whitelist;
import org.telegram.services.MessagePrinter;

import java.util.Map;

import static java.lang.String.format;
import static org.jsoup.Jsoup.clean;

public class JokePrinter implements MessagePrinter {

    public String print(Map<String, String> data) {
        return format("%s \n %s", clean(data.get("text"), Whitelist.basic()).replace("<br>", ""), data.get("site"));
    }
}