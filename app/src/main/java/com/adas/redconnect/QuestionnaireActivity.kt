package com.adas.redconnect

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import android.widget.ViewFlipper
import androidx.appcompat.app.AppCompatActivity
import com.adas.redconnect.databinding.ActivityQuestionnaireBinding

class QuestionnaireActivity : AppCompatActivity() {

    private lateinit var binding: ActivityQuestionnaireBinding

    private lateinit var questionFlipper: ViewFlipper
    private lateinit var buttonNext1: Button
    private lateinit var buttonNext2: Button
    private lateinit var buttonNext3: Button
    private lateinit var buttonNext4: Button
    private lateinit var backBtn1: ImageView
    private lateinit var backBtn2: ImageView
    private lateinit var backBtn3: ImageView
    private lateinit var backBtn4: ImageView
    private lateinit var radioGroup1: RadioGroup
    private lateinit var radioGroup2: RadioGroup
    private lateinit var radioGroup3: RadioGroup
    private lateinit var radioGroup4: RadioGroup

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityQuestionnaireBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Initialize ViewFlipper and buttons
        questionFlipper = binding.questionFlipper
        buttonNext1 = binding.buttonNext1
        buttonNext2 = binding.buttonNext2
        buttonNext3 = binding.buttonNext3
        buttonNext4 = binding.buttonNext4
        backBtn1 = binding.backBtn1
        backBtn2 = binding.backBtn2
        backBtn3 = binding.backBtn3
        backBtn4 = binding.backBtn4

        // Initialize RadioGroups for each question
        radioGroup1 = binding.radioGroup1
        radioGroup2 = binding.radioGroup2
        radioGroup3 = binding.radioGroup3
        radioGroup4 = binding.radioGroup4

        // Set up Next button click listeners for each question
        buttonNext1.setOnClickListener {

            val selectedOptionId = radioGroup1.checkedRadioButtonId
            if (selectedOptionId != -1) {
                showNextQuestion()
            } else {
                Toast.makeText(this, "Please select one", Toast.LENGTH_SHORT).show() // If any question is not answered
            }
        }
        buttonNext2.setOnClickListener {

            val selectedOptionId = radioGroup2.checkedRadioButtonId
            if (selectedOptionId != -1) {
                showNextQuestion()
            } else {
                Toast.makeText(this, "Please select one", Toast.LENGTH_SHORT).show() // If any question is not answered
            }
        }
        buttonNext3.setOnClickListener {

            val selectedOptionId = radioGroup3.checkedRadioButtonId
            if (selectedOptionId != -1) {
                showNextQuestion()
            } else {
                Toast.makeText(this, "Please select one", Toast.LENGTH_SHORT).show() // If any question is not answered
            }
        }
        buttonNext4.setOnClickListener {

            val selectedOptionId = radioGroup4.checkedRadioButtonId
            if (selectedOptionId != -1) {
                checkEligibility()
            } else {
                Toast.makeText(this, "Please select one", Toast.LENGTH_SHORT).show() // If any question is not answered
            }
        }

        // Set up Previous button (back button) to show the previous question
        backBtn1.setOnClickListener { showPreviousQuestion() }
        backBtn2.setOnClickListener { showPreviousQuestion() }
        backBtn3.setOnClickListener { showPreviousQuestion() }
        backBtn4.setOnClickListener { showPreviousQuestion() }
    }

    // Show the next question by moving to the next view in ViewFlipper
    private fun showNextQuestion() {
        if (questionFlipper.displayedChild < questionFlipper.childCount - 1) {
            questionFlipper.showNext()
        }
    }

    // Show the previous question by moving to the previous view in ViewFlipper
    private fun showPreviousQuestion() {
        if (questionFlipper.displayedChild > 0) {
            questionFlipper.showPrevious()
        }
    }

    // Check if the user is eligible to donate based on their answers
    private fun checkEligibility() {
        // Check if any selected answer is "true" in each RadioGroup
        val isEligible = listOf(radioGroup1, radioGroup2, radioGroup3, radioGroup4).all { group ->
            val selectedOptionId = group.checkedRadioButtonId
            if (selectedOptionId != -1) {
                val selectedText = findViewById<RadioButton>(selectedOptionId).text.toString()
                selectedText.equals("false", ignoreCase = true)
            } else {
                false // If any question is not answered, consider it as "not eligible"
            }
        }

        if (isEligible) {
            // User is eligible, you can add logic to proceed to the donation screen if necessary
            finish()
        } else {
            // User is not eligible, navigate to Not Eligible Activity
            startActivity(Intent(this, NotEligibleActivity::class.java))
        }
    }
}
