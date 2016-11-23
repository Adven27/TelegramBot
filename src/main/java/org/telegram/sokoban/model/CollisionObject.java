package org.telegram.sokoban.model;

/**
 * Created by k1per on 26.01.2016.
 */
public abstract class CollisionObject extends GameObject
{
    public CollisionObject(int x,int y){
        super(x,y);
    }

    public boolean isCollision(GameObject gameObject, Direction direction){
        int x = this.getX();
        int y = this.getY();

        switch (direction){
            case UP:
                y -= Model.FIELD_CELL_SIZE;
                break;
            case DOWN:
                y += Model.FIELD_CELL_SIZE;
                break;
            case LEFT:
                x -= Model.FIELD_CELL_SIZE;
                break;
            case RIGHT:
                x += Model.FIELD_CELL_SIZE;
        }

        if(gameObject.getX() == x && gameObject.getY() == y) return true;
        else return false;

    }
}
