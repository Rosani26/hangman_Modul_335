package com.example.hangman355;

import okhttp3.*;
import org.json.JSONArray;
import java.util.HashSet;
import java.util.Set;

public class GameLogic {
    private String wordToGuess = "";
    private Set<Character> guessedLetters = new HashSet<>();
    private int attemptsLeft = 10;
    private int score = 0;
    private OnUpdateListener onUpdate;

    public GameLogic(OnUpdateListener onUpdate) {
        this.onUpdate = onUpdate;
        fetchRandomWord();
    }

    public void checkLetter(char letter) {
        if (!guessedLetters.contains(letter)) {
            guessedLetters.add(letter);

            if (wordToGuess.indexOf(letter) >= 0) {
                score += 10;
            } else {
                attemptsLeft--;
                score -= 5;
            }

            onUpdate.onUpdate(getDisplayedWord());

            if (isGameWon() || isGameOver()) {
                resetGame();
            }
        }
    }

    public void resetGame() {
        guessedLetters.clear();
        attemptsLeft = 10;
        score = 0;
        fetchRandomWord();
    }

    private void fetchRandomWord() {
        ApiService.fetchWordFromApi(word -> {
            wordToGuess = word;
            onUpdate.onUpdate(getDisplayedWord());
        });
    }

    public String getDisplayedWord() {
        StringBuilder displayedWord = new StringBuilder();
        for (char c : wordToGuess.toCharArray()) {
            if (guessedLetters.contains(c)) {
                displayedWord.append(c);
            } else {
                displayedWord.append('_');
            }
            displayedWord.append(' ');
        }
        return displayedWord.toString().trim();
    }

    public boolean isGameWon() {
        for (char c : wordToGuess.toCharArray()) {
            if (!guessedLetters.contains(c)) {
                return false;
            }
        }
        return true;
    }

    public boolean isGameOver() {
        return attemptsLeft == 0;
    }

    public int getAttemptsLeft() {
        return attemptsLeft;
    }

    public int getScore() {
        return score;
    }

    public interface OnUpdateListener {
        void onUpdate(String displayedWord);
    }
}