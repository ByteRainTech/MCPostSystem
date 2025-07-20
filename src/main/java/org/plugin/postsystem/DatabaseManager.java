package org.plugin.postsystem;

import java.sql.*;
import java.util.*;

public class DatabaseManager {
    private Connection connection;
    private final String dbType;

    public DatabaseManager(String dbType) {
        this.dbType = dbType.toLowerCase();
    }

    private Connection getConnection() throws SQLException {
        if (connection == null || connection.isClosed()) {
            if ("mysql".equals(dbType)) {
                // 你爱几把用就用sql。
                connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/mcserver", "root", "password");
            } else { // 默认 SQLite
                connection = DriverManager.getConnection("jdbc:sqlite:plugins/PostSystem/posts.db");
            }
        }
        return connection;
    }

    public void initializeTables() throws SQLException {
        try (Statement stmt = getConnection().createStatement()) {
            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS posts (" +
                    "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                    "author_uuid TEXT NOT NULL," +
                    "author_name TEXT NOT NULL," +
                    "content TEXT NOT NULL," +
                    "timestamp BIGINT NOT NULL)");

            stmt.executeUpdate("CREATE TABLE IF NOT EXISTS player_views (" +
                    "player_uuid TEXT NOT NULL," +
                    "post_id INTEGER NOT NULL," +
                    "PRIMARY KEY (player_uuid, post_id))");
        }
    }

    public void createPost(UUID authorUUID, String authorName, String content) {
        String sql = "INSERT INTO posts (author_uuid, author_name, content, timestamp) VALUES (?,?,?,?)";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setString(1, authorUUID.toString());
            ps.setString(2, authorName);
            ps.setString(3, content);
            ps.setLong(4, System.currentTimeMillis());
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public int getLastSeenPostId(UUID playerUUID) {
        String sql = "SELECT MAX(post_id) FROM player_views WHERE player_uuid = ?";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setString(1, playerUUID.toString());
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return rs.getInt(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public List<Post> getPostsAfter(UUID playerUUID, int lastSeenId) {
        List<Post> list = new ArrayList<>();
        String sql = "SELECT * FROM posts WHERE id > ? ORDER BY id DESC";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setInt(1, lastSeenId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(new Post(
                        rs.getInt("id"),
                        UUID.fromString(rs.getString("author_uuid")),
                        rs.getString("author_name"),
                        rs.getString("content"),
                        rs.getLong("timestamp")
                ));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return list;
    }

    public void markPostAsViewed(UUID playerUUID, int postId) {
        String sql = "INSERT OR IGNORE INTO player_views (player_uuid, post_id) VALUES (?,?)";
        try (PreparedStatement ps = getConnection().prepareStatement(sql)) {
            ps.setString(1, playerUUID.toString());
            ps.setInt(2, postId);
            ps.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void closeConnection() {
        try {
            if (connection != null && !connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}