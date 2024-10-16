package com.adas.redconnect

import android.graphics.Color
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.core.content.ContextCompat

class BloodGroupFragment : Fragment() {

    private lateinit var bloodGroupGroup: RadioGroup
    private lateinit var rhFactorGroup: RadioGroup
    private lateinit var buttonNext: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_blood_group, container, false)

        bloodGroupGroup = view.findViewById(R.id.blood_group_radio_group)
        rhFactorGroup = view.findViewById(R.id.rh_factor_radio_group)
        buttonNext = view.findViewById(R.id.button_next)

        buttonNext.setOnClickListener {
            val selectedBloodGroupId = bloodGroupGroup.checkedRadioButtonId
            val selectedRhFactorId = rhFactorGroup.checkedRadioButtonId

            if (selectedBloodGroupId != -1 && selectedRhFactorId != -1) {
                val selectedBloodGroup = view.findViewById<RadioButton>(selectedBloodGroupId).text
                val selectedRhFactor = view.findViewById<RadioButton>(selectedRhFactorId).text

                // Use the selectedBloodGroup and selectedRhFactor as needed
                Toast.makeText(
                    requireContext(),
                    "Selected: $selectedBloodGroup $selectedRhFactor",
                    Toast.LENGTH_SHORT
                ).show()

                // Navigate to the next fragment or activity
                // Example: navigateToNextFragment()
            } else {
                Toast.makeText(
                    requireContext(),
                    "Please select both blood group and Rh factor",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

        return view
    }
}