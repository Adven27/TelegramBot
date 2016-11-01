package org.telegram.services;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.json.JSONObject;
import org.telegram.telegrambots.logging.BotLogger;

/**
 * @author Ruben Bermudez
 * @version 1.0
 * @brief Weather service
 * @date 20 of June of 2015
 */
public class QuoteService {
    private static final String LOGTAG = "FORISMATICSERVICE";

    private static final String BASEURL = "http://api.forismatic.com/api/1.0/?method=getQuote&format=json"; ///< Base url for REST
    private static volatile QuoteService instance; ///< Instance of this class

    /**
     * Constructor (private due to singleton pattern)
     */
    private QuoteService() {
    }

    /**
     * Singleton
     *
     * @return Return the instance of this class
     */
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
        String responseToUser;
        try {
            String completURL = BASEURL;
            CloseableHttpClient client = HttpClientBuilder.create().setSSLHostnameVerifier(new NoopHostnameVerifier()).build();
            HttpGet request = new HttpGet(completURL);

            CloseableHttpResponse response = client.execute(request);
            HttpEntity ht = response.getEntity();

            BufferedHttpEntity buf = new BufferedHttpEntity(ht);
            String responseString = EntityUtils.toString(buf, "UTF-8");

            JSONObject jsonObject = new JSONObject(responseString);
            BotLogger.info(LOGTAG, jsonObject.toString());
            responseToUser = String.format("%s \n\n %s (%s)",
                    jsonObject.get("quoteText"), jsonObject.get("quoteAuthor"), jsonObject.get("quoteLink"));
        } catch (Exception e) {
            BotLogger.error(LOGTAG, e);
            responseToUser = "����� � ��������� ��������...";
        }
        return responseToUser;
    }
}
