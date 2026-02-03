package com.divinefakeplayer;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedServerPing;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class MotdListener extends PacketAdapter {

    public MotdListener(Plugin plugin) {
        super(plugin, ListenerPriority.NORMAL, PacketType.Status.Server.SERVER_INFO);
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        WrappedServerPing ping = event.getPacket().getServerPings().read(0);
        int online = Bukkit.getOnlinePlayers().size() + GhostManager.getGhosts().size();
        ping.setPlayersOnline(online);
    }
}
