package com.adas.redconnect

import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.adas.redconnect.databinding.FragmentHeightBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class HeightFragment : Fragment() {

    private lateinit var binding: FragmentHeightBinding

    private lateinit var dbRef: DatabaseReference
    private lateinit var auth: FirebaseAuth

    private lateinit var heightInput: EditText
    private lateinit var buttonNext: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHeightBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        dbRef = FirebaseDatabase.getInstance().getReference("donor")

        heightInput = view.findViewById(R.id.height_input)  // Assuming EditText ID is 'height_input'
        buttonNext = view.findViewById(R.id.button_next)

        buttonNext.setOnClickListener {
            val heightText = heightInput.text.toString()

            if (!TextUtils.isEmpty(heightText)) {
                // Valid input, proceed to the next fragment (WeightFragment)

                dbRef.child(auth.currentUser!!.uid).child("height")
                        .setValue(heightText)

                findNavController().navigate(R.id.action_heightFragment_to_weightFragment)
            } else {
                // Show a message if no input is provided
                Toast.makeText(requireContext(), "Please enter your height", Toast.LENGTH_SHORT).show()
            }
        }

    }
}
