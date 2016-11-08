package org.telegram.services.impl;

import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.telegram.telegrambots.logging.BotLogger;

import java.io.IOException;

public class JsonResource {
    protected JSONObject getObjectFrom(String url) {
        return new JSONObject(from(url));
    }

    protected JSONArray getArrayFrom(String url) {
        return new JSONArray(from(url));
    }

    private String from(String url) {
        try {
            BotLogger.info("JsonResource", "fetching from " + url);
            CloseableHttpClient client = HttpClientBuilder.create().setSSLHostnameVerifier(new NoopHostnameVerifier()).build();
            BufferedHttpEntity buf = new BufferedHttpEntity(client.execute(new HttpGet(url)).getEntity());
            return EntityUtils.toString(buf, "UTF-8");
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
