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
        super(plugin, ListenerPriority.HIGHEST, PacketType.Status.Server.SERVER_INFO);
        this.plugin = plugin;
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        WrappedServerPing ping = event.getPacket().getServerPings().read(0);
        int fakeCount = GhostManager.getOnlineGhosts().size();
        int realCount = Bukkit.getOnlinePlayers().size();
        int online = realCount + fakeCount;
        plugin.getLogger().info("DEBUG: MOTD Packet intercepted. Setting count to: " + online);
        ping.setPlayersOnline(online);
    }
}
