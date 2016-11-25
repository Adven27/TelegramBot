package org.telegram.sokoban.model;

import org.telegram.sokoban.view.Point;

import java.util.Map;

/**
 * Created by k1per on 26.01.2016.
 */
public class Home extends GameObject {
    public Home(int x, int y) {
        super(x, y, 1, 1);
    }

    public void draw(Map<Point, GameObject> gameField) {
        if (gameField.get(new Point(getX(), getY())) == null) {
            gameField.put(new Point(getX(), getY()), this);
        }
    }
}
