package com.example.hangman355

import android.hardware.SensorManager
import android.os.Bundle
import android.os.Vibrator
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.hangman355.ui.theme.Hangman355Theme

class MainActivity : ComponentActivity() {
    private lateinit var gameLogic: GameLogic


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val wordTextView = findViewById<TextView>(R.id.wordTextView)
        val attemptsTextView = findViewById<TextView>(R.id.attemptsTextView)
        val letterInput = findViewById<EditText>(R.id.letterInput)
        val guessButton = findViewById<Button>(R.id.guessButton)
        val scoreTextView = findViewById<TextView>(R.id.scoreTextView)

        val vibrator = getSystemService(VIBRATOR_SERVICE) as Vibrator
        val sensorManager = getSystemService(SENSOR_SERVICE) as SensorManager


        gameLogic = GameLogic {
            wordTextView.text = gameLogic.getDisplayedWord()
            attemptsTextView.text = "Versuche übrig: ${gameLogic.attemptsLeft}"
            scoreTextView.text = "Punkte: ${gameLogic.score}"
        }



        guessButton.setOnClickListener {
            val letter = letterInput.text.toString().uppercase().firstOrNull()
            if (letter != null) {
                gameLogic.checkLetter(letter)
                letterInput.text.clear()
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Hangman355Theme {
        Greeting("Android")
    }
}