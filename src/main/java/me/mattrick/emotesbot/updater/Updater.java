package me.mattrick.emotesbot.updater;

import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import me.mattrick.emotesbot.EmotesBot;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.net.URL;

public class Updater implements Runnable {

    private EmotesBot bot;
    private int build = 0;

    public Updater(EmotesBot bot) {
        this.bot = bot;
    }

    @Override
    public void run() {
        File buildFile = new File("build");
        File jar = new File("emotesbot.new.jar");
        int newBuild = 0;

        try {
            build = Integer.parseInt(FileUtils.readFileToString(buildFile));
        } catch (IOException e) {
            e.printStackTrace();
        }

        while (true) {
            try {
                newBuild = Integer.parseInt(Unirest.get("http://ci.zackpollard.pro/job/TopKekBot/lastSuccessfulBuild/buildNumber").asString().getBody());
            } catch (UnirestException e) {
                e.printStackTrace();
            }
            if (newBuild > build) {
                System.out.println("New build found!");
                try {
                    FileUtils.writeStringToFile(buildFile, String.valueOf(newBuild));
                    FileUtils.copyURLToFile(new URL("http://ci.zackpollard.pro/job/TopKekBot/lastSuccessfulBuild/artifact/target/TopKekBot.jar"), jar);
                    System.out.println("New build downloaded - restarting!");
                } catch (IOException e) {
                    System.err.println("Updater failed!");
                    e.printStackTrace();
                    break;
                }
                System.exit(0);
            }
            try {
                Thread.sleep(2000L);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
