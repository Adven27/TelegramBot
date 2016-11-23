package org.telegram.sokoban.model;

import org.telegram.sokoban.view.Point;

import java.util.Map;

/**
 * Created by k1per on 26.01.2016.
 */
public abstract class GameObject
{
    private int x;
    private int y;
    private int width;
    private int height;

    public abstract void draw(Map<Point, GameObject> graphics);

    public GameObject(int x, int y){
        this.x = x;
        this.y = y;
        this.width = Model.FIELD_CELL_SIZE;
        this.height = Model.FIELD_CELL_SIZE;
    }
    public GameObject(int x, int y, int width, int height){
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public int getX()
    {
        return x;
    }

    public void setX(int x)
    {
        this.x = x;
    }

    public int getY()
    {
        return y;
    }

    public void setY(int y)
    {
        this.y = y;
    }

    public int getWidth()
    {
        return width;
    }

    public int getHeight()
    {
        return height;
    }
}
