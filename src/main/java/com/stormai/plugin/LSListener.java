package com.stormai.plugin;

import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class LSListener implements Listener {
    private final Main plugin;

    public LSListener(Main plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        LSManager.initPlayer(player);
    }

    @EventHandler
    public void onPlayerQuit(PlayerQuitEvent event) {
        Player player = event.getPlayer();
        LSManager.savePlayerData(player);
    }

    @EventHandler(priority = EventPriority.HIGHEST)
    public void onEntityDeath(EntityDeathEvent event) {
        if (!(event.getEntity() instanceof Player)) return;
        if (!(event.getEntity().getKiller() instanceof Player)) return;

        Player killer = event.getEntity().getKiller();
        Player victim = (Player) event.getEntity();

        // Check if killer is a player and not a mob/TNT
        if (killer == null || victim == null) return;

        int maxHealth = (int) Math.round(victim.getMaxHealth());
        if (maxHealth <= 2) {
            // Elimination - ban player or set to spectator
            Bukkit.broadcastMessage("§c" + victim.getName() + " has been eliminated!");
            victim.setHealth(0);
            // Uncomment for ban instead of spectator
            // victim.kickPlayer("You have been eliminated!");
            // Bukkit.getBanList(BanList.Type.NAME).addBan(victim.getName(), "Eliminated", null, null);
            victim.setGameMode(org.bukkit.GameMode.SPECTATOR);
            return;
        }

        // Transfer heart
        int newMaxHealth = maxHealth - 1;
        victim.setMaxHealth(newMaxHealth);
        LSManager.updateHealth(victim, newMaxHealth);

        // Give killer +1 max health
        int killerMaxHealth = (int) Math.round(killer.getMaxHealth());
        if (killerMaxHealth < 40) {
            killer.setMaxHealth(killerMaxHealth + 1);
            LSManager.updateHealth(killer, killerMaxHealth + 1);
            killer.sendMessage("§a+1 Max Health! You now have " + killer.getMaxHealth() + " HP.");
        }
    }

    @EventHandler
    public void onEntityDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player && event.getCause() == DamageCause.VOID) {
            Player player = (Player) event.getEntity();
            if (player.getHealth() <= 2) {
                event.setCancelled(true);
                player.setHealth(0);
            }
        }
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        if (player.getHealth() <= 2) {
            Bukkit.broadcastMessage("§c" + player.getName() + " has been eliminated!");
            player.setGameMode(org.bukkit.GameMode.SPECTATOR);
        }
    }
}