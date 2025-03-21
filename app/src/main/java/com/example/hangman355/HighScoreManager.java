package com.example.hangman355;

import android.content.Context;
import android.content.SharedPreferences;

public class HighScoreManager {
    private SharedPreferences prefs;

    public HighScoreManager(Context context) {
        this.prefs = context.getSharedPreferences("HangmanPrefs", Context.MODE_PRIVATE);
    }

    public void saveHighScore(int score) {
        int highScore = getHighScore();
        if (score > highScore) {
            prefs.edit().putInt("HIGH_SCORE", score).apply();
        }
    }

    public int getHighScore() {
        return prefs.getInt("HIGH_SCORE", 0);
    }
}
