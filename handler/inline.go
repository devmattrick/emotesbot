package handler

import (
	"github.com/rs/zerolog/log"
	tb "gopkg.in/tucnak/telebot.v3"

	"github.com/devmattrick/emotesbot/emotes"
)

func InlineQuery(ctx tb.Context) error {
	q := ctx.Query()

	log.Info().
		Str("event", "query").
		Int64("user_id", q.Sender.ID).
		Str("username", q.Sender.Username).
		Str("query", q.Text).
		Msg("Recieved inline query")

	results := tb.Results{}
	for _, emote := range emotes.Find(q.Text) {
		results = append(results, &tb.ArticleResult{
			ResultBase: tb.ResultBase{
				ID: emote.Value,
			},
			Title:       emote.Value,
			Description: emote.Name,
			Text:        emote.Value,
		})
	}

	return ctx.Answer(&tb.QueryResponse{
		Results: results,
	})
}

func InlineQueryFeedback(ctx tb.Context) error {
	result := ctx.InlineResult()

	log.Info().
		Str("event", "feedback").
		Int64("user_id", result.Sender.ID).
		Str("username", result.Sender.Username).
		Str("query", result.Query).
		Str("result", result.ResultID).
		Msg("Recieved inline query feedback")

	return nil
}
