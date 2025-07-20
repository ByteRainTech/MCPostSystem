package org.plugin.postsystem;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import java.util.List;

public class JoinListener implements Listener {
    private final PlayerDataManager playerDataManager;
    private final PostManager postManager;

    public JoinListener(PlayerDataManager playerDataManager, PostManager postManager) {
        this.playerDataManager = playerDataManager;
        this.postManager = postManager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        List<Post> unseenPosts = postManager.getUnseenPosts(player.getUniqueId());

        if (!unseenPosts.isEmpty()) {
            Post latestPost = unseenPosts.get(0); // Get most recent
            player.sendMessage("§6有新的帖子! §7(由 " + latestPost.getAuthorName() + " 发布)");
            player.sendMessage("§f" + latestPost.getContent());
            player.sendMessage("§e输入 §6/post list §e来查看新的帖子！");

            // Mark as viewed
            playerDataManager.markPostsAsViewed(player.getUniqueId(), unseenPosts);
        }
    }
}