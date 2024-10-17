package com.adas.redconnect

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.adas.redconnect.databinding.FragmentWeightBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class WeightFragment : Fragment(){

private lateinit var binding: FragmentWeightBinding

private lateinit var dbRef: DatabaseReference
private lateinit var auth: FirebaseAuth

    private lateinit var weightInput: EditText
    private lateinit var buttonNext: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentWeightBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        dbRef = FirebaseDatabase.getInstance().getReference("donor")

        weightInput = view.findViewById(R.id.weight_input)
        buttonNext = view.findViewById(R.id.button_next)

        buttonNext.setOnClickListener {
            val enteredWeight = weightInput.text.toString()

            if (enteredWeight.isNotEmpty()) {

                val sharedPreferences=activity?.getSharedPreferences("DonorDet", MODE_PRIVATE)
                val name=sharedPreferences?.getString("name","")
                if(name!!.isNotEmpty()) {
                    dbRef.child(name).child("weight")
                        .setValue(enteredWeight)
                }

                // Optional: Show a Toast with the entered weight
                Toast.makeText(requireContext(), "Weight: $enteredWeight kg", Toast.LENGTH_SHORT).show()

                // Navigate to the MainActivity
//                val intent = Intent(activity, MainActivity::class.java)
//                startActivity(intent)
//                activity?.finish()  // Optional: Close the fragment's parent activity if necessary

                findNavController().navigate(R.id.action_weightFragment_to_previousDonationFragment)
            } else {
                // No weight entered, show a message
                Toast.makeText(requireContext(), "Please enter your weight", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
