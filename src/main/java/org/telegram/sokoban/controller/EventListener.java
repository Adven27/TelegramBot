package org.telegram.sokoban.controller;


import org.telegram.sokoban.model.Direction;

public interface EventListener {
    boolean move(Direction direction);

    void restart();

    void startNextLevel();

    void levelCompleted(int level);
}
