package org.telegram.services;

public class SimpleSkin implements GameSkin {

    private final String border;
    private final String empty;
    private final String player;
    private final String box;
    private final String home;

    public SimpleSkin(String empty, String border, String player, String box, String home) {
        this.border = border;
        this.empty = empty;
        this.player = player;
        this.box = box;
        this.home = home;
    }

    @Override
    public String getHome() {
        return home;
    }

    @Override
    public String getBox() {
        return box;
    }

    @Override
    public String getBorder() {
        return border;
    }

    @Override
    public String getPlayer() {
        return player;
    }

    @Override
    public String getEmptyCell() {
        return empty;
    }
}
