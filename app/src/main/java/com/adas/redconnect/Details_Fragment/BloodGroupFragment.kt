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
import com.adas.redconnect.databinding.FragmentBloodGroupBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class BloodGroupFragment : Fragment() {

    private lateinit var binding: FragmentBloodGroupBinding

    private lateinit var dbRef: DatabaseReference
    private lateinit var auth: FirebaseAuth

    private lateinit var bloodGroupGroup: RadioGroup
    private lateinit var rhFactorGroup: RadioGroup
    private lateinit var buttonNext: Button

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentBloodGroupBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()

        dbRef= FirebaseDatabase.getInstance().getReference("donor")
        bloodGroupGroup = view.findViewById(R.id.blood_group_radio_group)
        rhFactorGroup = view.findViewById(R.id.rh_factor_radio_group)
        buttonNext = view.findViewById(R.id.button_next)

        buttonNext.setOnClickListener {
            val selectedBloodGroupId = bloodGroupGroup.checkedRadioButtonId
            val selectedRhFactorId = rhFactorGroup.checkedRadioButtonId

            if (selectedBloodGroupId != -1 && selectedRhFactorId != -1) {
                val selectedBloodGroup = view.findViewById<RadioButton>(selectedBloodGroupId).text
                val selectedRhFactor = view.findViewById<RadioButton>(selectedRhFactorId).text
                val bloodgroup=selectedBloodGroup.toString()+selectedRhFactor.toString()
                Log.e("UserDetailss",selectedBloodGroup.toString())
                Log.e("UserDetailss",selectedRhFactor.toString())

                val sharedPreferences=activity?.getSharedPreferences("DonorDet", MODE_PRIVATE)
                val name=sharedPreferences?.getString("name","")
                if(name!!.isNotEmpty()) {
                    dbRef.child(name).child("bloodgroup")
                        .setValue(bloodgroup)
                }

                // Toast to show selected blood group and Rh factor
                Toast.makeText(
                    requireContext(),
                    "Selected: $selectedBloodGroup $selectedRhFactor",
                    Toast.LENGTH_SHORT
                ).show()

                // Navigate to the next fragment (GenderFragment)
                findNavController().navigate(R.id.action_bloodGroupFragment_to_genderFragment)

            } else {
                // Show message if no selection
                Toast.makeText(
                    requireContext(),
                    "Please select both blood group and Rh factor",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    }
}
