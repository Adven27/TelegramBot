package org.telegram.sokoban.view;


import org.telegram.sokoban.controller.EventListener;
import org.telegram.sokoban.model.GameObject;
import org.telegram.sokoban.model.GameObjects;

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
