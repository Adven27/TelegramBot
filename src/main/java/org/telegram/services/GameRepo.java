package org.telegram.services;

import java.util.Map;

public interface GameRepo {
    void insert(String name, String game);
    Map<String, String> selectAll();
    void update(String user, String game);
    void delete(String user);
}
