package org.telegram.games.sokoban.view;


import org.telegram.games.sokoban.controller.EventListener;
import org.telegram.games.sokoban.model.GameObject;
import org.telegram.games.sokoban.model.GameObjects;

import java.util.Map;

public class Field {
    View view;
    EventListener eventListener;

    public Field(View view) {
        this.view = view;
    }

    public void setEventListener(EventListener eventListener) {
        this.eventListener = eventListener;
    }

    public void paint(Map<Point, GameObject> gameField) {
        GameObjects gameObjects = view.getGameObjects();
        for (GameObject gameObject : gameObjects.getAll()) {
            gameObject.draw(gameField);
        }

    }

}
