package com.zeshanaslam.faintstorage.utils;

import com.zeshanaslam.faintstorage.Main;
import com.zeshanaslam.faintstorage.config.SafeLocation;
import com.zeshanaslam.faintstorage.config.Upgrade;
import com.zeshanaslam.faintstorage.config.storage.StorageData;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.UUID;

public class StorageHelpers {

    private final Main main;

    public StorageHelpers(Main main) {
        this.main = main;
    }

    public StorageData createStorageData(Player player, Location location) {
        SafeLocation safeLocation = new SafeLocation().fromLocation(location);
        Upgrade upgrade = main.configStore.upgrades.get(0);
        StorageData storageData = new StorageData(UUID.randomUUID(), player.getUniqueId(), upgrade.rank, null, safeLocation);

        main.configStore.storageStore.storageData.put(safeLocation, storageData);
        return storageData;
    }
}
