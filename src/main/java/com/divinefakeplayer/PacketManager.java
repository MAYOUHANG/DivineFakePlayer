package com.divinefakeplayer;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.comphenix.protocol.wrappers.WrappedSignedProperty;
import java.util.EnumSet;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class PacketManager {
    private static final PacketType INFO_UPDATE_PACKET;
    private static final PacketType INFO_REMOVE_PACKET;

    static {
        PacketType updatePacket = null;
        PacketType removePacket = null;
        try {
            java.lang.reflect.Field updateField = PacketType.Play.Server.class.getField("PLAYER_INFO_UPDATE");
            updatePacket = (PacketType) updateField.get(null);

            java.lang.reflect.Field removeField = PacketType.Play.Server.class.getField("PLAYER_INFO_REMOVE");
            removePacket = (PacketType) removeField.get(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        INFO_UPDATE_PACKET = updatePacket;
        INFO_REMOVE_PACKET = removePacket;
    }

    private static final EnumSet<EnumWrappers.PlayerInfoAction> TAB_LIST_ACTIONS = EnumSet.of(
        EnumWrappers.PlayerInfoAction.ADD_PLAYER,
        EnumWrappers.PlayerInfoAction.UPDATE_LISTED,
        EnumWrappers.PlayerInfoAction.UPDATE_LATENCY,
        EnumWrappers.PlayerInfoAction.UPDATE_DISPLAY_NAME
    );

    private final JavaPlugin plugin;
    private final ProtocolManager protocolManager;

    public PacketManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.protocolManager = ProtocolLibrary.getProtocolManager();
    }

    public void sendTabListAdd(GhostPlayer ghost, Player receiver) {
        if (INFO_UPDATE_PACKET == null) {
            plugin.getLogger().severe("PLAYER_INFO_UPDATE packet type unavailable. Check ProtocolLib version.");
            return;
        }
        PacketContainer packet = protocolManager.createPacket(INFO_UPDATE_PACKET);
        packet.getPlayerInfoActions().write(0, TAB_LIST_ACTIONS);

        WrappedGameProfile profile = new WrappedGameProfile(ghost.getUuid(), ghost.getName());
        applySkin(profile, ghost);

        int latency = ThreadLocalRandom.current().nextInt(10, 101);
        WrappedChatComponent displayName = WrappedChatComponent.fromText(ghost.getPrefix() + ghost.getName());

        PlayerInfoData infoData = new PlayerInfoData(
            ghost.getUuid(),
            latency,
            true,
            EnumWrappers.NativeGameMode.SURVIVAL,
            profile,
            displayName
        );

        packet.getPlayerInfoDataLists().write(1, List.of(infoData));

        try {
            protocolManager.sendServerPacket(receiver, packet);
        } catch (Exception e) {
            plugin.getLogger().severe("Failed to send tab list add packet: " + e.getMessage());
        }
    }

    public void sendTabListRemove(GhostPlayer ghost, Player receiver) {
        if (INFO_REMOVE_PACKET == null) {
            plugin.getLogger().severe("PLAYER_INFO_REMOVE packet type unavailable. Check ProtocolLib version.");
            return;
        }
        PacketContainer packet = protocolManager.createPacket(INFO_REMOVE_PACKET);
        packet.getUUIDLists().write(0, List.of(ghost.getUuid()));

        try {
            protocolManager.sendServerPacket(receiver, packet);
        } catch (Exception e) {
            plugin.getLogger().severe("Failed to send tab list remove packet: " + e.getMessage());
        }
    }

    public void updateTabForAll(GhostPlayer ghost) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            sendTabListAdd(ghost, player);
        }
    }

    private void applySkin(WrappedGameProfile profile, GhostPlayer ghost) {
        String texture = ghost.getSkinTexture();
        String signature = ghost.getSkinSignature();
        if (texture == null || texture.isBlank() || signature == null || signature.isBlank()) {
            return;
        }
        profile.getProperties().put("textures", new WrappedSignedProperty("textures", texture, signature));
    }
}
