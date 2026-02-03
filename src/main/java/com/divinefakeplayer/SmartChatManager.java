package com.divinefakeplayer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;

public class SmartChatManager {

    private final DivineFakePlayer plugin;
    private final DeepSeekService deepSeekService;
    private final DeathManager deathManager;

    public SmartChatManager(DivineFakePlayer plugin, DeepSeekService deepSeekService, DeathManager deathManager) {
        this.plugin = plugin;
        this.deepSeekService = deepSeekService;
        this.deathManager = deathManager;
    }

    public void startIdleChat() {
        int intervalSeconds = plugin.getConfig().getInt("chat-interval", 15);
        if (intervalSeconds <= 0) {
            return;
        }
        Bukkit.getScheduler().runTaskTimerAsynchronously(
            plugin,
            this::runIdleTick,
            20L,
            intervalSeconds * 20L
        );
    }

    private void runIdleTick() {
        List<GhostPlayer> ghosts = deathManager.getAliveGhosts(GhostManager.getOnlineGhosts());
        if (ghosts.isEmpty()) {
            return;
        }
        if (ThreadLocalRandom.current().nextDouble() < 0.5) {
            return;
        }
        GhostPlayer ghost = ghosts.get(ThreadLocalRandom.current().nextInt(ghosts.size()));
        List<String> idlePhrases = plugin.getConfig().getStringList("messages.idle-phrases");
        String phrase = idlePhrases.isEmpty()
            ? "..."
            : idlePhrases.get(ThreadLocalRandom.current().nextInt(idlePhrases.size()));
        broadcastFormatted(ghost, phrase);
    }

    public void triggerAI(String question) {
        deepSeekService.askAI(question, answer -> {
            List<GhostPlayer> ghosts = new ArrayList<>(deathManager.getAliveGhosts(GhostManager.getOnlineGhosts()));
            if (ghosts.isEmpty()) {
                return;
            }
            Collections.shuffle(ghosts);
            double replyChance = plugin.getConfig().getDouble("events.reply-chance", 0.2);
            int responders = (int) Math.max(1, Math.floor(ghosts.size() * replyChance));
            responders = Math.min(responders, ghosts.size());

            GhostPlayer lead = ghosts.get(0);
            broadcastFormatted(lead, answer);

            if (responders <= 1) {
                return;
            }
            List<String> agreementPhrases = plugin.getConfig().getStringList("messages.agreement-phrases");
            long lastTalkTime = 0L;
            long sequenceDelay = lastTalkTime + 40L;
            for (int i = 1; i < responders; i++) {
                GhostPlayer follower = ghosts.get(i);
                long gap = 30L + ThreadLocalRandom.current().nextInt(40);
                sequenceDelay += gap;
                Bukkit.getScheduler().runTaskLater(plugin, () -> {
                    String phrase = agreementPhrases.isEmpty()
                        ? "+1"
                        : agreementPhrases.get(ThreadLocalRandom.current().nextInt(agreementPhrases.size()));
                    broadcastFormatted(follower, phrase);
                }, sequenceDelay);
            }
        });
    }

    private void broadcastFormatted(GhostPlayer ghost, String messageText) {
        String format = plugin.getConfig().getString("chat-format", "{prefix}{name}: {message}");
        String message = format
            .replace("{prefix}", ghost.getPrefix())
            .replace("{name}", ghost.getName())
            .replace("{message}", messageText);
        message = ChatColor.translateAlternateColorCodes('&', message);
        Bukkit.broadcastMessage(message);
    }
}
