package org.telegram.sokoban.model;

import org.telegram.sokoban.view.Point;

import java.util.Map;

/**
 * Created by k1per on 26.01.2016.
 */
public class Box extends CollisionObject implements Movable {


    public Box(int x, int y) {
        super(x, y);
    }

    public void move(int x, int y) {
        setX(getX() + x);
        setY(getY() + y);
    }

    @Override
    public void draw(Map<Point, GameObject> graphics) {
        graphics.put(new Point(getX(), getY()), this);
    }
}
