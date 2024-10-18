package com.adas.redconnect

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.adas.redconnect.databinding.FragmentGenderBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class GenderFragment : Fragment() {

    private lateinit var binding: FragmentGenderBinding

    private lateinit var dbRef: DatabaseReference
    private lateinit var auth: FirebaseAuth

    private lateinit var genderGroup: RadioGroup
    private lateinit var buttonNext: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentGenderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        dbRef = FirebaseDatabase.getInstance().getReference("donor")

        genderGroup = view.findViewById(R.id.gender)
        buttonNext = view.findViewById(R.id.button_next)

        buttonNext.setOnClickListener {
            // Get the selected RadioButton's ID
            val selectedGenderId = genderGroup.checkedRadioButtonId

            if (selectedGenderId != -1) {
                // A RadioButton is selected
                val selectedGender = view.findViewById<RadioButton>(selectedGenderId).text
                Log.e("UserDetailss",selectedGender.toString())

                    dbRef.child(auth.currentUser!!.uid).child("gender")
                        .setValue(selectedGender.toString())


                // Optional: Show a Toast with the selected gender
                Toast.makeText(requireContext(), "Selected: $selectedGender", Toast.LENGTH_SHORT).show()

                // Navigate to the HeightFragment
                findNavController().navigate(R.id.action_genderFragment_to_heightFragment)
            } else {
                // No selection, show a message
                Toast.makeText(requireContext(), "Please select a gender", Toast.LENGTH_SHORT).show()
            }
        }

    }
}
