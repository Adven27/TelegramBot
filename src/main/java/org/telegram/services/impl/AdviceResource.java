package org.telegram.services.impl;

import org.json.JSONObject;
import org.telegram.services.URLResource;
import org.telegram.telegrambots.logging.BotLogger;

import java.util.HashMap;
import java.util.Map;

public class AdviceResource extends JsonResource implements URLResource {
    private static final String LOGTAG = "ADVICERESOURCE";
    private static final String BASEURL = "http://fucking-great-advice.ru/api/random";

    public AdviceResource() {}
    
    public Map<String, String> fetch() {
        JSONObject js = getObjectFrom(BASEURL);
        BotLogger.info(LOGTAG, js.toString());
        Map<String, String> res = new HashMap();
        res.put("text", js.getString("text"));
        return res;
    }
}