package com.stormai.plugin;

import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    @Override
    public void onEnable() {
        // Register events
        getServer().getPluginManager().registerEvents(new LSListener(this), this);

        // Register commands
        getCommand("lifesteal").setExecutor(new LSCommand(this));

        // Register recipe
        new HeartRecipe().register(this);

        // Load player data
        LSManager.loadData();
    }

    @Override
    public void onDisable() {
        // Save player data
        LSManager.saveData();
    }
}