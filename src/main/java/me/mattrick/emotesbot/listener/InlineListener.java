package me.mattrick.emotesbot.listener;

import me.mattrick.emotesbot.EmotesBot;
import pro.zackpollard.telegrambot.api.chat.inline.InlineQuery;
import pro.zackpollard.telegrambot.api.chat.inline.send.content.InputTextMessageContent;
import pro.zackpollard.telegrambot.api.chat.inline.send.results.InlineQueryResult;
import pro.zackpollard.telegrambot.api.chat.inline.send.results.InlineQueryResultArticle;
import pro.zackpollard.telegrambot.api.event.Listener;
import pro.zackpollard.telegrambot.api.event.chat.inline.InlineQueryReceivedEvent;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class InlineListener implements Listener {

    private EmotesBot bot;
    private Map<String, String> emotes;

    public InlineListener(EmotesBot bot, Map<String, String> emotes) {
        this.bot = bot;
        this.emotes = emotes;
        bot.getBot().getEventsManager().register(this);
    }

    @Override
    public void onInlineQueryReceived(InlineQueryReceivedEvent event) {
        InlineQuery query = event.getQuery();
        String search = query.getQuery();

        List<InlineQueryResult> results = new ArrayList<>();
        emotes.keySet().stream().filter(key -> key.toLowerCase().contains(search.toLowerCase())).forEach(key -> {
            results.add(
                    InlineQueryResultArticle.builder()
                            .title(emotes.get(key))
                            .description(key)
                            .inputMessageContent(
                                    InputTextMessageContent.builder()
                                            .messageText(emotes.get(key))
                                            .build()
                            )
                            .build());
        });

        query.answer(
                bot.getBot(),
                results.toArray(new InlineQueryResult[results.size()])
        );
    }

}
