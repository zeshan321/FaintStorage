package com.zeshanaslam.faintstorage;

import co.aikar.commands.BaseCommand;
import co.aikar.commands.CommandHelp;
import co.aikar.commands.annotation.*;
import co.aikar.commands.bukkit.contexts.OnlinePlayer;
import com.zeshanaslam.faintstorage.config.ConfigStore;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.inventory.ItemStack;

public class FSCommands extends BaseCommand {

    private Main plugin;

    public FSCommands(Main plugin) {
        super("faintstorage");
        this.plugin = plugin;
    }

    @HelpCommand
    public void help(CommandSender sender, CommandHelp help){
        help.showHelp();
    }

    @Subcommand("reload")
    @CommandPermission("faintstorage.reload")
    @Description("Reloads config")
    public void onReload(CommandSender sender) {
        plugin.configStore = new ConfigStore(plugin);
        sender.sendMessage(ChatColor.GREEN + "Config has been reloaded!");
    }

    @Subcommand("give")
    @CommandPermission("faintstorage.give")
    @Description("Gives bookshelves to player")
    @Syntax("<player> [amount]")
    @CommandCompletion("@players amount")
    public void onGive(CommandSender sender, OnlinePlayer player, @Default("1") int amount) {
        ItemStack itemStack = plugin.configStore.storageItem.getItem();
        itemStack.setAmount(amount);

        player.getPlayer().getInventory().addItem(itemStack);
        sender.sendMessage(ChatColor.GREEN + "Successfully gave player bookshelves!");
    }
}
