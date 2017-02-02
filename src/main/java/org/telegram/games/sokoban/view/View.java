package org.telegram.games.sokoban.view;


import org.telegram.games.sokoban.controller.EventListener;
import org.telegram.services.DAO;
import org.telegram.games.sokoban.controller.Controller;
import org.telegram.games.sokoban.model.GameObject;
import org.telegram.games.sokoban.model.GameObjects;

import java.util.HashMap;
import java.util.Map;

public class View {
    private Controller controller;
    private Field field;

    public Map<Point, GameObject> getGameField() {
        return gameField;
    }

    private Map<Point, GameObject> gameField = new HashMap();

    public View(Controller controller) {
        this.controller = controller;
    }

    public void init() {
        field = new Field(this);
    }

    public void setEventListener(EventListener eventListener){
        field.setEventListener(eventListener);
    }

    public void update(){
        gameField.clear();
        field.paint(gameField);
    }


    public void completed(int level){
        update();
        new DAO().saveLevel(level + 1);
        controller.startNextLevel();

    }

    public GameObjects getGameObjects(){
        return controller.getGameObjects();
    }
}
