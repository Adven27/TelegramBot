package org.telegram.sokoban.model;


import org.telegram.services.DAO;
import org.telegram.sokoban.controller.EventListener;

import java.util.Set;


public class Model {
    EventListener eventListener;
    public static final int FIELD_CELL_SIZE = 1;
    GameObjects gameObjects;
    public int currentLevel = new DAO().getLevel();
    LevelLoader levelLoader = new LevelLoader("/levels.txt");

    public void setEventListener(EventListener eventListener) {
        this.eventListener = eventListener;
    }

    public GameObjects getGameObjects() {
        return gameObjects;
    }

    public void restartLevel(int level) {
        currentLevel = level;
        this.gameObjects = levelLoader.getLevel(level);
    }

    public void restart() {
        restartLevel(currentLevel);
    }

    public void startNextLevel() {
        currentLevel++;
        restartLevel(currentLevel);
    }

    public boolean move(Direction direction) {
        if (checkWallCollision(gameObjects.getPlayer(), direction)) return false;
        if (checkBoxCollision(direction)) return false;
        Player player = gameObjects.getPlayer();

        switch (direction) {
            case UP:
                player.move(0, -FIELD_CELL_SIZE);
                break;
            case DOWN:
                player.move(0, FIELD_CELL_SIZE);
                break;
            case LEFT:
                player.move(-FIELD_CELL_SIZE, 0);
                break;
            case RIGHT:
                player.move(FIELD_CELL_SIZE, 0);
                break;
        }
        checkCompletion();
        return true;
    }

    public boolean checkWallCollision(CollisionObject gameObject, Direction direction) {
        Set<Wall> walls = gameObjects.getWalls();

        for (Wall wall : walls) {
            if (gameObject.isCollision(wall, direction)) {
                return true;
            }
        }
        return false;
    }

    public boolean checkBoxCollision(Direction direction) {
        Player player = gameObjects.getPlayer();
        Set<Box> boxes = gameObjects.getBoxes();

        for (Box box : boxes) {
            if (player.isCollision(box, direction)) {
                for (Box box1 : boxes) {
                    if (box.isCollision(box1, direction)) return true;
                }
                if (checkWallCollision(box, direction)) return true;
                switch (direction) {
                    case UP:
                        box.move(0, -Model.FIELD_CELL_SIZE);
                        break;
                    case DOWN:
                        box.move(0, Model.FIELD_CELL_SIZE);
                        break;
                    case LEFT:
                        box.move(-Model.FIELD_CELL_SIZE, 0);
                        break;
                    case RIGHT:
                        box.move(Model.FIELD_CELL_SIZE, 0);
                }
                return false;
            }
        }

        return false;
    }

    public void checkCompletion() {

        int counter = 0;
        for (Home home : gameObjects.getHomes()) {
            for (Box box : gameObjects.getBoxes()) {
                if (home.getX() == box.getX() && home.getY() == box.getY()) counter++;
            }
        }
        if (counter == gameObjects.getHomes().size()) {
            eventListener.levelCompleted(currentLevel);
        }
    }

}
