package org.telegram.services.impl;

import org.telegram.services.DAO;
import org.telegram.services.MessageTransformer;

import java.util.Random;

public class Mamorator implements MessageTransformer {

    private final DAO dao;

    public Mamorator(DAO dao) {
        this.dao = dao;
    }

    public String transform(String m) {
        return "мам" + (m.length() < 4 ? m.substring(1) : m.substring(2)) + "... " + dao.getEndWord(new Random().nextInt(4));
    }

}