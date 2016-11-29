package org.telegram.sokoban.controller;


import org.telegram.sokoban.model.Direction;
import org.telegram.sokoban.model.GameObject;
import org.telegram.sokoban.model.GameObjects;
import org.telegram.sokoban.model.Model;
import org.telegram.sokoban.view.Point;
import org.telegram.sokoban.view.View;

import java.util.LinkedList;
import java.util.Map;

public class Controller implements EventListener {
    View view;
    Model model;

    public LinkedList<Map<Point, GameObject>> getHistory() {
        return history;
    }

    LinkedList<Map<Point, GameObject>> history = new LinkedList<>();

    public Controller() {
        this(0);
    }

    public Controller(int level) {
        view = new View(this);
        model = new Model();
        view.init();
        if (level == 0)  {
            model.restart();
        } else {
            model.restartLevel(level);
        }
        model.setEventListener(this);
        view.setEventListener(this);
        view.update();
    }

    public Map<Point, GameObject> getGameField() {
        Map<Point, GameObject> gameField = view.getGameField();
        history.add(gameField);
        return gameField;
    }

    public GameObjects getGameObjects() {
        return model.getGameObjects();
    }


    public boolean move(Direction direction) {
        if (model.move(direction)) {
            view.update();
            return true;
        }
        return false;
    }

    public void restart() {
        model.restart();
        view.update();
    }


    public void startNextLevel() {
        model.startNextLevel();
        view.update();
    }


    public void levelCompleted(int level) {
        view.completed(level);
    }


    public int getCurLevel() {
        return model.currentLevel;
    }
}
