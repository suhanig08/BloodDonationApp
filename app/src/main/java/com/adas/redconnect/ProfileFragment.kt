package com.adas.redconnect

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import androidx.fragment.app.Fragment
import com.adas.redconnect.databinding.FragmentProfileBinding
import com.bumptech.glide.Glide
import com.google.android.material.materialswitch.MaterialSwitch
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var dbRef: DatabaseReference
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbRef = FirebaseDatabase.getInstance().getReference("donor")
        auth = FirebaseAuth.getInstance()

        // Load profile image URL and display it
        dbRef.child(auth.currentUser!!.uid).child("profileUrl").get()
            .addOnSuccessListener { snapshot ->
                val url = snapshot.value as? String
                if (!url.isNullOrEmpty()) {
                    Glide.with(this)
                        .load(url)
                        .placeholder(R.drawable.img)
                        .error(R.drawable.img)
                        .into(binding.profileImage)
                } else {
                    Glide.with(this)
                        .load(R.drawable.img)
                        .placeholder(R.drawable.img)
                        .error(R.drawable.img)
                        .into(binding.profileImage)
                }
            }
            .addOnFailureListener {
                Glide.with(this)
                    .load(R.drawable.img)
                    .placeholder(R.drawable.img)
                    .error(R.drawable.img)
                    .into(binding.profileImage)
            }


        // Load other profile details
        loadProfileData()

        // Logout button functionality
        if(activity != null){
            binding.logoutBtn.setOnClickListener {
                signOut()
            }
        }


        // Handle toggle availability switch
        val toggleAvailability: MaterialSwitch = view.findViewById(R.id.toggle_availability)
        toggleAvailability.setOnCheckedChangeListener { _, isChecked ->
            dbRef.child(auth.currentUser!!.uid).child("availability").setValue(isChecked)
        }

        // Navigate to account info fragment
        binding.accInfo.setOnClickListener {
            val accInfoFrag = account_InfoFragment()
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.profileFragment, accInfoFrag).addToBackStack(null).commit()
        }
    }

    private fun loadProfileData() {
        // Load name
        dbRef.child(auth.currentUser!!.uid).child("name").get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                binding.profileName.text = snapshot.value.toString()
            } else {
                binding.profileName.text = "N/A" // Placeholder text
            }
        }.addOnFailureListener {
            binding.profileName.text = "Error" // In case of error
        }

        // Load blood group
        dbRef.child(auth.currentUser!!.uid).child("bloodGroup").get()
            .addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    binding.tvBloodGrp.text = snapshot.value.toString()
                } else {
                    binding.tvBloodGrp.text = "N/A"
                }
            }.addOnFailureListener {
                binding.tvBloodGrp.text = "Error"
            }

        // Load last donation date
        dbRef.child(auth.currentUser!!.uid).child("dateDonated").get()
            .addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    binding.lastDonation.text = snapshot.value.toString()
                } else {
                    binding.lastDonation.text = "N/A"
                }
            }.addOnFailureListener {
                binding.lastDonation.text = "Error"
            }
    }

    private fun signOut() {
        val sharedPreferences = activity?.getSharedPreferences("ChoicePref", MODE_PRIVATE)
        val editor = sharedPreferences?.edit()
        editor?.putBoolean("isLoggedIn", false)?.apply()
        val intent = Intent(context, ChoiceActivity::class.java)
        startActivity(intent)

        FirebaseAuth.getInstance().signOut()

        activity?.finish()


    }
}
