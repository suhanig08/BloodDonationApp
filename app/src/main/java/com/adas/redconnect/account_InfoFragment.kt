package com.adas.redconnect

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import com.adas.redconnect.databinding.FragmentAccountInfoBinding
import com.adas.redconnect.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class account_InfoFragment : Fragment() {
    private lateinit var binding: FragmentAccountInfoBinding
    private lateinit var dbRef: DatabaseReference
    private lateinit var auth: FirebaseAuth


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentAccountInfoBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbRef = FirebaseDatabase.getInstance().getReference("donor")
        auth = FirebaseAuth.getInstance()

        dbRef.child(auth.currentUser!!.uid).child("name").get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                val bloodGroup = snapshot.value.toString()
                binding.tvName.text = bloodGroup
            } else {
                binding.tvName.text = "N/A" // Or some placeholder text
            }
        }.addOnFailureListener {
            binding.tvName.text = "Error" // In case of any errors
        }

        dbRef.child(auth.currentUser!!.uid).child("bloodgroup").get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                val bloodGroup = snapshot.value.toString()
                binding.tvBloodGrp.text = bloodGroup
            } else {
                binding.tvBloodGrp.text = "N/A" // Or some placeholder text
            }
        }.addOnFailureListener {
            binding.tvBloodGrp.text = "Error" // In case of any errors
        }

        dbRef.child(auth.currentUser!!.uid).child("gender").get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                val gender = snapshot.value.toString()
                binding.tvGender.text = gender
            } else {
                binding.tvGender.text = "N/A" // Or some placeholder text
            }
        }.addOnFailureListener {
            binding.tvGender.text = "Error" // In case of any errors
        }

        dbRef.child(auth.currentUser!!.uid).child("age").get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                val age = snapshot.value.toString()
                binding.tvAge.text = age
            } else {
                binding.tvAge.text = "N/A" // Or some placeholder text
            }
        }.addOnFailureListener {
            binding.tvAge.text = "Error" // In case of any errors
        }

        dbRef.child(auth.currentUser!!.uid).child("phone").get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                val num = snapshot.value.toString()
                binding.tvPhn.text = num
            } else {
                binding.tvPhn.text = "N/A" // Or some placeholder text
            }
        }.addOnFailureListener {
            binding.tvPhn.text = "Error" // In case of any errors
        }

        dbRef.child(auth.currentUser!!.uid).child("address").get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                val location = snapshot.value.toString()
                binding.tvLocation.text = location
            } else {
                binding.tvLocation.text = "N/A" // Or some placeholder text
            }
        }.addOnFailureListener {
            binding.tvLocation.text = "Error" // In case of any errors
        }

    }

}