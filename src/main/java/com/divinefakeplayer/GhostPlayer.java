package com.divinefakeplayer;

import java.util.UUID;
import net.milkbowl.vault.chat.Chat;
import org.bukkit.plugin.java.JavaPlugin;

public class GhostPlayer {

    private final String name;
    private final UUID uuid;
    private final String skinTexture;
    private final String skinSignature;
    private final String prefix;

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
}
