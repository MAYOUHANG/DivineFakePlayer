package com.divinefakeplayer;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import java.io.IOException;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.function.Consumer;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class DeepSeekService {

    private final JavaPlugin plugin;
    private final Gson gson = new Gson();

    public DeepSeekService(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    public void askAI(String userMessage, Consumer<String> callback) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            String apiUrl = plugin.getConfig().getString("ai-settings.api-url");
            String apiKey = plugin.getConfig().getString("ai-settings.api-key");
            String model = plugin.getConfig().getString("ai-settings.model");
            String systemPrompt = plugin.getConfig().getString("ai-settings.system-prompt", "");

            JsonObject payload = new JsonObject();
            payload.addProperty("model", model);
            JsonArray messages = new JsonArray();
            JsonObject systemMessage = new JsonObject();
            systemMessage.addProperty("role", "system");
            systemMessage.addProperty("content", systemPrompt);
            messages.add(systemMessage);
            JsonObject user = new JsonObject();
            user.addProperty("role", "user");
            user.addProperty("content", userMessage);
            messages.add(user);
            payload.add("messages", messages);
            payload.addProperty("max_tokens", 50);

            String responseText = "";
            try {
                HttpURLConnection connection = (HttpURLConnection) new URL(apiUrl).openConnection();
                connection.setRequestMethod("POST");
                connection.setDoOutput(true);
                connection.setRequestProperty("Authorization", "Bearer " + apiKey);
                connection.setRequestProperty("Content-Type", "application/json");

                byte[] body = gson.toJson(payload).getBytes(StandardCharsets.UTF_8);
                try (OutputStream outputStream = connection.getOutputStream()) {
                    outputStream.write(body);
                }

                int status = connection.getResponseCode();
                if (status >= 200 && status < 300) {
                    String responseBody = new String(connection.getInputStream().readAllBytes(), StandardCharsets.UTF_8);
                    responseText = parseResponse(responseBody);
                } else {
                    responseText = "";
                }
            } catch (IOException e) {
                responseText = "";
            }

            String finalResponse = responseText;
            Bukkit.getScheduler().runTask(plugin, () -> callback.accept(finalResponse));
        });
    }

    private String parseResponse(String responseBody) {
        JsonObject root = gson.fromJson(responseBody, JsonObject.class);
        if (root == null || !root.has("choices")) {
            return "";
        }
        JsonArray choices = root.getAsJsonArray("choices");
        if (choices == null || choices.size() == 0) {
            return "";
        }
        JsonObject choice = choices.get(0).getAsJsonObject();
        if (choice == null || !choice.has("message")) {
            return "";
        }
        JsonObject message = choice.getAsJsonObject("message");
        if (message == null || !message.has("content")) {
            return "";
        }
        return message.get("content").getAsString();
    }
}
