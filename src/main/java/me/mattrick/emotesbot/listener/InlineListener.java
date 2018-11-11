package me.mattrick.emotesbot.listener;

import com.jtelegram.api.TelegramBot;
import com.jtelegram.api.TelegramBotRegistry;
import com.jtelegram.api.events.inline.InlineQueryEvent;
import com.jtelegram.api.ex.TelegramException;
import com.jtelegram.api.inline.InlineQuery;
import com.jtelegram.api.inline.input.InputTextMessageContent;
import com.jtelegram.api.inline.result.InlineResultArticle;
import com.jtelegram.api.inline.result.framework.InlineResult;
import com.jtelegram.api.requests.inline.AnswerInlineQuery;
import com.jtelegram.api.update.PollingUpdateProvider;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.function.Consumer;

public class InlineListener {

    private Map<String, String> emotes;

    public InlineListener(String apiKey, Map<String, String> emotes) {
        TelegramBotRegistry registry = TelegramBotRegistry.builder().eventThreadCount(4).updateProvider(new PollingUpdateProvider()).build();
        registry.registerBot(apiKey, this::handleRegisteredBot);

        this.emotes = emotes;
    }

    private void handleRegisteredBot(TelegramBot bot, TelegramException error) {
        if (error != null) {
            System.out.println("We failed to login.");
            System.exit(1);
            return;
        }

        System.out.println("Bot active: " + bot.getBotInfo());
        bot.getEventRegistry().registerEvent(InlineQueryEvent.class, this::onInlineQueryReceived);

    }

    private void onInlineQueryReceived(InlineQueryEvent event) {
        System.out.println("Called");
        TelegramBot bot = event.getBot();

        InlineQuery query = event.getQuery();
        String search = query.getQuery();

        List<InlineResult> results = new ArrayList<>();
        emotes.keySet().stream().filter(key -> key.toLowerCase().contains(search.toLowerCase())).forEach(key -> {
            results.add(
                    InlineResultArticle.builder()
                            .id(UUID.randomUUID().toString())
                            .title(emotes.get(key))
                            .description(key)
                            .inputMessageContent(
                                    InputTextMessageContent.builder()
                                            .messageText(emotes.get(key))
                                            .build()
                            )
                            .build());
        });

        System.out.println(results.toString());
        bot.perform(AnswerInlineQuery.builder().queryId(query.getId()).addAllResults(results).errorHandler(System.out::println).build());
    }

}
