package com.zeshanaslam.faintstorage.listeners;

import com.zeshanaslam.faintstorage.Main;
import com.zeshanaslam.faintstorage.config.ConfigStore;
import com.zeshanaslam.faintstorage.config.SafeLocation;
import com.zeshanaslam.faintstorage.config.Upgrade;
import com.zeshanaslam.faintstorage.config.storage.StorageData;
import com.zeshanaslam.faintstorage.utils.StorageHelpers;
import com.zeshanaslam.faintstorage.utils.UpgradeInvHelpers;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.*;
import org.bukkit.event.entity.EntityExplodeEvent;
import org.bukkit.event.inventory.InventoryAction;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryDragEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.UUID;

public class StorageItemEvents implements Listener {

    private final Main main;
    private final StorageHelpers storageHelpers;
    private final UpgradeInvHelpers upgradeInvHelpers;
    private final HashMap<UUID, SafeLocation> open;

    public StorageItemEvents(Main main) {
        this.main = main;
        this.storageHelpers = new StorageHelpers(main);
        this.upgradeInvHelpers = new UpgradeInvHelpers(main);
        this.open = new HashMap<>();
    }

    @EventHandler
    public void onPlace(BlockPlaceEvent event) {
        if (event.isCancelled())
            return;

        Player player = event.getPlayer();
        if (event.getItemInHand() == null || !event.getItemInHand().hasItemMeta() || !event.getItemInHand().getItemMeta().hasDisplayName())
            return;

        if (event.getItemInHand().getItemMeta().getDisplayName().equals(main.configStore.storageItem.display)) {
            storageHelpers.createStorageData(player, event.getBlockPlaced().getLocation());
            player.sendMessage(main.configStore.messages.get(ConfigStore.Messages.PlacedBookshelf));
        }
    }

    @EventHandler
    public void onBreak(BlockBreakEvent event) {
        if (event.isCancelled())
            return;

        Block block = event.getBlock();
        Location blockLocation = block.getLocation();
        SafeLocation safeLocation = new SafeLocation().fromLocation(blockLocation);
        if (!main.configStore.storageStore.storageData.containsKey(safeLocation))
            return;

        StorageData storageData = main.configStore.storageStore.storageData.get(safeLocation);
        main.configStore.storageStore.storageData.remove(safeLocation);
        main.configStore.storageStore.storageDataHelpers.delete(storageData);

        event.setCancelled(true);
        block.setType(Material.AIR);
        blockLocation.getWorld().dropItemNaturally(blockLocation, main.configStore.storageItem.getItem());
    }

    @EventHandler
    public void onExplode(EntityExplodeEvent event) {
        if (event.isCancelled())
            return;

        for (Block block: event.blockList()) {
            Location blockLocation = block.getLocation();
            SafeLocation safeLocation = new SafeLocation().fromLocation(blockLocation);
            if (!main.configStore.storageStore.storageData.containsKey(safeLocation))
                return;

            StorageData storageData = main.configStore.storageStore.storageData.get(safeLocation);
            main.configStore.storageStore.storageData.remove(safeLocation);
            main.configStore.storageStore.storageDataHelpers.delete(storageData);
        }
    }

    @EventHandler
    public void onChange(BlockFromToEvent event) {
        if (event.isCancelled())
            return;

        Block block = event.getBlock();
        Location blockLocation = block.getLocation();
        SafeLocation safeLocation = new SafeLocation().fromLocation(blockLocation);
        if (!main.configStore.storageStore.storageData.containsKey(safeLocation))
            return;

        event.setCancelled(true);
    }

    @EventHandler
    public void onPiston(BlockPistonExtendEvent event) {
        if (event.isCancelled())
            return;

        for (Block block: event.getBlocks()) {
            Location blockLocation = block.getLocation();
            SafeLocation safeLocation = new SafeLocation().fromLocation(blockLocation);
            if (!main.configStore.storageStore.storageData.containsKey(safeLocation))
                return;

            event.setCancelled(true);
            return;
        }
    }

    @EventHandler
    public void onPiston(BlockPistonRetractEvent event) {
        if (event.isCancelled())
            return;

        for (Block block: event.getBlocks()) {
            Location blockLocation = block.getLocation();
            SafeLocation safeLocation = new SafeLocation().fromLocation(blockLocation);
            if (!main.configStore.storageStore.storageData.containsKey(safeLocation))
                return;

            event.setCancelled(true);
            return;
        }
    }

    @EventHandler
    public void onClick(PlayerInteractEvent event) {
        if (event.isCancelled())
            return;

        Player player = event.getPlayer();
        if (event.getAction() != Action.RIGHT_CLICK_BLOCK && event.getAction() != Action.LEFT_CLICK_BLOCK)
            return;

        Block block = event.getClickedBlock();
        Location blockLocation = block.getLocation();
        SafeLocation safeLocation = new SafeLocation().fromLocation(blockLocation);
        if (!main.configStore.storageStore.storageData.containsKey(safeLocation))
            return;

        StorageData storageData = main.configStore.storageStore.storageData.get(safeLocation);
        Upgrade upgrade = main.configStore.upgrades.get(storageData.rank);
        if (!main.hookHandler.isOwnerOrMember(blockLocation, player)) {
            player.sendMessage(main.configStore.messages.get(ConfigStore.Messages.NotYourBookShelf));
            return;
        }
        if (event.getAction().equals(Action.RIGHT_CLICK_BLOCK)) {
            event.setCancelled(true);

            if (!player.isSneaking()) {
                Inventory inventory = Bukkit.createInventory(player, upgrade.size, upgrade.title);
                if (storageData.contents != null) {
                    inventory.setContents(storageData.contents);
                }

                player.openInventory(inventory);
                open.put(player.getUniqueId(), storageData.safeLocation);
            }
        } else {
            if (!player.isSneaking()) {
                upgradeInvHelpers.openInventory(player, upgrade);
                open.put(player.getUniqueId(), storageData.safeLocation);
            }
        }
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        Player player = (Player) event.getPlayer();

        if (!open.containsKey(player.getUniqueId()))
            return;

        open.remove(player.getUniqueId());
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (!open.containsKey(player.getUniqueId()))
            return;

        if (event.getCursor() != null && !main.configStore.allowedItems.contains(event.getCursor().getType())) {
            player.sendMessage(main.configStore.messages.get(ConfigStore.Messages.NotAllowed));
            event.setCancelled(true);
        }

        if (event.getCurrentItem() != null && !main.configStore.allowedItems.contains(event.getCurrentItem().getType())) {
            player.sendMessage(main.configStore.messages.get(ConfigStore.Messages.NotAllowed));
            event.setCancelled(true);
        }

        if (event.getAction() == InventoryAction.HOTBAR_SWAP || event.getAction() == InventoryAction.HOTBAR_MOVE_AND_READD) {
            ItemStack itemStack = event.getView().getBottomInventory().getItem(event.getHotbarButton());

            if (itemStack != null && !main.configStore.allowedItems.contains(itemStack.getType())) {
                player.sendMessage(main.configStore.messages.get(ConfigStore.Messages.NotAllowed));
                event.setCancelled(true);
            };
        }

        if (event.getView().getTopInventory() == null  || event.getView().getTopInventory().getTitle() == null) {
            return;
        }

        if (event.isCancelled())
            return;

        SafeLocation safeLocation = open.get(player.getUniqueId());
        StorageData storageData = main.configStore.storageStore.storageData.get(safeLocation);
        Upgrade upgrade = main.configStore.upgrades.get(storageData.rank);
        if (!event.getView().getTopInventory().getTitle().equals(upgrade.title))
            return;

        main.getServer().getScheduler().runTaskLater(main, () -> {
            storageData.contents = event.getView().getTopInventory().getContents();
            main.configStore.storageStore.storageData.put(safeLocation, storageData);
        }, 5);
    }

    @EventHandler
    public void onDrag(InventoryDragEvent event) {
        if (event.getView().getTopInventory() == null  || event.getView().getTopInventory().getTitle() == null || (!(event.getWhoClicked() instanceof Player))) {
            return;
        }

        Player player = (Player) event.getWhoClicked();
        if (!open.containsKey(player.getUniqueId()))
            return;

        for (ItemStack itemStack: event.getNewItems().values()) {
            if (!main.configStore.allowedItems.contains(itemStack.getType())) {
                player.sendMessage(main.configStore.messages.get(ConfigStore.Messages.NotAllowed));

                event.setCancelled(true);
                return;
            }
        }
        if (event.isCancelled())
            return;

        SafeLocation safeLocation = open.get(player.getUniqueId());
        StorageData storageData = main.configStore.storageStore.storageData.get(safeLocation);
        Upgrade upgrade = main.configStore.upgrades.get(storageData.rank);

        main.getServer().getScheduler().runTaskLater(main, () -> {
            storageData.contents = event.getView().getTopInventory().getContents();
            main.configStore.storageStore.storageData.put(safeLocation, storageData);
        }, 5);
    }

    @EventHandler
    public void onClickUpgrade(InventoryClickEvent event) {
        if (event.getClickedInventory() == null || event.getClickedInventory().getTitle() == null || (!(event.getWhoClicked() instanceof Player))) {
            return;
        }

        if (!event.getClickedInventory().getTitle().equals(main.configStore.upgradeTitle))
            return;

        event.setCancelled(true);

        Player player = (Player) event.getWhoClicked();
        if (!open.containsKey(player.getUniqueId()))
            return;

        SafeLocation safeLocation = open.get(player.getUniqueId());
        StorageData storageData = main.configStore.storageStore.storageData.get(safeLocation);
        Upgrade currentUpgrade = main.configStore.upgrades.get(storageData.rank);
        Upgrade upgrade = upgradeInvHelpers.getUpgradeByPosition(event.getSlot());

        if (upgrade == null)
            return;

        if (currentUpgrade.rank >= upgrade.rank) {
            player.sendMessage(main.configStore.messages.get(ConfigStore.Messages.AlreadyUnlocked));
            return;
        }

        if (main.economy.getBalance(Bukkit.getOfflinePlayer(player.getUniqueId())) <  upgrade.cost) {
            player.sendMessage(main.configStore.messages.get(ConfigStore.Messages.NotEnoughMoney));
            return;
        }

        if (storageData.rank + 1 != upgrade.rank) {
            player.sendMessage(main.configStore.messages.get(ConfigStore.Messages.CannotUnlock));
            return;
        }

        // Update rank
        storageData.rank = upgrade.rank;
        main.configStore.storageStore.storageData.put(safeLocation, storageData);

        player.closeInventory();
        //upgradeInvHelpers.openInventory(player, upgrade);
        player.sendMessage(main.configStore.messages.get(ConfigStore.Messages.UnlockedUpgrade));
    }
}
