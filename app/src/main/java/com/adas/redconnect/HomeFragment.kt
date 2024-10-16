package com.adas.redconnect

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import com.adas.redconnect.databinding.FragmentHeightBinding
import com.adas.redconnect.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.values

class HomeFragment : Fragment() {

    private lateinit var dbRef:DatabaseReference

    private lateinit var binding: FragmentHomeBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbRef=FirebaseDatabase.getInstance().getReference("donor")

        val sharedPreferences=activity?.getSharedPreferences("DonorDet", MODE_PRIVATE)
        val name=sharedPreferences?.getString("name","")

        binding.userName.setText("Hi, ${name}")

        dbRef.child(name.toString()).child("bloodgroup").get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                val bloodGroup = snapshot.value.toString()
                binding.userBloodGrp.text = bloodGroup
            } else {
                binding.userBloodGrp.text = "N/A" // Or some placeholder text
            }
        }.addOnFailureListener {
            binding.userBloodGrp.text = "Error" // In case of any errors
        }

    }
}