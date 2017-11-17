package me.mattrick.emotesbot;

import lombok.Getter;
import me.mattrick.emotesbot.listener.InlineListener;
import me.mattrick.emotesbot.updater.Updater;
import pro.zackpollard.telegrambot.api.TelegramBot;

import java.io.*;
import java.util.HashMap;
import java.util.Map;

public class EmotesBot {

    @Getter
    private final TelegramBot bot;

    @Getter
    private InlineListener listener;

    @Getter
    private Updater updater;

    public EmotesBot(String apiKey) {
        //Log in to Telegram API
        bot = TelegramBot.login(apiKey);

        //Register listener and wait for updates
        listener = new InlineListener(this, loadEmotes());
        bot.startUpdates(false);

        //Begin auto updater
        /*
        updater = new Updater(this);
        updater.run();
        */
    }

    private Map<String, String> loadEmotes() {
        Map<String, String> emotes = new HashMap<>();

        InputStream inputStream = EmotesBot.class.getResourceAsStream("/emotes.txt");
        if (inputStream != null) {
            InputStreamReader streamReader = new InputStreamReader(inputStream);
            try (BufferedReader br = new BufferedReader(streamReader)) {
                for (String line; (line = br.readLine()) != null; ) {
                    String key = line.substring(0, line.indexOf(":"));
                    String value = line.substring(line.indexOf(":") + 1);
                    System.out.println("Found emoticon: " + key + " : " + value);
                    emotes.put(key, value);
                }
            } catch (IOException e) {
                e.printStackTrace();
            }

            return emotes;
        }

        return null;
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
