package com.divinefakeplayer;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.server.ServerListPingEvent;

public class MotdListener implements Listener {

    private final GhostManager ghostManager;

    public MotdListener(GhostManager ghostManager) {
        this.ghostManager = ghostManager;
    }

    @EventHandler
    public void onServerListPing(ServerListPingEvent event) {
        int totalPlayers = Bukkit.getOnlinePlayers().size() + ghostManager.getGhosts().size();
        event.setNumPlayers(totalPlayers);
    }
}
