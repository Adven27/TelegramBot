package org.telegram.services.impl;

import com.google.common.collect.Maps;
import org.telegram.services.GameRepo;

import java.sql.*;
import java.util.Map;

public class SQLiteGameRepo implements GameRepo {

    public static final String SQLITE_BOT_DB = "jdbc:sqlite:bot.db";

    public SQLiteGameRepo() {
        createTable();
    }

    private Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(SQLITE_BOT_DB);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public void insert(String name, String game) {
        String sql = "INSERT INTO games(username, game) VALUES(?,?)";

        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, name);
            ps.setString(2, game);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public Map<String, String> selectAll() {
        Map result = Maps.newHashMap();
        String sql = "SELECT username, game FROM games";

        try (Connection c = connect();
             ResultSet rs = c.createStatement().executeQuery(sql)) {

            while (rs.next()) {
                result.put(rs.getString("username"), rs.getString("game"));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return result;
    }

    public void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS games (username text PRIMARY KEY, game text NOT NULL);";

        try (Connection c = connect()) {
            c.createStatement().execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void dropTable() {
        String sql = "DROP TABLE games;";

        try (Connection conn = connect()) {
            conn.createStatement().execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void update(String user, String game) {
        String sql = "UPDATE games SET game = ? WHERE username = ?";

        try (Connection c = connect();
            PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, game);
            ps.setString(2, user);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void delete(String user) {
        String sql = "DELETE FROM games WHERE username = ?";
        try (Connection c = this.connect();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, user);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
