package org.telegram.services;

import java.util.Random;

public enum Stickers {
    HI("BQADAgAD9gIAAi8P8AbW5LZuTmYRNwI"),
    ASS("BQADAgADugIAAi8P8AbTPDXTPJyALwI"),
    DRINK("BQADAgADGwMAAi8P8AaO8R3qFJ6cpgI"),
    EAT("BQADAgAD0gIAAi8P8AbA6c5fV6KeAAEC"),
    RUN("BQADAgADvAIAAi8P8AZyeCjjha4HpwI"),
    DANCE("BQADAgADHwMAAi8P8Aax9-Ibl9ozBwI"),
    THINK("BQADAgAD4wIAAi8P8AZPctLFKjIGjwI"),
    ASK("BQADAgAD4QIAAi8P8AbPKiGkzj2RIQI"),
    LOL("BQADAgADJAUAAi8P8AbOf9c5yMdOIQI"),
    HELP("BQADAgAD9AIAAi8P8AaNtW5R8yIbWAI"),
    BLA("BQADAgADHQMAAi8P8AZUoPRpcJ7uiAI");

    private String id;

    Stickers(String id) {
        this.id = id;
    }

    public String getId() {
                return id;
        }

    public static Stickers random() {
        return values()[new Random().nextInt(values().length)];
    }
}
