package org.telegram.services.impl;

import org.json.JSONObject;
import org.telegram.telegrambots.logging.BotLogger;

import static java.lang.String.format;

public class QuoteService extends JsonResource {
    private static final String LOGTAG = "FORISMATICSERVICE";
    private static final String BASEURL = "http://api.forismatic.com/api/1.0/?method=getQuote&format=json";

    private static volatile QuoteService instance;

    private QuoteService() {}

    public static QuoteService getInstance() {
        QuoteService currentInstance;
        if (instance == null) {
            synchronized (QuoteService.class) {
                if (instance == null) {
                    instance = new QuoteService();
                }
                currentInstance = instance;
            }
        } else {
            currentInstance = instance;
        }
        return currentInstance;
    }

    public String fetchQuote() {
        try {
            JSONObject js = getObjectFrom(BASEURL);
            BotLogger.info(LOGTAG, js.toString());
            return format("%s \n\n %s (%s)", js.get("quoteText"), js.get("quoteAuthor"), js.get("quoteLink"));
        } catch (Exception e) {
            BotLogger.error(LOGTAG, e);
            return "Ñגÿחü ס םממספונמי ןמעונÿםא...";
        }
    }
}