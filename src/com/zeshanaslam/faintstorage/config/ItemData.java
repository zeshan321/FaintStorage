package com.zeshanaslam.faintstorage.config;

import com.zeshanaslam.faintstorage.utils.EnchantGlow;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ItemData {
    public Material material;
    public int byteData;
    public String display;
    public List<String> lore;
    public boolean glow;
    public List<String> enchantments;
    public List<Integer> levels;

    public ItemData(Material material, int byteData, String display, List<String> lore, boolean glow, List<String> enchantments, List<Integer> levels) {
        this.material = material;
        this.byteData = byteData;
        this.display = display;
        this.lore = lore;
        this.glow = glow;
        this.enchantments = enchantments;
        this.levels = levels;
    }

    public ItemStack getItem() {
        ItemStack itemStack;
        if (byteData == -1) {
            itemStack = new ItemStack(material);
        } else {
            itemStack = new ItemStack(material, 1, (byte) byteData);
        }

        ItemMeta itemMeta = itemStack.getItemMeta();
        itemMeta.setDisplayName(display);
        itemMeta.setLore(lore);

        if (glow) {
            EnchantGlow glow = new EnchantGlow(255);
            itemMeta.addEnchant(glow, 1, true);
        }

        if (!enchantments.isEmpty()) {
            if (material == Material.ENCHANTED_BOOK) {
                EnchantmentStorageMeta enchantmentStorageMeta = (EnchantmentStorageMeta) itemMeta;
                for (int i = 0; i < enchantments.size(); i++) {
                    Enchantment enchantment = Enchantment.getByName(enchantments.get(i));
                    int level = levels.get(i);

                    enchantmentStorageMeta.addStoredEnchant(enchantment, level, true);
                }
            } else  {
                for (int i = 0; i < enchantments.size(); i++) {
                    Enchantment enchantment = Enchantment.getByName(enchantments.get(i));
                    int level = levels.get(i);

                    itemMeta.addEnchant(enchantment, level, true);
                }
            }
        }

        itemStack.setItemMeta(itemMeta);
        return itemStack;
    }
}
