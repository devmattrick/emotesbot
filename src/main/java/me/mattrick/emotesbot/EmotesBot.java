package me.mattrick.emotesbot;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.Getter;
import me.mattrick.emotesbot.listener.InlineListener;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.util.Map;

public class EmotesBot {

    @Getter
    private InlineListener listener;

    public EmotesBot(String apiKey) {
        //Log in to Telegram API

        //Register listener and wait for updates
        listener = new InlineListener(apiKey, loadEmotes());
    }

    private Map<String, String> loadEmotes() {
        Gson gson = new Gson();
        InputStream inputStream = EmotesBot.class.getResourceAsStream("/emotes.json");
        if (inputStream == null) {
            System.out.println("No emotes found");
            System.exit(1);
            return null;
        }
        InputStreamReader reader = new InputStreamReader(inputStream);

        Type type = new TypeToken<Map<String, String>>() {
        }.getType();

        return gson.fromJson(reader, type);
    }

    public static void main(String... args) {
        String token = System.getenv("BOT_TOKEN");
        if (token == null || token.equals("")) {
            if (args.length >= 1) {
                token = args[0];
            }
            else {
                System.out.println("Auth token not found, shutting down...");
                System.exit(1);
            }
        }

        new EmotesBot(token);
    }
}
