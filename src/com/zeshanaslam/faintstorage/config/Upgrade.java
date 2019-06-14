package com.zeshanaslam.faintstorage.config;

import org.bukkit.Material;

public class Upgrade {
    public int rank;
    public double cost;
    public int size;
    public String title;
    public Material iconLocked;
    public Material iconUnlocked;
    public int position;

    public Upgrade(int rank, double cost, int size, String title, Material iconLocked, Material iconUnlocked, int position) {
        this.rank = rank;
        this.cost = cost;
        this.size = size;
        this.title = title;
        this.iconLocked = iconLocked;
        this.iconUnlocked = iconUnlocked;
        this.position = position;
    }
}
