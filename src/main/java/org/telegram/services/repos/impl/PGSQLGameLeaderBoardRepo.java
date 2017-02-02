package org.telegram.services.repos.impl;

import org.telegram.services.repos.LeaderBoardRepo;

import java.net.URI;
import java.net.URISyntaxException;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static java.lang.String.format;

public class PGSQLGameLeaderBoardRepo implements LeaderBoardRepo {
    private final String table;

    public PGSQLGameLeaderBoardRepo() {
        this("games_leaderboard");
    }

    public PGSQLGameLeaderBoardRepo(String table) {
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

    public List<Record> selectAll() {
        List<Record> result = new ArrayList<>();
        try (Connection c = connect();
             ResultSet rs = c.createStatement().executeQuery("SELECT id, username, score FROM " + table)) {
            while (rs.next()) {
                result.add(new LeaderBoardRepo.Record(rs.getInt("id"), rs.getString("username"), rs.getLong("score")));
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return result;
    }

    @Override
    public void replace(int id, String username, long score) {
        //TODO TRANSACTION
        insert(username, score);
        delete(id);
    }

    public void insert(String name, long score) {
        try (Connection conn = connect();
             PreparedStatement ps = conn.prepareStatement("INSERT INTO " + table + "(username, score) VALUES(?,?)")) {
            ps.setString(1, name);
            ps.setLong(2, score);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    private void delete(int id) {
        String sql = "DELETE FROM " + table + " WHERE id = ?";
        try (Connection c = this.connect();
             PreparedStatement ps = c.prepareStatement(sql)) {
            ps.setInt(1, id);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
    }

    public void createTable() {
        String sql = "CREATE TABLE IF NOT EXISTS " + table + " (id SERIAL PRIMARY KEY, username TEXT NOT NULL, score INT NOT NULL);";

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
}
