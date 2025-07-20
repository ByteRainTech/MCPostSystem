package org.plugin.postsystem;

import java.util.List;
import java.util.UUID;

public class PlayerDataManager {
    private final DatabaseManager dbManager;

    public PlayerDataManager(DatabaseManager dbManager) {
        this.dbManager = dbManager;
    }

    public void markPostsAsViewed(UUID playerUUID, List<Post> posts) {
        for (Post post : posts) {
            dbManager.markPostAsViewed(playerUUID, post.getId());
        }
    }
}