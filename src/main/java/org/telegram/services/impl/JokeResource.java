package org.telegram.services.impl;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class JokeResource extends JsonResource {

    private List<JSONObject> results = new ArrayList<>();

    public Map<String, String> fetch() {
        Map<String, String> res = new HashMap();
        if (results.isEmpty()) {
            JSONArray array = getArrayFrom("http://www.umori.li/api/get?site=bash.im&name=bash&num=100");
            for (Object o : array) {
                results.add((JSONObject) o);
            }
        }
        JSONObject o = results.remove(0);
        res.put("site", o.getString("site"));
        res.put("text", o.getString("elementPureHtml"));
        return res;
    }
}
