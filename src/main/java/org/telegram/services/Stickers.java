package org.telegram.services;

public enum Stickers {
    HI("BQADAgAD9gIAAi8P8AbW5LZuTmYRNwI"),
    ASS("BQADAgADugIAAi8P8AbTPDXTPJyALwI"),
    DRINK("BQADAgADGwMAAi8P8AaO8R3qFJ6cpgI"),
    RUN("BQADAgADvAIAAi8P8AZyeCjjha4HpwI"),
    DANCE("BQADAgADHwMAAi8P8Aax9-Ibl9ozBwI"),
    BLA("BQADAgADHQMAAi8P8AZUoPRpcJ7uiAI");

    private String id;

    Stickers(String id) {
        this.id = id;
    }

        public String getId() {
                return id;
        }
}
