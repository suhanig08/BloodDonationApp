package com.adas.redconnect

import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Im
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import androidx.navigation.fragment.findNavController
import com.adas.redconnect.databinding.FragmentHomeBinding
import com.adas.redconnect.databinding.FragmentProfileBinding
import com.google.android.material.materialswitch.MaterialSwitch
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var dbRef: DatabaseReference
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbRef = FirebaseDatabase.getInstance().getReference("donor")
        auth = FirebaseAuth.getInstance()

        binding.logoutBtn.setOnClickListener {
            signOut()
        }

        dbRef.child(auth.currentUser!!.uid).child("name").get().addOnSuccessListener { snapshot ->
              if (snapshot.exists()) {
                val bloodGroup = snapshot.value.toString()
                binding.profileName.text = bloodGroup
            } else {
                binding.profileName.text = "N/A" // Or some placeholder text
            }
        }.addOnFailureListener {
            binding.profileName.text = "Error" // In case of any errors
        }

        dbRef.child(auth.currentUser!!.uid).child("bloodgroup").get()
            .addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    val bloodGroup = snapshot.value.toString()
                    binding.tvBloodGrp.text = bloodGroup
                } else {
                    binding.tvBloodGrp.text = "N/A" // Or some placeholder text
                }
            }.addOnFailureListener {
            binding.tvBloodGrp.text = "Error" // In case of any errors
        }

        dbRef.child(auth.currentUser!!.uid).child("dateDonated").get()
            .addOnSuccessListener { snapshot ->
                if (snapshot.exists()) {
                    val date = snapshot.value.toString()
                    binding.lastDonation.text = date
                } else {
                    binding.lastDonation.text = "N/A" // Or some placeholder text
                }
            }.addOnFailureListener {
            binding.lastDonation.text = "Error" // In case of any errors
        }

        val toggleAvailability: MaterialSwitch = view.findViewById(R.id.toggle_availability)
        val accountInfo: LinearLayout = view.findViewById(R.id.accInfo)
        val donorHistory: ImageView = view.findViewById(R.id.donationHistory)
        val manageAddress: ImageView = view.findViewById(R.id.manageAddress)
        val settings: ImageView = view.findViewById(R.id.settings)


        // Handle toggle availability switch
        toggleAvailability.setOnCheckedChangeListener { _, isChecked ->
            // Logic for availability toggle
            if (isChecked) {


                dbRef.child(auth.currentUser!!.uid).child("availability")
                    .setValue(true)
                // User is available for donation
            } else {
                dbRef.child(auth.currentUser!!.uid).child("availability")
                    .setValue(false)
            }
            // User is unavailable for donation
        }


            accountInfo.setOnClickListener {
                val accInfoFrag = account_InfoFragment()
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.profileFragment, accInfoFrag).addToBackStack(null).commit()
            }

    }
        
//        val hospitalId = "test"
//        val appointmentId = "test"
//
//        settings.setOnClickListener {
//            // Assuming you have the hospitalId and appointmentId
//            val chatFragment = ChatFragment().apply {
//                arguments = Bundle().apply {
//                    putString("hospitalId", hospitalId)
//                    putString("appointmentId", appointmentId)
//                }
//            }
//
//            // Replace the current fragment with ChatFragment
//            requireActivity().supportFragmentManager.beginTransaction()
//                .replace(R.id.profileFragment, chatFragment)
//                .addToBackStack(null)
//                .commit()
//        }

    private fun signOut() {
        val sharedPreferences = activity?.getSharedPreferences("ChoicePref", MODE_PRIVATE)
        val editor = sharedPreferences?.edit()
        editor?.putBoolean("isLoggedIn",false)
            ?.apply()
        val i=Intent(context,ChoiceActivity::class.java)
        startActivity(i)
        activity?.finish()
    }

}