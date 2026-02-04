package com.divinefakeplayer;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.EnumWrappers.NativeGameMode;
import com.comphenix.protocol.wrappers.EnumWrappers.PlayerInfoAction;
import com.comphenix.protocol.wrappers.PlayerInfoData;
import com.comphenix.protocol.wrappers.WrappedChatComponent;
import com.comphenix.protocol.wrappers.WrappedGameProfile;
import com.comphenix.protocol.wrappers.WrappedSignedProperty;
import java.util.Collections;
import java.util.EnumSet;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class PacketManager {
    private static final EnumSet<PlayerInfoAction> TAB_LIST_ACTIONS = EnumSet.of(
        PlayerInfoAction.ADD_PLAYER,
        PlayerInfoAction.UPDATE_LISTED,
        PlayerInfoAction.UPDATE_LATENCY,
        PlayerInfoAction.UPDATE_GAME_MODE,
        PlayerInfoAction.UPDATE_DISPLAY_NAME
    );

    private final JavaPlugin plugin;
    private final ProtocolManager protocolManager;

    public PacketManager(JavaPlugin plugin) {
        this.plugin = plugin;
        this.protocolManager = ProtocolLibrary.getProtocolManager();
    }

    public void sendTabListAdd(Player target, GhostPlayer ghost) {
        PacketContainer packet = protocolManager.createPacket(PacketType.Play.Server.PLAYER_INFO);
        packet.getPlayerInfoActions().write(0, TAB_LIST_ACTIONS);

        WrappedGameProfile profile = ghost.getProfile();
        applySkin(profile, ghost);

        PlayerInfoData infoData = new PlayerInfoData(
            ghost.getUuid(),
            ghost.getPing(),
            true,
            NativeGameMode.SURVIVAL,
            profile,
            WrappedChatComponent.fromLegacyText(
                ChatColor.translateAlternateColorCodes('&', ghost.getDisplayName())
            )
        );

        packet.getPlayerInfoDataLists().write(1, Collections.singletonList(infoData));

        try {
            protocolManager.sendServerPacket(target, packet);
        } catch (Exception e) {
            plugin.getLogger().severe("Failed to send tab list add packet: " + e.getMessage());
        }
    }

    public void sendTabListRemove(Player target, GhostPlayer ghost) {
        PacketContainer packet = protocolManager.createPacket(PacketType.Play.Server.PLAYER_INFO_REMOVE);
        packet.getUUIDLists().write(0, Collections.singletonList(ghost.getUuid()));

        try {
            protocolManager.sendServerPacket(target, packet);
        } catch (Exception e) {
            plugin.getLogger().severe("Failed to send tab list remove packet: " + e.getMessage());
        }
    }

    public void updateTabForAll(GhostPlayer ghost) {
        for (Player player : Bukkit.getOnlinePlayers()) {
            sendTabListAdd(player, ghost);
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
