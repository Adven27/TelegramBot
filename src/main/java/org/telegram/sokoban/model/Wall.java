package org.telegram.sokoban.model;

import org.telegram.sokoban.view.Point;

import java.util.Map;

/**
 * Created by k1per on 26.01.2016.
 */
public class Wall extends CollisionObject
{
    public Wall(int x, int y){
        super(x,y);
    }

    public void draw(Map<Point, GameObject> graphics) {
        graphics.put(new Point(getX(), getY()), this);
    }
}
