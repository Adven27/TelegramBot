package org.telegram.games.sokoban.controller;


import org.telegram.games.sokoban.model.Direction;

public interface EventListener {
    boolean move(Direction direction);

    void restart();

    void startNextLevel();

    void levelCompleted(int level);
}
