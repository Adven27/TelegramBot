package org.telegram.sokoban.model;

import java.util.Map;

/**
 * Created by k1per on 26.01.2016.
 */
public class Home extends GameObject {
    public Home(int x, int y) {
        super(x, y, 1, 1);
    }

    public void draw(Map<org.telegram.sokoban.view.Point, GameObject> graphics) {
        graphics.put(new org.telegram.sokoban.view.Point(getX(), getY()), this);
    }
}
