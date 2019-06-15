package com.zeshanaslam.faintstorage.config;

import com.zeshanaslam.faintstorage.Main;
import com.zeshanaslam.faintstorage.config.storage.StorageStore;
import org.bukkit.ChatColor;
import org.bukkit.Material;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ConfigStore {

    private final Main main;
    public HashMap<Messages, String> messages;
    public List<Material> allowedItems;
    public ItemData storageItem;
    public List<Upgrade> upgrades;
    public StorageStore storageStore;
    public int upgradeSize;
    public String upgradeTitle;
    public String upgradeItemDisplay;
    public List<String> upgradeItemLore;

    public ConfigStore(Main main) {
        this.main = main;
        new File("plugins/FaintStorage/data/").mkdirs();

        messages = new HashMap<>();
        for (String key: main.getConfig().getConfigurationSection("Messages").getKeys(false)) {
            messages.put(Messages.valueOf(key), ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("Messages." + key)));
        }

        loadAllowedItems();
        loadStorageItem();
        loadUpgrades();
        loadUpgradeInv();
        storageStore = new StorageStore(main);
    }

    public enum Messages {
        NotYourBookShelf,
        PlacedBookshelf,
        NotAllowed,
        AlreadyUnlocked,
        NotEnoughMoney,
        UnlockedUpgrade,
        CannotUnlock
    }

    public void save() {
        storageStore.save();
    }

    private void loadAllowedItems() {
        allowedItems = new ArrayList<>();
        for (String materials: main.getConfig().getStringList("AllowedItems")) {
            allowedItems.add(Material.matchMaterial(materials));
        }
    }

    private void loadStorageItem() {
        Material material = Material.matchMaterial(main.getConfig().getString("Bookshelf.Material"));
        int byteData = main.getConfig().getInt("Bookshelf.ByteData");
        String display = ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("Bookshelf.Display"));
        List<String> lore = new ArrayList<>();
        for (String line:  main.getConfig().getStringList("Bookshelf.Lore")) {
            lore.add(ChatColor.translateAlternateColorCodes('&', line));
        }
        boolean glow = main.getConfig().getBoolean("Bookshelf.Glow");
        List<String> enchantments = new ArrayList<>();
        if (main.getConfig().contains("Bookshelf.Enchantments")) {
            enchantments = main.getConfig().getStringList("Bookshelf.Enchantments");
        }

        List<Integer> levels = new ArrayList<>();
        if (main.getConfig().contains("Bookshelf.Levels")) {
            levels = main.getConfig().getIntegerList("Bookshelf.Levels");
        }

        storageItem = new ItemData(material, byteData, display, lore, glow, enchantments, levels);
    }

    private void loadUpgrades() {
        upgrades = new ArrayList<>();
        for (String key: main.getConfig().getConfigurationSection("Upgrades").getKeys(false)) {
            int rank = Integer.parseInt(key);
            double cost = main.getConfig().getDouble("Upgrades." + key + ".Cost");
            int size = main.getConfig().getInt("Upgrades." + key + ".Size");
            String title = ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("Upgrades." + key + ".Title"));
            Material iconLocked = Material.matchMaterial(main.getConfig().getString("Upgrades." + key + ".IconLocked"));
            Material iconUnlocked = Material.matchMaterial(main.getConfig().getString("Upgrades." + key + ".IconUnlocked"));
            int position = main.getConfig().getInt("Upgrades." + key + ".Position");

            upgrades.add(new Upgrade(rank, cost, size, title, iconLocked, iconUnlocked, position - 1));
        }
    }

    private void loadUpgradeInv() {
        upgradeSize = main.getConfig().getInt("UpgradesInventory.Size");
        upgradeTitle = ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("UpgradesInventory.Title"));
        upgradeItemDisplay = ChatColor.translateAlternateColorCodes('&', main.getConfig().getString("UpgradesItem.Display"));

        List<String> lore = new ArrayList<>();
        for (String line:  main.getConfig().getStringList("UpgradesItem.Lore")) {
            lore.add(ChatColor.translateAlternateColorCodes('&', line));
        }
        upgradeItemLore = lore;
    }
}
