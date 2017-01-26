package org.telegram.services.impl;

import com.google.common.collect.Maps;
import org.telegram.services.GameRepo;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.*;
import java.util.Map;

import static java.lang.String.format;

public class PGSQLGameRepo implements GameRepo {
    private final String table;

    public PGSQLGameRepo() {
        this("games");
    }

    public PGSQLGameRepo(String table) {
        this.table = table;
        createTable();
    }

    private Connection connect() {
        Connection conn = null;
        try {
            URI uri = new URI(System.getenv("DATABASE_URL"));

            String username = uri.getUserInfo().split(":")[0];
            String password = uri.getUserInfo().split(":")[1];
            String url = format("jdbc:postgresql://%s:%d%s?sslmode=require", uri.getHost(), uri.getPort(), uri.getPath());
            conn = DriverManager.getConnection(url, username, password);
        } catch (SQLException | URISyntaxException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    public void insert(String name, String game) {
        String sql = "INSERT INTO " + table + "(username, game) VALUES(?,?)";

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
        String sql = "SELECT username, game FROM " + table;

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
        String sql = "CREATE TABLE IF NOT EXISTS " + table + " (username text PRIMARY KEY, game text NOT NULL);";

        try (Connection c = connect()) {
            c.createStatement().execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void dropTable() {
        String sql = "DROP TABLE " + table + ";";

        try (Connection conn = connect()) {
            conn.createStatement().execute(sql);
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void update(String user, String game) {
        String sql = "UPDATE " + table + " SET game = ? WHERE username = ?";

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
        String sql = "DELETE FROM " + table + " WHERE username = ?";
        try (Connection c = this.connect();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setString(1, user);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }
}
