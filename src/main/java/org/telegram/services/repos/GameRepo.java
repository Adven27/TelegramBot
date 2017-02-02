package org.telegram.services.repos;

import java.util.Map;

public interface GameRepo {
    void insert(String name, String game);
    Map<String, String> selectAll();
    void update(String user, String game);
    void delete(String user);
}
