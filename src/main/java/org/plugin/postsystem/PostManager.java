package org.plugin.postsystem;

import org.bukkit.entity.Player;
import java.util.List;
import java.util.UUID;

public class PostManager {
    private final DatabaseManager dbManager;

    public PostManager(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }

    public void createPost(Player author, String content) {
        dbManager.createPost(author.getUniqueId(), author.getName(), content);
    }

    public List<Post> getUnseenPosts(UUID playerUUID) {
        return dbManager.getPostsAfter(playerUUID, dbManager.getLastSeenPostId(playerUUID));
    }
}