package com.adas.redconnect

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController

class HeightFragment : Fragment() {

    private lateinit var heightInput: EditText
    private lateinit var buttonNext: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_height, container, false)

        heightInput = view.findViewById(R.id.height_input)  // Assuming EditText ID is 'height_input'
        buttonNext = view.findViewById(R.id.button_next)

        buttonNext.setOnClickListener {
            val heightText = heightInput.text.toString()

            if (!TextUtils.isEmpty(heightText)) {
                // Valid input, proceed to the next fragment (WeightFragment)
                findNavController().navigate(R.id.action_heightFragment_to_weightFragment)
            } else {
                // Show a message if no input is provided
                Toast.makeText(requireContext(), "Please enter your height", Toast.LENGTH_SHORT).show()
            }
        }

        return view
    }
}
