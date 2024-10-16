package com.adas.redconnect

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class GenderFragment : Fragment() {

    private lateinit var genderGroup: RadioGroup
    private lateinit var buttonNext: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_gender, container, false)

        genderGroup = view.findViewById(R.id.gender)
        buttonNext = view.findViewById(R.id.button_next)

        buttonNext.setOnClickListener {
            // Get the selected RadioButton's ID
            val selectedGenderId = genderGroup.checkedRadioButtonId

            if (selectedGenderId != -1) {
                // A RadioButton is selected
                val selectedGender = view.findViewById<RadioButton>(selectedGenderId).text

                // Optional: Show a Toast with the selected gender
                Toast.makeText(requireContext(), "Selected: $selectedGender", Toast.LENGTH_SHORT).show()

                // Navigate to the HeightFragment
                findNavController().navigate(R.id.action_genderFragment_to_heightFragment)
            } else {
                // No selection, show a message
                Toast.makeText(requireContext(), "Please select a gender", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }
}
