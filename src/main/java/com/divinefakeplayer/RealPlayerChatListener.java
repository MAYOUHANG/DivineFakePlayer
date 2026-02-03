package com.divinefakeplayer;

import java.util.Arrays;
import java.util.List;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class RealPlayerChatListener implements Listener {

    private final SmartChatManager smartChatManager;
    private final List<String> keywords = Arrays.asList("?", "怎么", "服务器", "ip", "IP", "Ip");

    public RealPlayerChatListener(SmartChatManager smartChatManager) {
        this.smartChatManager = smartChatManager;
    }

    @EventHandler
    public void onPlayerChat(AsyncPlayerChatEvent event) {
        String message = event.getMessage();
        for (String keyword : keywords) {
            if (message.contains(keyword)) {
                smartChatManager.triggerAI(message);
                return;
            }
        }
    }
}
