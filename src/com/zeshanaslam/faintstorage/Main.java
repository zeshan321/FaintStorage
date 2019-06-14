package com.zeshanaslam.faintstorage;

import co.aikar.commands.PaperCommandManager;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zeshanaslam.faintstorage.config.ConfigStore;
import com.zeshanaslam.faintstorage.hooks.HookHandler;
import com.zeshanaslam.faintstorage.listeners.StorageItemEvents;
import com.zeshanaslam.faintstorage.bukkitserialization.ItemStackAdapter;
import net.milkbowl.vault.economy.Economy;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    public static Gson gson;
    public HookHandler hookHandler;
    public ConfigStore configStore;
    public Economy economy;
    private static PaperCommandManager commandManager;

    @Override
    public void onEnable() {
        super.onEnable();

        // gson
        gson = new GsonBuilder()
                .registerTypeHierarchyAdapter(ItemStack.class, new ItemStackAdapter())
                .create();

        // Config
        saveDefaultConfig();
        configStore = new ConfigStore(this);

        // Setup hooks
        hookHandler = new HookHandler(this);

        // Hook into vault
        RegisteredServiceProvider<Economy> rsp = getServer().getServicesManager().getRegistration(Economy.class);
        if (rsp != null) {
            economy = rsp.getProvider();
        }

        // Commands
        if (commandManager == null) {
            commandManager = new PaperCommandManager(this);
            commandManager.enableUnstableAPI("help");
            commandManager.registerCommand(new FSCommands(this));
        }

        // Listeners
        getServer().getPluginManager().registerEvents(new StorageItemEvents(this), this);
    }

    @Override
    public void onDisable() {
        super.onDisable();

        configStore.save();
    }
}
