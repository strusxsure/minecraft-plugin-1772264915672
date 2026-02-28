package com.stormai.plugin;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;

public class HeartRecipe {
    public void register(Main plugin) {
        ItemStack heart = LSManager.createHeartItem();

        NamespacedKey key = new NamespacedKey(plugin, "heart_recipe");
        ShapedRecipe recipe = new ShapedRecipe(key, heart);

        recipe.shape("GGG", "GNG", "GGG");
        recipe.setIngredient('G', Material.GOLD_BLOCK);
        recipe.setIngredient('N', Material.NETHER_STAR);

        plugin.getServer().addRecipe(recipe);
    }
}