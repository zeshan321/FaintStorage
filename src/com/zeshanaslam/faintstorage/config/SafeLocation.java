package com.zeshanaslam.faintstorage.config;

import org.bukkit.Bukkit;
import org.bukkit.Location;

import java.util.Objects;
import java.util.UUID;

public class SafeLocation {
    public UUID world;
    public double x;
    public double y;
    public double z;
    public float pitch;
    public float yaw;

    public SafeLocation() {}

    public SafeLocation(UUID world, double x, double y, double z, float pitch, float yaw) {
        this.world = world;
        this.x = x;
        this.y = y;
        this.z = z;
        this.pitch = pitch;
        this.yaw = yaw;
    }

    public Location getLocation() {
        Location location = new Location(Bukkit.getWorld(world), x, y, z);
        location.setPitch(pitch);
        location.setYaw(yaw);

        return location;
    }

    public SafeLocation fromLocation(Location location) {
        world = location.getWorld().getUID();
        x = location.getBlockX();
        y = location.getBlockY();
        z = location.getBlockZ();
        pitch = location.getPitch();
        yaw = location.getYaw();

        return this;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SafeLocation that = (SafeLocation) o;
        return Double.compare(that.x, x) == 0 &&
                Double.compare(that.y, y) == 0 &&
                Double.compare(that.z, z) == 0 &&
                Float.compare(that.pitch, pitch) == 0 &&
                Float.compare(that.yaw, yaw) == 0 &&
                Objects.equals(world, that.world);
    }

    @Override
    public int hashCode() {
        return Objects.hash(world, x, y, z, pitch, yaw);
    }
}
