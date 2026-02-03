package com.divinefakeplayer;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class PacketManager {

    private final JavaPlugin plugin;
    private final ProtocolManager protocolManager;

    public PacketManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.protocolManager = ProtocolLibrary.getProtocolManager();
    }

    public void sendTabListAdd(GhostPlayer ghost, Player receiver) {
        PacketContainer packet = protocolManager.createPacket(PacketType.Play.Server.PLAYER_INFO);
        packet.getPlayerInfoAction().write(0, EnumWrappers.PlayerInfoAction.ADD_PLAYER);
        WrappedGameProfile profile = new WrappedGameProfile(ghost.getUuid(), ghost.getName());
        int latency = ThreadLocalRandom.current().nextInt(10, 101);
        WrappedChatComponent displayName = WrappedChatComponent.fromText(ghost.getPrefix() + ghost.getName());
        PlayerInfoData infoData = new PlayerInfoData(profile, latency, EnumWrappers.NativeGameMode.SURVIVAL, displayName);
        List<PlayerInfoData> infoList = new ArrayList<>();
        infoList.add(infoData);
        packet.getPlayerInfoDataLists().write(0, infoList);
        try {
            protocolManager.sendServerPacket(receiver, packet);
        } catch (Exception e) {
            plugin.getLogger().severe("Failed to send tab list add packet: " + e.getMessage());
        }
    }

    public void sendTabListRemove(GhostPlayer ghost, Player receiver) {
        PacketContainer packet = protocolManager.createPacket(PacketType.Play.Server.PLAYER_INFO);
        packet.getPlayerInfoAction().write(0, EnumWrappers.PlayerInfoAction.REMOVE_PLAYER);
        WrappedGameProfile profile = new WrappedGameProfile(ghost.getUuid(), ghost.getName());
        PlayerInfoData infoData = new PlayerInfoData(profile, 0, EnumWrappers.NativeGameMode.SURVIVAL, null);
        List<PlayerInfoData> infoList = new ArrayList<>();
        infoList.add(infoData);
        packet.getPlayerInfoDataLists().write(0, infoList);
        try {
            protocolManager.sendServerPacket(receiver, packet);
        } catch (Exception e) {
            plugin.getLogger().severe("Failed to send tab list remove packet: " + e.getMessage());
        }
    }
}
