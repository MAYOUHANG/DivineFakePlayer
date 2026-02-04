package com.divinefakeplayer;

import com.comphenix.protocol.wrappers.WrappedGameProfile;
import java.util.UUID;
import java.util.concurrent.ThreadLocalRandom;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.plugin.java.JavaPlugin;

public class GhostPlayer {

    private final String name;
    private final UUID uuid;
    private final String skinTexture;
    private final String skinSignature;
    private final String prefix;
    private int ping;
    private final WrappedGameProfile profile;
    private final String displayName;
    private boolean isOnline;

    public GhostPlayer(String name, JavaPlugin plugin) {
        this.name = name;
        this.uuid = UUID.randomUUID();
        this.skinTexture = "";
        this.skinSignature = "";
        Chat chat = plugin.getServer().getServicesManager().getRegistration(Chat.class) != null
            ? plugin.getServer().getServicesManager().getRegistration(Chat.class).getProvider()
            : null;
        if (chat != null) {
            this.prefix = chat.getGroupPrefix("world", "default");
        } else {
            this.prefix = "";
        }
        this.displayName = this.prefix + this.name;
        this.ping = ThreadLocalRandom.current().nextInt(40, 301);
        this.profile = new WrappedGameProfile(this.uuid, this.name);
        this.isOnline = true;
    }

    public String getName() {
        return name;
    }

    public UUID getUuid() {
        return uuid;
    }

    public String getSkinTexture() {
        return skinTexture;
    }

    public String getSkinSignature() {
        return skinSignature;
    }

    public String getPrefix() {
        return prefix;
    }

    public int getPing() {
        return ping;
    }

    public void setRandomPing() {
        this.ping = ThreadLocalRandom.current().nextInt(40, 301);
    }

    public WrappedGameProfile getProfile() {
        return profile;
    }

    public String getDisplayName() {
        return displayName;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }
}
