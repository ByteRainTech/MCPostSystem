package org.plugin.postsystem;
import org.bukkit.plugin.java.JavaPlugin;

import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.configuration.file.FileConfiguration;
import java.sql.SQLException;

public class PostSystemPlugin extends JavaPlugin {
    private DatabaseManager dbManager;
    private PostManager postManager;
    private PlayerDataManager playerDataManager;

    @Override
    public void onEnable() {
      saveDefaultConfig();
      FileConfiguration config = getConfig();
      dbManager = new DatabaseManager(config.getString("database.type", "sqlite"));
      try {
        dbManager.initializeTables(); // 可能抛 SQLException
      } catch (SQLException e) {
        getLogger().severe("无法初始化数据库: " + e.getMessage());
        getServer().getPluginManager().disablePlugin(this); // 禁用插件
        return;
      }

      postManager = new PostManager(dbManager);
      playerDataManager = new PlayerDataManager(dbManager);

      getCommand("post").setExecutor(new PostCommand(postManager, playerDataManager));
      getServer().getPluginManager().registerEvents(new JoinListener(playerDataManager, postManager), this);

      getLogger().info("PostSystem enabled!");
    }

    @Override
    public void onDisable() {
        dbManager.closeConnection();
        getLogger().info("帖子系统 已禁用！");
    }
}