package com.zeshanaslam.faintstorage.config.storage;

import com.zeshanaslam.faintstorage.Main;
import com.zeshanaslam.faintstorage.config.SafeLocation;

import java.util.HashMap;

public class StorageStore {

    private final Main main;
    public StorageDataHelpers storageDataHelpers;
    public HashMap<SafeLocation, StorageData> storageData;

    public StorageStore(Main main) {
        this.main = main;
        this.storageDataHelpers = new StorageDataHelpers(main);

        storageData = new HashMap<>();
        for (StorageData data: storageDataHelpers.getAllStorageData()) {
            storageData.put(data.safeLocation, data);
        }
    }

    public void save() {
        for (StorageData data: storageData.values()) {
            storageDataHelpers.createOrUpdateStorageData(data);
        }
    }
}
