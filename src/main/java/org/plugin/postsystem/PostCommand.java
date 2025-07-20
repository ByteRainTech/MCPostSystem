package org.plugin.postsystem;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import java.util.*;

public class PostCommand implements CommandExecutor {
    private final PostManager postManager;
    private final PlayerDataManager playerDataManager;

    public PostCommand(PostManager postManager, PlayerDataManager playerDataManager) {
        this.postManager = postManager;
        this.playerDataManager = playerDataManager;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage("只有玩家可以使用这个指令哦~");
            return true;
        }

        if (args.length == 0) {
            player.sendMessage("§6帖子系统帮助:");
            player.sendMessage("§e/post create <消息> §7- 创建一个帖子");
            player.sendMessage("§e/post list §7- 查看未读的所有帖子。");
            return true;
        }

        if (args[0].equalsIgnoreCase("create") && args.length > 1) {
            String content = String.join(" ", Arrays.copyOfRange(args, 1, args.length));
            postManager.createPost(player, content);
            player.sendMessage("§a发帖成功！其他玩家上线后将会看到！");
            return true;
        }

        if (args[0].equalsIgnoreCase("list")) {
            List<Post> unseen = postManager.getUnseenPosts(player.getUniqueId());
            if (unseen.isEmpty()) {
                player.sendMessage("§7暂无新帖子~");
            } else {
                player.sendMessage("§6--- 最新帖子 ---");
                for (Post p : unseen) {
                    player.sendMessage("§e" + p.getAuthorName() + ": §f" + p.getContent());
                }
                playerDataManager.markPostsAsViewed(player.getUniqueId(), unseen);
            }
            return true;
        }

        return false;
    }
}