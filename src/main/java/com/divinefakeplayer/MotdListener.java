package com.divinefakeplayer;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedServerPing;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class MotdListener extends PacketAdapter {

    private final Plugin plugin;

    public MotdListener(Plugin plugin) {
        super(PacketAdapter.params(plugin, PacketType.Status.Server.SERVER_INFO)
            .listenerPriority(ListenerPriority.HIGHEST)
            .optionAsync());
        this.plugin = plugin;
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        WrappedServerPing ping = event.getPacket().getServerPings().read(0);
        int ghostCount = GhostManager.getOnlineGhosts().size();
        int realPlayers = Bukkit.getOnlinePlayers().size();
        int total = realPlayers + ghostCount;
        ping.setPlayersOnline(total);
        plugin.getLogger().info("DEBUG: Async MOTD updated to " + total);
    }
}
