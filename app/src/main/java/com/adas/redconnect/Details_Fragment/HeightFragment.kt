package com.adas.redconnect.Details_Fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.adas.redconnect.R
import com.adas.redconnect.databinding.FragmentGenderBinding
import com.adas.redconnect.databinding.FragmentHeightBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference

class HeightFragment : Fragment() {

    private lateinit var binding: FragmentHeightBinding

    private lateinit var dbRef: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHeightBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        dbRef.child(auth.currentUser!!.uid).child("height").setValue(binding.heightUser.text.toString())
    }

}