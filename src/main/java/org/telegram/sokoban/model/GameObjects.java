package org.telegram.sokoban.model;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by k1per on 26.01.2016.
 */
public class GameObjects
{
    private Set<Wall> walls;
    private Set<Box> boxes;
    private Set<Home> homes;
    private Player player;

    public GameObjects(Set<Wall> walls, Set<Box> boxes, Set<Home> homes, Player player)
    {
        this.walls = walls;
        this.boxes = boxes;
        this.homes = homes;
        this.player = player;
    }

    public Set<Wall> getWalls()
    {
        return walls;
    }

    public void setWalls(Set<Wall> walls)
    {
        this.walls = walls;
    }

    public Set<Box> getBoxes()
    {
        return boxes;
    }

    public void setBoxes(Set<Box> boxes)
    {
        this.boxes = boxes;
    }

    public Set<Home> getHomes()
    {
        return homes;
    }

    public void setHomes(Set<Home> homes)
    {
        this.homes = homes;
    }

    public Player getPlayer()
    {
        return player;
    }

    public void setPlayer(Player player)
    {
        this.player = player;
    }

    public Set<GameObject> getAll(){
        Set<GameObject> set = new HashSet<>();
        set.addAll(walls);
        set.addAll(homes);
        set.addAll(boxes);
        set.add(player);
        return set;
    }
}
