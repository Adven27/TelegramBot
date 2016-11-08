package org.telegram.services.impl;

import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;

import java.util.Map;

public class JokePrinter {

    private JokeResource jokeResource;

    public JokePrinter(JokeResource jokeResource) {
        this.jokeResource = jokeResource;
    }

    public String print() {
        Map<String, String> data = jokeResource.fetch();
        String text = data.get("text");
        return String.format("%s \n %s", Jsoup.clean(text, Whitelist.basic()).replace("<br>",""), data.get("site"));
    }
}
