package org.telegram.sokoban.model;

import org.telegram.sokoban.view.Point;

import java.util.Map;

public class Home extends GameObject {
    public Home(int x, int y) {
        super(x, y);
    }

    public void draw(Map<Point, GameObject> gameField) {
        gameField.putIfAbsent(new Point(getX(), getY()), this);
    }
}
