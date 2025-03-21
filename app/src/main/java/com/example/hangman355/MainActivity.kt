package com.example.hangman

import android.content.SharedPreferences
import android.hardware.SensorManager
import android.os.Bundle
import android.os.Vibrator
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.ComponentActivity
import com.example.hangman355.R
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONArray
import kotlin.concurrent.thread

class MainActivity : ComponentActivity() {
    private var wordToGuess = ""
    private var guessedLetters = mutableSetOf<Char>()
    private var attemptsLeft = 6
    private var score = 0
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPreferences = getSharedPreferences("HangmanPrefs", MODE_PRIVATE)
        val highScore = sharedPreferences.getInt("HIGH_SCORE", 0)

        val wordTextView = findViewById<TextView>(R.id.wordTextView)
        val attemptsTextView = findViewById<TextView>(R.id.attemptsTextView)
        val letterInput = findViewById<EditText>(R.id.letterInput)
        val guessButton = findViewById<Button>(R.id.guessButton)
        val scoreTextView = findViewById<TextView>(R.id.scoreTextView)
        val highScoreTextView = findViewById<TextView>(R.id.highScoreTextView)

        highScoreTextView.text = "Highscore: $highScore"

        fetchRandomWord { newWord ->
            wordToGuess = newWord
            updateWordDisplay(wordTextView)
        }

        guessButton.setOnClickListener {
            val letter = letterInput.text.toString().uppercase().firstOrNull()
            if (letter != null) {
                checkLetter(letter, wordTextView, attemptsTextView, scoreTextView, highScoreTextView)
                letterInput.text.clear()
            }
        }
    }

    private fun fetchRandomWord(onWordFetched: (String) -> Unit) {
        thread {
            val client = OkHttpClient()
            val request = Request.Builder().url("https://random-word-api.vercel.app/api?words=1").build()

            try {
                val response = client.newCall(request).execute()
                val responseBody = response.body?.string()

                if (!responseBody.isNullOrEmpty()) {
                    val jsonArray = JSONArray(responseBody)
                    val randomWord = jsonArray.getString(0).uppercase()

                    runOnUiThread {
                        onWordFetched(randomWord)
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
                runOnUiThread {
                    findViewById<TextView>(R.id.messageTextView).text = "Error fetching word"
                }
            }
        }
    }

    private fun checkLetter(letter: Char, wordTextView: TextView, attemptsTextView: TextView, scoreTextView: TextView, highScoreTextView: TextView) {
        if (!guessedLetters.contains(letter)) {
            guessedLetters.add(letter)

            if (wordToGuess.contains(letter)) {
                score += 10

                val animation = AnimationUtils.loadAnimation(this, R.anim.correct_guess)
                wordTextView.startAnimation(animation)

            } else {
                attemptsLeft--
                score -= 5
            }

            updateWordDisplay(wordTextView)
            attemptsTextView.text = "Attempts left: $attemptsLeft"
            scoreTextView.text = "Score: $score"

            if (isGameWon() || isGameOver()) {
                updateHighScore(highScoreTextView)
                resetGame(wordTextView, attemptsTextView, scoreTextView)
            }
        }


    }

    private fun resetGame(wordTextView: TextView, attemptsTextView: TextView, scoreTextView: TextView) {
        guessedLetters.clear()
        attemptsLeft = 6
        // score bleibt erhalten! -> KEIN score = 0

        fetchRandomWord { newWord ->
            wordToGuess = newWord
            updateWordDisplay(wordTextView)
            attemptsTextView.text = "Attempts left: $attemptsLeft"
            scoreTextView.text = "Score: $score" // Behalte den Score aus vorherigen Runden
        }
    }

    private fun updateWordDisplay(wordTextView: TextView) {
        val displayedWord = wordToGuess.map { if (it in guessedLetters) it else '_' }.joinToString(" ")
        wordTextView.text = displayedWord
    }

    private fun isGameWon(): Boolean {
        return wordToGuess.all { it in guessedLetters }
    }

    private fun isGameOver(): Boolean {
        return attemptsLeft == 0
    }

    private fun updateHighScore(highScoreTextView: TextView) {
        val highScore = sharedPreferences.getInt("HIGH_SCORE", 0)
        if (score > highScore) {
            sharedPreferences.edit().putInt("HIGH_SCORE", score).apply()
            highScoreTextView.text = "Highscore: $score"
        }
    }
}