package com.divinefakeplayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;

public class ConnectionListener implements Listener {

    private final GhostManager ghostManager;
    private final PacketManager packetManager;
    private final DivineFakePlayer plugin;

    public ConnectionListener(DivineFakePlayer plugin, GhostManager ghostManager, PacketManager packetManager) {
        this.plugin = plugin;
        this.ghostManager = ghostManager;
        this.packetManager = packetManager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {
        for (GhostPlayer ghost : ghostManager.getGhosts()) {
            packetManager.sendTabListAdd(ghost, event.getPlayer());
        }

        double welcomeChance = plugin.getConfig().getDouble("events.welcome-chance", 0.6);
        List<GhostPlayer> shuffled = new ArrayList<>(ghostManager.getGhosts());
        Collections.shuffle(shuffled);
        int speakers = (int) Math.floor(shuffled.size() * welcomeChance);
        if (speakers <= 0) {
            return;
        }
        List<String> welcomePhrases = plugin.getConfig().getStringList("messages.welcome-phrases");
        String format = plugin.getConfig().getString("chat-format", "{prefix}{name}: {message}");
        long baseDelay = 20L;
        for (int i = 0; i < speakers && i < shuffled.size(); i++) {
            GhostPlayer ghost = shuffled.get(i);
            long delay = baseDelay + ThreadLocalRandom.current().nextInt(60);
            Bukkit.getScheduler().runTaskLater(plugin, () -> {
                String phrase = welcomePhrases.isEmpty()
                    ? "Welcome!"
                    : welcomePhrases.get(ThreadLocalRandom.current().nextInt(welcomePhrases.size()));
                String message = format
                    .replace("{prefix}", ghost.getPrefix())
                    .replace("{name}", ghost.getName())
                    .replace("{message}", phrase);
                message = ChatColor.translateAlternateColorCodes('&', message);
                Bukkit.broadcastMessage(message);
            }, delay);
            baseDelay = delay + 20L;
        }
    }
}
