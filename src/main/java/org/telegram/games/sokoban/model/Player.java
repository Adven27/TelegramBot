package org.telegram.games.sokoban.model;

import org.telegram.games.sokoban.view.Point;

import java.util.Map;

public class Player extends CollisionObject implements Movable {
    public Player(int x, int y) {
        super(x, y);
    }

    public void draw(Map<Point, GameObject> graphics) {
        graphics.put(new Point(getX(), getY()), this);
    }

    @Override
    public void move(int x, int y) {
        setX(getX() + x);
        setY(getY() + y);
    }
}
