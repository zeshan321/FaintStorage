package com.zeshanaslam.faintstorage.config.storage;

import com.zeshanaslam.faintstorage.Main;
import com.zeshanaslam.faintstorage.utils.FileHandler;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class StorageDataHelpers {

    private Main main;
    private final String path;

    public StorageDataHelpers(Main main) {
        this.main = main;
        this.path = "plugins/FaintStorage/data/";
    }

    public void createOrUpdateStorageData(StorageData storageData) {
        FileHandler fileHandler = new FileHandler(path + storageData.id + ".yml");
        fileHandler.set("data", Main.gson.toJson(storageData, StorageData.class));
        fileHandler.save();
    }

    public void delete(StorageData storageData) {
        FileHandler fileHandler = new FileHandler(path + storageData.id + ".yml");
        fileHandler.delete();
    }

    public StorageData getStorageData(UUID id) {
        if (!FileHandler.fileExists(path + id.toString() + ".yml")) {
            System.err.println("Storage data not found: " + path + id.toString() + ".yml");
            return null;
        }

        FileHandler fileHandler = new FileHandler(path + id.toString() + ".yml");
        return Main.gson.fromJson(fileHandler.getString("data"), StorageData.class);
    }

    public List<StorageData> getAllStorageData() {
        List<StorageData> storageData = new ArrayList<>();
        try (Stream<Path> walk = Files.walk(Paths.get(path))) {

            List<String> result = walk.map(Path::toString)
                    .filter(f -> f.contains(".yml"))
                    .collect(Collectors.toList());

            for (String file: result) {
                file = file.replace("plugins\\FaintStorage\\data\\", "").replace("plugins/FaintStorage/data/", "").replace(".yml", "");
                UUID id = UUID.fromString(file);

                storageData.add(getStorageData(id));
            }

        } catch (IOException e) {
            e.printStackTrace();
        }

        return storageData;
    }
}
