package main

import (
	"os"
	"time"

	"github.com/joho/godotenv"
	"github.com/rs/zerolog/log"
	tb "gopkg.in/tucnak/telebot.v3"

	"github.com/devmattrick/emotesbot/handler"
)

func main() {
	if err := run(); err != nil {
		log.Fatal().Err(err).Send()
		os.Exit(1)
	}
}

func run() error {
	// Load .env file
	err := godotenv.Load()
	if err != nil && !os.IsNotExist(err) {
		return err
	}

	// Set up Telegram bot
	botToken := os.Getenv("TELEGRAM_BOT_TOKEN")
	b, err := tb.NewBot(tb.Settings{
		Token:  botToken,
		Poller: &tb.LongPoller{Timeout: 10 * time.Second},
	})
	if err != nil {
		return err
	}

	b.OnError = func(e error, c tb.Context) {
		log.Error().
			Err(e).
			Str("event", "error").
			Msg("Telegram Bot API error occurred")
	}

	b.Handle(tb.OnQuery, handler.InlineQuery)
	b.Handle(tb.OnInlineResult, handler.InlineQueryFeedback)
	b.Start()

	return nil
}
