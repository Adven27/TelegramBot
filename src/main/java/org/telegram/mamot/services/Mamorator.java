package org.telegram.mamot.services;

import java.util.Random;

public class Mamorator {

    private final DAO dao;

    public Mamorator(DAO dao) {
        this.dao = dao;
    }

    public String mamate(String m) {
        return "мам" + (m.length() < 4 ? m.substring(1) : m.substring(2)) + "... " + dao.getEndWord(new Random().nextInt(4));
    }

}