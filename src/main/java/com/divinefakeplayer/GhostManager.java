package com.divinefakeplayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import org.bukkit.plugin.java.JavaPlugin;

public class GhostManager {

    private static final String[] PREFIXES = {
        "Dark", "Super", "Ender", "Iron", "Silent", "Night", "Holy", "Crazy", "Happy", "Blue",
        "Red", "Snow", "Shadow", "Magic", "Hyper", "Pro", "Master", "Epic", "Lost", "Wild"
    };
    private static final String[] SUFFIXES = {
        "Wolf", "Tiger", "Dragon", "Killer", "Hunter", "Crafter", "Miner", "Knight", "King", "Panda",
        "Ghost", "Spirit", "Boy", "Girl", "Cat", "Fox", "Hawk", "Star", "Moon", "Sun"
    };

    private static GhostManager instance;
    private final JavaPlugin plugin;
    private final List<GhostPlayer> ghosts = new ArrayList<>();

    public GhostManager(JavaPlugin plugin) {
        this.plugin = plugin;
        instance = this;
    }

    public void initializeGhosts(int count) {
        ghosts.clear();
        HashSet<String> usedNames = new HashSet<>();
        while (ghosts.size() < count) {
            String prefix = PREFIXES[ThreadLocalRandom.current().nextInt(PREFIXES.length)];
            String suffix = SUFFIXES[ThreadLocalRandom.current().nextInt(SUFFIXES.length)];
            String name = prefix + suffix;
            if (!usedNames.add(name)) {
                continue;
            }
            ghosts.add(new GhostPlayer(name, plugin));
        }
    }

    public static List<GhostPlayer> getGhosts() {
        if (instance == null) {
            return Collections.emptyList();
        }
        return Collections.unmodifiableList(instance.ghosts);
    }

    public static List<GhostPlayer> getOnlineGhosts() {
        if (instance == null) {
            return Collections.emptyList();
        }
        List<GhostPlayer> onlineGhosts = new ArrayList<>();
        for (GhostPlayer ghost : instance.ghosts) {
            if (ghost.isOnline()) {
                onlineGhosts.add(ghost);
            }
        }
        return Collections.unmodifiableList(onlineGhosts);
    }

    public void clearGhosts() {
        ghosts.clear();
    }
}
