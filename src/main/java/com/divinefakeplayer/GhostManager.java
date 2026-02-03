package com.divinefakeplayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import org.bukkit.plugin.java.JavaPlugin;

public class GhostManager {

    private static GhostManager instance;
    private final JavaPlugin plugin;
    private final List<GhostPlayer> ghosts = new ArrayList<>();

    public GhostManager(JavaPlugin plugin) {
        this.plugin = plugin;
        instance = this;
    }

    public void initializeGhosts(int count) {
        ghosts.clear();
        for (int i = 0; i < count; i++) {
            ghosts.add(new GhostPlayer(randomName(), plugin));
        }
    }

    public static List<GhostPlayer> getGhosts() {
        if (instance == null) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(instance.ghosts);
    }

    public void clearGhosts() {
        ghosts.clear();
    }

    private String randomName() {
        String[] first = {"Silver", "Crimson", "Night", "Sky", "Iron", "Golden", "Shadow", "Frost", "Storm", "Lunar"};
        String[] second = {"Fox", "Rider", "Warden", "Mage", "Builder", "Seeker", "Knight", "Smith", "Hunter", "Sage"};
        int firstIndex = ThreadLocalRandom.current().nextInt(first.length);
        int secondIndex = ThreadLocalRandom.current().nextInt(second.length);
        int suffix = ThreadLocalRandom.current().nextInt(10, 99);
        return first[firstIndex] + second[secondIndex] + suffix;
    }
}
