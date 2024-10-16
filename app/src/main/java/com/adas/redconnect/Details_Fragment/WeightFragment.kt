package com.adas.redconnect

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment

class WeightFragment : Fragment() {

    private lateinit var weightInput: EditText
    private lateinit var buttonNext: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_weight, container, false)

        weightInput = view.findViewById(R.id.weight_input)
        buttonNext = view.findViewById(R.id.button_next)

        buttonNext.setOnClickListener {
            val enteredWeight = weightInput.text.toString()

            if (enteredWeight.isNotEmpty()) {
                // Optional: Show a Toast with the entered weight
                Toast.makeText(requireContext(), "Weight: $enteredWeight kg", Toast.LENGTH_SHORT).show()

                // Navigate to the MainActivity
                val intent = Intent(activity, MainActivity::class.java)
                startActivity(intent)
                activity?.finish()  // Optional: Close the fragment's parent activity if necessary
            } else {
                // No weight entered, show a message
                Toast.makeText(requireContext(), "Please enter your weight", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }
}
