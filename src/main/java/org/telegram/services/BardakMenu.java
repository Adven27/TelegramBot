package org.telegram.services;

import java.time.LocalDateTime;

public class BardakMenu {
    private final DAO dao;

    public BardakMenu(DAO dao) {
        this.dao = dao;
    }

    public String menu(LocalDateTime dateTime) {
        return dao.getWeekMenu().get(dateTime.getDayOfWeek().getValue());
    }
}