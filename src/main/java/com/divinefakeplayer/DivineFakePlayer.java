package com.divinefakeplayer;

import net.milkbowl.vault.chat.Chat;
import com.comphenix.protocol.ProtocolLibrary;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.RegisteredServiceProvider;
import org.bukkit.plugin.java.JavaPlugin;

public final class DivineFakePlayer extends JavaPlugin {

    private Chat chat;
    private GhostManager ghostManager;
    private PacketManager packetManager;
    private DeepSeekService deepSeekService;
    private SmartChatManager smartChatManager;
    private DeathManager deathManager;
    private ConnectionSimulator connectionSimulator;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        if (!setupChat()) {
            return;
        }
        ghostManager = new GhostManager(this);
        int ghostCount = getConfig().getInt("ghost-count", 20);
        ghostManager.initializeGhosts(ghostCount);
        packetManager = new PacketManager(this);
        for (Player player : Bukkit.getOnlinePlayers()) {
            for (GhostPlayer ghost : GhostManager.getOnlineGhosts()) {
                packetManager.sendTabListAdd(ghost, player);
            }
        }
        deathManager = new DeathManager(this);
        connectionSimulator = new ConnectionSimulator(this, packetManager);
        getServer().getPluginManager().registerEvents(new ConnectionListener(this, packetManager, deathManager), this);
        ProtocolLibrary.getProtocolManager().addPacketListener(new MotdListener(this));
        deepSeekService = new DeepSeekService(this);
        smartChatManager = new SmartChatManager(this, deepSeekService, deathManager);
        getServer().getPluginManager().registerEvents(new RealPlayerChatListener(smartChatManager), this);
        smartChatManager.startIdleChat();
        deathManager.startDeathSimulation();
        connectionSimulator.startSimulation();
        getLogger().info("DivineFakePlayer enabled.");
    }

    @Override
    public void onDisable() {
        if (ghostManager != null) {
            ghostManager.clearGhosts();
        }
        getLogger().info("DivineFakePlayer disabled.");
    }

    public boolean setupChat() {
        if (getServer().getPluginManager().getPlugin("Vault") == null) {
            getLogger().severe("Vault not found. Disabling DivineFakePlayer.");
            getServer().getPluginManager().disablePlugin(this);
            return false;
        }
        RegisteredServiceProvider<Chat> provider = getServer().getServicesManager().getRegistration(Chat.class);
        if (provider == null) {
            getLogger().severe("Vault Chat provider not found. Disabling DivineFakePlayer.");
            getServer().getPluginManager().disablePlugin(this);
            return false;
        }
        chat = provider.getProvider();
        return true;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!command.getName().equalsIgnoreCase("dfp")) {
            return false;
        }
        if (args.length == 1 && args[0].equalsIgnoreCase("reload")) {
            reloadConfig();
            sender.sendMessage("DivineFakePlayer configuration reloaded.");
            return true;
        }
        sender.sendMessage("Usage: /" + label + " reload");
        return true;
    }

    public Chat getChat() {
        return chat;
    }

    public GhostManager getGhostManager() {
        return ghostManager;
    }
}
