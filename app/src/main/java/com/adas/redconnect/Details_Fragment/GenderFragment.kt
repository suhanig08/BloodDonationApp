package com.adas.redconnect.Details_Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import com.adas.redconnect.R
import com.adas.redconnect.databinding.FragmentGenderBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class GenderFragment : Fragment() {

    private lateinit var binding: FragmentGenderBinding

    private lateinit var dbRef: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentGenderBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        auth = FirebaseAuth.getInstance()
        dbRef = FirebaseDatabase.getInstance().getReference("donor")

        binding.gender.setOnCheckedChangeListener { group, checkedId ->
            // Get the selected RadioButton
            val selectedRadioButton = view.findViewById<RadioButton>(checkedId)
            // Set the selectedGender variable to the text of the selected RadioButton
            val selectedGender = selectedRadioButton.text.toString()
            dbRef.child(auth.currentUser!!.uid).child("gender").setValue(selectedGender)
        }


    }

}