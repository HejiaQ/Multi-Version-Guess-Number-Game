package com.example.guessnumbergametraditionalui

import android.app.AlertDialog
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import kotlin.random.Random

class HumanVsComputer: ClassicGame() {

    private lateinit var provideAnswerSection: LinearLayout
    private lateinit var computerAnswer: TextView
    private lateinit var submitButton2: Button
    private lateinit var userProvidedAnswer: EditText
    private var userSetAnswer: Int? = null
    private var computerGeneratedAnswer: Int? = null
    private var lowerBound: Int = 1
    private var upperBound: Int = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        provideAnswerSection = findViewById(R.id.submit_for_computer)
        provideAnswerSection.visibility = View.VISIBLE

        computerAnswer = findViewById(R.id.computer_answer)
        computerAnswer.visibility = View.VISIBLE

        submitButton2 = findViewById(R.id.submitButton2)

        userProvidedAnswer = findViewById(R.id.provide_answer)

        hintButton.visibility = View.GONE

        submitButton2.setOnClickListener { submitAnswerForComputer() }

    }

    private fun submitAnswerForComputer() {
        userSetAnswer = userProvidedAnswer.text.toString().toIntOrNull()

        if(userSetAnswer == null) {
            showCustomDialog("Note", getString(R.string.invalid))
            return
        }

        if(userSetAnswer!! > 100 || userSetAnswer!! < 1) {
            showCustomDialog("Note", getString(R.string.invalid_range))
            return
        }

        Log.d("Test", userSetAnswer.toString())
        submitButton2.visibility = View.GONE
        userProvidedAnswer.isEnabled = false
    }

    @Override
    override fun reset() {
        super.reset()
        userProvidedAnswer.text.clear()
        userProvidedAnswer.isEnabled = true
        submitButton2.visibility = View.VISIBLE
        computerAnswer.text = ""
        userSetAnswer = null
        computerGeneratedAnswer = null
    }

    @Override
    override fun submitAnswer() {
        if(submitButton2.visibility == View.VISIBLE) {
            showCustomDialog(
                "Note",
                "Please enter a answer for your opponent and click submit."
            )
        } else {
            val input: Int? = inputField.text.toString().toIntOrNull()
            computerGeneratedAnswer = Random.nextInt(lowerBound, upperBound)
            computerAnswer.text = getString(R.string.computer_answer, computerGeneratedAnswer)
            //get user input and determine whether it is right or wrong
            val userCorrectness = determineUserCorrectness(input)
            //get computer input and determine whether it is right or wrong
            val computerCorrectness = determineComputerCorrectness()
            //if they are both correct,
            //put a both success dialog and reset automatically
            if(userCorrectness && computerCorrectness) {
                showCustomDialog("Draw", getString(R.string.result_both_success))
                reset()
                return
            }
            //user is correct > success dialog, reset
            if(userCorrectness) {
                showCustomDialog("Success", getString(R.string.result_success))
                reset()
                return
            }
            //computer is correct > lose dialog, reset
            if(computerCorrectness) {
                showCustomDialog("Fail", getString(R.string.result_lose))
                reset()
                return
            }

            //update the question to hint
            updateQuestionHint(input)
            inputField.text.clear()
        }
    }

    private fun updateQuestionHint(input: Int?) {
        if(input == null) {
            questionGuide.text = getString(R.string.invalid)
        } else {
            when{
                correctAnswer > input -> questionGuide.text = getString(R.string.instruction_greater)
                else -> questionGuide.text = getString(R.string.instruction_less)
            }
        }
    }

    private fun determineComputerCorrectness(): Boolean {
        if(computerGeneratedAnswer!! > userSetAnswer!!) {
            upperBound = computerGeneratedAnswer!!
        }

        if(computerGeneratedAnswer!! < userSetAnswer!!) {
            lowerBound = computerGeneratedAnswer!! + 1
        }

        return computerGeneratedAnswer == userSetAnswer
    }

    private fun determineUserCorrectness(userInput: Int?): Boolean {
        if(userInput == null) {
            return false
        } else {
            return userInput == correctAnswer
        }
    }

    private fun showCustomDialog(title: String, text: String) {
        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setTitle(title)
        dialogBuilder.setMessage(text)

        dialogBuilder.setPositiveButton("Ok") { dialog, _ ->
            dialog.dismiss()
        }

        val dialog = dialogBuilder.create()
        dialog.show()
    }
}