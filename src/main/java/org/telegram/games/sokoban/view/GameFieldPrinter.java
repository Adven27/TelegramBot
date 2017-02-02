package org.telegram.games.sokoban.view;

import org.telegram.games.sokoban.model.*;
import org.telegram.services.GameSkin;

import java.util.Map;


public class GameFieldPrinter {

    private GameSkin skin;

    public GameFieldPrinter(GameSkin skin) {
        this.skin = skin;
    }

    public String print(Map<Point, GameObject> gameField, GameObjects gameObjects, int curLevel) {
        String msg = "Dark Bloodsokoborn III \nLevel: " + curLevel + "\n";

        for (int y = 0; y < gameObjects.getH(); y++) {
            for (int x = 0; x < gameObjects.getW(); x++) {
                GameObject gameObject = gameField.get(new Point(x, y));
                if (gameObject == null) {
                    msg += skin.getEmptyCell();
                } else if (gameObject instanceof Player) {
                    msg += skin.getPlayer();
                } else if (gameObject instanceof Wall) {
                    msg += skin.getBorder();
                } else if (gameObject instanceof Box) {
                    msg += skin.getBox();
                } else if (gameObject instanceof Home) {
                    msg += skin.getHome();
                }
            }
            msg += "\n";
        }
        return msg;
    }
}
