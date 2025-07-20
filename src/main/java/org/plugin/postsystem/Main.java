package org.plugin.postmanager;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    @Override
    public void onEnable() {
        getLogger().info("帖子系统已启用！");
        saveDefaultConfig();
    }

    @Override
    public void onDisable() {
        getLogger().info("帖子系统已启用！");
    }
}