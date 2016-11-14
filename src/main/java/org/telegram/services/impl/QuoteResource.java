package org.telegram.services.impl;

import org.json.JSONObject;
import org.telegram.services.URLResource;
import org.telegram.telegrambots.logging.BotLogger;

import java.util.HashMap;
import java.util.Map;

public class QuoteResource extends JsonResource implements URLResource {
    private static final String LOGTAG = "FORISMATICSERVICE";
    private static final String BASEURL = "http://api.forismatic.com/api/1.0/?method=getQuote&format=json";

    public QuoteResource() {}
    
    public Map<String, String> fetch() {
        JSONObject js = getObjectFrom(BASEURL);
        BotLogger.info(LOGTAG, js.toString());
        Map<String, String> res = new HashMap();
        res.put("text", js.getString("quoteText"));
        res.put("author", js.getString("quoteAuthor"));
        res.put("link", js.getString("quoteLink"));
        return res;
    }
}