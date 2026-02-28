package com.stormai.plugin;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.OfflinePlayer;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class LSManager {
    private static final Map<UUID, Integer> playerMaxHealth = new HashMap<>();
    private static final File dataFile = new File(Main.getPlugin(Main.class).getDataFolder(), "playerdata.yml");

    public static void initPlayer(Player player) {
        UUID uuid = player.getUniqueId();
        int maxHealth = getHealth(uuid);

        // Apply max health
        player.setMaxHealth(maxHealth);
        if (player.getHealth() > maxHealth) {
            player.setHealth(maxHealth);
        }
    }

    public static int getHealth(UUID uuid) {
        if (playerMaxHealth.containsKey(uuid)) {
            return playerMaxHealth.get(uuid);
        }
        return 20; // default 10 hearts
    }

    public static void updateHealth(Player player, int newMaxHealth) {
        playerMaxHealth.put(player.getUniqueId(), newMaxHealth);
        player.setMaxHealth(newMaxHealth);
        if (player.getHealth() > newMaxHealth) {
            player.setHealth(newMaxHealth);
        }
    }

    public static ItemStack createHeartItem() {
        ItemStack item = new ItemStack(Material.NETHER_STAR, 1);
        ItemMeta meta = item.getItemMeta();
        meta.setDisplayName("§cHeart");
        meta.setCustomModelData(1001);
        item.setItemMeta(meta);
        return item;
    }

    public static void savePlayerData(Player player) {
        playerMaxHealth.put(player.getUniqueId(), (int) Math.round(player.getMaxHealth()));
    }

    public static void loadData() {
        if (!dataFile.exists()) return;

        FileConfiguration config = YamlConfiguration.loadConfiguration(dataFile);
        for (String uuidStr : config.getKeys(false)) {
            try {
                UUID uuid = UUID.fromString(uuidStr);
                int maxHealth = config.getInt(uuidStr);
                playerMaxHealth.put(uuid, maxHealth);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public static void saveData() {
        Main.getPlugin(Main.class).saveResource("playerdata.yml", false);
        FileConfiguration config = YamlConfiguration.loadConfiguration(dataFile);

        for (Map.Entry<UUID, Integer> entry : playerMaxHealth.entrySet()) {
            config.set(entry.getKey().toString(), entry.getValue());
        }

        try {
            config.save(dataFile);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}