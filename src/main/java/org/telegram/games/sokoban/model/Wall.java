package org.telegram.games.sokoban.model;

import org.telegram.games.sokoban.view.Point;

import java.util.Map;

public class Wall extends CollisionObject
{
    public Wall(int x, int y){
        super(x,y);
    }

    public void draw(Map<Point, GameObject> graphics) {
        graphics.put(new Point(getX(), getY()), this);
    }
}
