package com.zeshanaslam.faintstorage.utils;

import com.zeshanaslam.faintstorage.Main;
import com.zeshanaslam.faintstorage.config.Upgrade;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.List;

public class UpgradeInvHelpers {

    private final Main main;

    public UpgradeInvHelpers(Main main) {
        this.main = main;
    }

    public void openInventory(Player player, Upgrade currentUpgrade) {
        Inventory inventory = Bukkit.createInventory(null, main.configStore.upgradeSize, main.configStore.upgradeTitle);

        for (Upgrade upgrade: main.configStore.upgrades) {
            ItemStack option = new ItemStack((currentUpgrade.rank >= upgrade.rank) ? upgrade.iconUnlocked : upgrade.iconLocked);

            ItemMeta itemMeta = option.getItemMeta();
            itemMeta.setDisplayName(replaceText(main.configStore.upgradeTitle, upgrade));

            List<String> lore = new ArrayList<>();
            for (String loreText: main.configStore.upgradeItemLore) {
                lore.add(replaceText(loreText, upgrade));
            }
            itemMeta.setLore(lore);

            option.setItemMeta(itemMeta);

            inventory.setItem(upgrade.position, option);
        }

        player.openInventory(inventory);
    }

    public Upgrade getUpgradeByPosition(int position) {
        for (Upgrade upgrade: main.configStore.upgrades) {
            if (upgrade.position == position)
                return upgrade;
        }

        return null;
    }

    private String replaceText(String text, Upgrade upgrade) {
        return text.replace("%title%", upgrade.title)
                .replace("%rank%", String.valueOf(upgrade.rank))
                .replace("%size%", String.valueOf(upgrade.size))
                .replace("%cost%", String.valueOf(upgrade.cost))
                .replace("%position%", String.valueOf(upgrade.position));
    }
}
