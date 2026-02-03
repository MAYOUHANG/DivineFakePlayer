package com.divinefakeplayer;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

public class ChatTask implements Runnable {

    private final JavaPlugin plugin;
    private final GhostManager ghostManager;
    private final MockAI mockAI;

    public ChatTask(JavaPlugin plugin, GhostManager ghostManager, MockAI mockAI) {
        this.plugin = plugin;
        this.ghostManager = ghostManager;
        this.mockAI = mockAI;
    }

    @Override
    public void run() {
        List<GhostPlayer> ghosts = ghostManager.getGhosts();
        if (ghosts.size() < 2) {
            return;
        }
        GhostPlayer asker = ghosts.get(ThreadLocalRandom.current().nextInt(ghosts.size()));
        GhostPlayer answerer = ghosts.get(ThreadLocalRandom.current().nextInt(ghosts.size()));
        if (asker == answerer) {
            return;
        }
        String[] prompts = {"server", "lag", "spawn", "event", "shop", "mines", "quests"};
        String prompt = prompts[ThreadLocalRandom.current().nextInt(prompts.length)];
        String response = mockAI.respond(prompt);

        String format = plugin.getConfig().getString("chat-format", "{prefix}{name}: {message}");
        String message = format
            .replace("{prefix}", answerer.getPrefix())
            .replace("{name}", answerer.getName())
            .replace("{message}", response);
        message = ChatColor.translateAlternateColorCodes('&', message);
        Bukkit.broadcastMessage(message);
    }
}
