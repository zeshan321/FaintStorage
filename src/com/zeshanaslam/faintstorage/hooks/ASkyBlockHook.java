package com.zeshanaslam.faintstorage.hooks;

import com.wasteofplastic.askyblock.ASkyBlockAPI;
import com.wasteofplastic.askyblock.Island;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ASkyBlockHook {

    public UUID getOwner(Location location) {
        ASkyBlockAPI aSkyBlockAPI = ASkyBlockAPI.getInstance();
        Island island = aSkyBlockAPI.getIslandAt(location);

        return island.getOwner();
    }

    public boolean isMember(Location location, Player player) {
        ASkyBlockAPI aSkyBlockAPI = ASkyBlockAPI.getInstance();
        Island island = aSkyBlockAPI.getIslandAt(location);

        return island.getOwner() == player.getUniqueId() || island.getMembers().contains(player.getUniqueId());
    }
}
