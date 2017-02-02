package org.telegram.services.repos;

import org.telegram.games.game2048.Game2048;

public interface LeaderBoard {
    int BOARD_SIZE = 5;
    void update(String username, Game2048 game);
}
