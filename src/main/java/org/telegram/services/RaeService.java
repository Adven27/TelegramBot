package org.telegram.services;

import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.conn.ssl.NoopHostnameVerifier;
import org.apache.http.entity.BufferedHttpEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.telegram.telegrambots.logging.BotLogger;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RaeService {
    private static final String LOGTAG = "RAESERVICE";

    private static final String BASEURL = "https://music.yandex.ru/search?type=tracks&text="; ///< Base url for REST


    public List<RaeResult> getResults(String query) {
        List<RaeResult> results = new ArrayList<>();

        String completeURL;
        try {
            completeURL = BASEURL + URLEncoder.encode(query, "UTF-8");

            CloseableHttpClient client = HttpClientBuilder.create().setSSLHostnameVerifier(new NoopHostnameVerifier()).build();
            HttpGet request = new HttpGet(completeURL);

            CloseableHttpResponse response = client.execute(request);
            HttpEntity ht = response.getEntity();

            BufferedHttpEntity buf = new BufferedHttpEntity(ht);
            String responseString = EntityUtils.toString(buf, "UTF-8");

            Document document = Jsoup.parse(responseString);
            Elements elements = document.getElementsByClass("track_selectable");

            if (elements.isEmpty()) {
                results = getResultsWordSearch(query);
            } else {
                //results = getResultsFromExactMatch(elements);
            }
        } catch (IOException e) {
            BotLogger.error(LOGTAG, e);
        }

        return results;
    }

    private List<RaeResult> getResultsWordSearch(String query) {
        List<RaeResult> results = new ArrayList<>();

        String completeURL;
        try {
            completeURL = BASEURL + URLEncoder.encode(query, "UTF-8");

            CloseableHttpClient client = HttpClientBuilder.create().setSSLHostnameVerifier(new NoopHostnameVerifier()).build();
            HttpGet request = new HttpGet(completeURL);

            CloseableHttpResponse response = client.execute(request);
            HttpEntity ht = response.getEntity();

            BufferedHttpEntity buf = new BufferedHttpEntity(ht);
            String responseString = EntityUtils.toString(buf, "UTF-8");

            Document document = Jsoup.parse(responseString);
            Element list = document.select("body div ul").first();

            if (list != null) {
                Elements links = list.getElementsByTag("a");
                if (!links.isEmpty()) {
                    for (Element link : links) {
                        List<RaeResult> partialResults = fetchWord(URLEncoder.encode(link.attributes().get("href"), "UTF-8"), link.text());
                        if (!partialResults.isEmpty()) {
                            results.addAll(partialResults);
                        }
                    }
                }
            }
        } catch (IOException e) {
            BotLogger.error(LOGTAG, e);
        }

        return results;
    }

    private List<RaeResult> fetchWord(String link, String word) {
        List<RaeResult> results = new ArrayList<>();

        String completeURL;
        try {
            completeURL = BASEURL + link;

            CloseableHttpClient client = HttpClientBuilder.create().setSSLHostnameVerifier(new NoopHostnameVerifier()).build();
            HttpGet request = new HttpGet(completeURL);

            CloseableHttpResponse response = client.execute(request);
            HttpEntity ht = response.getEntity();

            BufferedHttpEntity buf = new BufferedHttpEntity(ht);
            String responseString = EntityUtils.toString(buf, "UTF-8");

            Document document = Jsoup.parse(responseString);
            Element article = document.getElementsByTag("article").first();
            String articleId = null;
            if (article != null) {
                articleId = article.attributes().get("id");
            }
            Elements elements = document.select(".j");

            if (!elements.isEmpty()) {
                //results = getResultsFromExactMatch(elements, word, articleId);
            }
        } catch (IOException e) {
            BotLogger.error(LOGTAG, e);
        }

        return results;
    }



    public static class RaeResult {
        public int index;
        public String word;
        public Map<String, String> tags = new HashMap<>();
        public String definition;
        public String link;

        public String getDefinition() {
            final StringBuilder builder = new StringBuilder();
            if (link != null && !link.isEmpty()) {
                builder.append("[").append(word).append("](");
                builder.append(link).append(")\n");
            }
            for (Map.Entry<String, String> tag : tags.entrySet()) {
                builder.append("*").append(tag.getKey()).append("*");
                builder.append(" (_").append(tag.getValue()).append("_)\n");
            }
            builder.append(definition);
            return builder.toString();
        }

        public String getDescription() {
            return definition;
        }

        public String getTitle() {
            final StringBuilder builder = new StringBuilder();
            builder.append(index).append(". ").append(word);
            return builder.toString();
        }
    }
}
