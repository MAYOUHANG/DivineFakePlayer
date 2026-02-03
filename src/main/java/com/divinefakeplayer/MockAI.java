package com.divinefakeplayer;

import java.util.LinkedHashMap;
import java.util.Map;

public class MockAI {

    private final Map<String, String> responses = new LinkedHashMap<>();

    public MockAI() {
        responses.put("server", "It's awesome.");
        responses.put("lag", "The TPS looks great right now!");
        responses.put("spawn", "Spawn is looking shiny today.");
        responses.put("event", "Can't wait for the next event!");
        responses.put("shop", "The market is buzzing with deals.");
    }

    public String respond(String prompt) {
        if (prompt == null) {
            return "Sounds good!";
        }
        String lowered = prompt.toLowerCase();
        for (Map.Entry<String, String> entry : responses.entrySet()) {
            if (lowered.contains(entry.getKey())) {
                return entry.getValue();
            }
        }
        return "Sounds good!";
    }
}
