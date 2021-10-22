package emotes

import (
	_ "embed"
	"encoding/json"
	"strings"

	"github.com/sahilm/fuzzy"
)

type Emote struct {
	Name     string
	Value    string
	Keywords []string
}

//go:embed emotes.json
var emotesFile []byte

var Emotes []Emote
var joinedKeywords []string

func init() {
	json.Unmarshal(emotesFile, &Emotes)

	// Join and cache the keywords for each emote to make fuzzy searching easier
	joinedKeywords = make([]string, len(Emotes))
	for i, emote := range Emotes {
		joinedKeywords[i] = strings.Join(emote.Keywords, " ")
	}
}

func Find(query string) []Emote {
	matches := fuzzy.Find(query, joinedKeywords)

	result := []Emote{}
	for _, match := range matches {
		result = append(result, Emotes[match.Index])
	}

	return result
}
