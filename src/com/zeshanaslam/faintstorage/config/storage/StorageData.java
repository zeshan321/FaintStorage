package com.zeshanaslam.faintstorage.config.storage;

import com.zeshanaslam.faintstorage.config.SafeLocation;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.UUID;

public class StorageData {
    public UUID id;
    public UUID owner;
    public int rank;
    public ItemStack[] contents;
    public SafeLocation safeLocation;

    public StorageData(UUID id, UUID owner, int rank, ItemStack[] contents, SafeLocation safeLocation) {
        this.id = id;
        this.owner = owner;
        this.rank = rank;
        this.contents = contents;
        this.safeLocation = safeLocation;
    }
}
