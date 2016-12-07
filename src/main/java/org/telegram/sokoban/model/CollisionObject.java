package org.telegram.sokoban.model;

public abstract class CollisionObject extends GameObject
{
    public CollisionObject(int x,int y){
        super(x,y);
    }

    public boolean isCollision(GameObject go, Direction d){
        int x = this.getX();
        int y = this.getY();

        switch (d){
            case UP: y -= Model.FIELD_CELL_SIZE; break;
            case DOWN: y += Model.FIELD_CELL_SIZE; break;
            case LEFT: x -= Model.FIELD_CELL_SIZE; break;
            case RIGHT: x += Model.FIELD_CELL_SIZE; }
        return go.getX() == x && go.getY() == y;
    }
}
