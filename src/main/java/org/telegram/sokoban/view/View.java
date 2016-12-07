package org.telegram.sokoban.view;


import org.telegram.mamot.services.DAO;
import org.telegram.sokoban.controller.Controller;
import org.telegram.sokoban.controller.EventListener;
import org.telegram.sokoban.model.GameObject;
import org.telegram.sokoban.model.GameObjects;

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
        //JOptionPane.showMessageDialog(null, "You completed " + level + " level");
        controller.startNextLevel();

    }

    public GameObjects getGameObjects(){
        return controller.getGameObjects();
    }
}
