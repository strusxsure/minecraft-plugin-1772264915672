package com.stormai.plugin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class LSCommand implements CommandExecutor {
    private final Main plugin;

    public LSCommand(Main plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("§cOnly players can use this command.");
            return true;
        }

        Player player = (Player) sender;

        if (args.length == 0) {
            player.sendMessage("§6/Lifesteal withdraw <amount>");
            return true;
        }

        if (args[0].equalsIgnoreCase("withdraw")) {
            if (args.length < 2) {
                player.sendMessage("§cUsage: /lifesteal withdraw <amount>");
                return true;
            }

            int amount;
            try {
                amount = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                player.sendMessage("§cInvalid amount.");
                return true;
            }

            if (amount <= 0) {
                player.sendMessage("§cAmount must be positive.");
                return true;
            }

            int currentHealth = (int) Math.round(player.getHealth());
            if (amount > currentHealth - 2) {
                player.sendMessage("§cYou can't withdraw that much. Minimum health is 2 HP.");
                return true;
            }

            // Create heart items
            ItemStack heart = LSManager.createHeartItem();
            for (int i = 0; i < amount; i++) {
                player.getInventory().addItem(heart);
            }

            player.setHealth(currentHealth - amount);
            player.sendMessage("§aWithdrew " + amount + " health as Heart items.");
            return true;
        }

        return false;
    }
}