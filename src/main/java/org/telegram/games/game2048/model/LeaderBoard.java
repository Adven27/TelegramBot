package org.telegram.games.game2048.model;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable(tableName = "accounts")
public class LeaderBoard {

    @DatabaseField(id = true)
    private String name;
    @DatabaseField
    private String score;

    public LeaderBoard() {
        // ORMLite needs a no-arg constructor
    }
    public LeaderBoard(String name, String score) {
        this.name = name;
        this.score = score;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getScore() {
        return score;
    }
    public void setScore(String score) {
        this.score = score;
    }
}