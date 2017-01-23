package org.telegram.commands;

import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Test;
import org.telegram.games.game2048.Game2048;

public class Game2048CommandTest {
    @Test
    public <K, V> void execute() throws Exception {
        JSONObject jsonObject = new JSONObject();
        Game2048.Tile[] myTiles = new Game2048.Tile[4 * 4];;
        for (int i = 0; i < 16; i++) {
            myTiles[i] = new Game2048.Tile();
        }

        jsonObject.put("tiles", new JSONArray(myTiles));

        jsonObject.toString();

    }


}