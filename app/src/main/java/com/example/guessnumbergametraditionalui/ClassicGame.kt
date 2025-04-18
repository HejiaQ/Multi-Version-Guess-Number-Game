package com.example.guessnumbergametraditionalui

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import kotlin.random.Random

open class ClassicGame : AppCompatActivity() {
    protected lateinit var questionGuide: TextView
    protected lateinit var inputField: EditText
    private lateinit var submitButton: Button
    private lateinit var resetButton: Button
    protected lateinit var hintButton: Button
    private lateinit var hintText: TextView

    private val maxAttempt = 5
    private var attempt: Int = maxAttempt
    protected var correctAnswer: Int = Random.nextInt(1, 101)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.classic_game_activity)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.classic_main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        questionGuide = findViewById(R.id.numberHint)
        inputField = findViewById(R.id.userInput)
        submitButton = findViewById(R.id.submitButton)
        resetButton = findViewById(R.id.resetButton)
        hintButton = findViewById(R.id.hintButton)
        hintText = findViewById(R.id.hintText)

        submitButton.setOnClickListener { submitAnswer() }
        resetButton.setOnClickListener { reset() }
        hintButton.setOnClickListener { provideHint() }

    }

    private fun provideHint() {
        if(attempt != 0 && questionGuide.text.toString() != getString(R.string.success)) {
            val randomHint = Random.nextInt(1,5)
            when {
                randomHint == 1 ->  {
                    val isEven = isEven(correctAnswer)
                    if(isEven) {
                        hintText.text = getString(R.string.hint_even)
                    } else {
                        hintText.text = getString(R.string.hint_odd)
                    }
                }
                randomHint == 2 -> {
                    val isMultipleOfTen = isDivisibleByTen(correctAnswer)
                    if(isMultipleOfTen) {
                        hintText.text = getString(R.string.hint_multiple_of_ten)
                    } else {
                        hintText.text = getString(R.string.hint_not_multiple_of_ten)
                    }
                }
                randomHint == 3 -> {
                    val lastDigit = lastDigit(correctAnswer)
                    hintText.text = getString(R.string.last_digit, lastDigit)
                }
                else -> {
                    hintText.text = getString(R.string.empty_hint)
                }
            }
        }
    }

    open fun reset() {
        questionGuide.text = getString(R.string.instruction)
        correctAnswer = Random.nextInt(1, 101)
        attempt = maxAttempt
        hintText.text = ""
        inputField.text.clear()
    }

    open fun submitAnswer() {
        if(questionGuide.text.toString() != getString(R.string.success) && attempt == 1) {
            questionGuide.text = getString(R.string.fail, correctAnswer)
            return
        }

        if(questionGuide.text.toString() != getString(R.string.success)) {
            val input: Int? = inputField.text.toString().toIntOrNull()

            if(input != null) {
                attempt--

                when{
                    input == correctAnswer -> questionGuide.text = getString(R.string.success)
                    correctAnswer > input -> questionGuide.text = getString(R.string.instruction_greater)
                    else -> questionGuide.text = getString(R.string.instruction_less)
                }

                inputField.text.clear()

            } else {
                questionGuide.text = getString(R.string.invalid)
            }
        }
    }

}