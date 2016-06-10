package me.mattrick.emotesbot;

import lombok.Getter;
import me.mattrick.emotesbot.listener.InlineListener;
import me.mattrick.emotesbot.updater.Updater;
import pro.zackpollard.telegrambot.api.TelegramBot;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
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

        File emotesFile = new File(getClass().getClassLoader().getResource("emotes.txt").getFile());
        try(BufferedReader br = new BufferedReader(new FileReader(emotesFile))) {
            for(String line; (line = br.readLine()) != null; ) {
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

    public static void main(String... args) {
        if (args.length >= 1) {
            new EmotesBot(args[0]);
        }
        else {
            System.out.println("Auth token not found, shutting down...");
        }
    }

}
