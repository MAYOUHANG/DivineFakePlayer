package com.divinefakeplayer;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.WrappedServerPing;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;

public class MotdListener extends PacketAdapter {

    public MotdListener(Plugin plugin) {
        super(PacketAdapter.params(plugin, PacketType.Status.Server.SERVER_INFO)
            .optionAsync()
            .listenerPriority(com.comphenix.protocol.events.ListenerPriority.HIGHEST));
    }

    @Override
    public void onPacketSending(PacketEvent event) {
        try {
            WrappedServerPing ping = event.getPacket().getServerPings().read(0);
            if (ping == null) {
                return;
            }

            int realPlayers = Bukkit.getOnlinePlayers().size();
            int fakePlayers = GhostManager.getOnlineGhosts().size();
            int totalPlayers = realPlayers + fakePlayers;
            ping.setPlayersOnline(totalPlayers);
            ping.setVersionProtocol(763);
            ping.setVersionName("1.20.1");
        } catch (Exception ignored) {
        }
    }
}
