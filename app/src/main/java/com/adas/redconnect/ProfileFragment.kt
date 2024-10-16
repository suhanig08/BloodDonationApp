package com.adas.redconnect

import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Im
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import com.adas.redconnect.databinding.FragmentHomeBinding
import com.adas.redconnect.databinding.FragmentProfileBinding
import com.google.android.material.materialswitch.MaterialSwitch
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase


class ProfileFragment : Fragment() {

    private lateinit var binding: FragmentProfileBinding
    private lateinit var dbRef: DatabaseReference


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

        val toggleAvailability: MaterialSwitch = view.findViewById(R.id.toggle_availability)
        val accountInfo: ImageView = view.findViewById(R.id.accInfo)
        val donorHistory: ImageView = view.findViewById(R.id.donationHistory)
        val manageAddress: ImageView = view.findViewById(R.id.manageAddress)
        val settings: ImageView = view.findViewById(R.id.settings)


        // Handle toggle availability switch
        toggleAvailability.setOnCheckedChangeListener { _, isChecked ->
            // Logic for availability toggle
            if (isChecked) {

                val sharedPreferences=activity?.getSharedPreferences("DonorDet", MODE_PRIVATE)
                val name=sharedPreferences?.getString("name","")
                if(name!!.isNotEmpty()) {
                    dbRef.child(name).child("availaibility")
                        .setValue(true)
                }
                // User is available for donation
            } else {
                val sharedPreferences=activity?.getSharedPreferences("DonorDet", MODE_PRIVATE)
                val name=sharedPreferences?.getString("name","")
                if(name!!.isNotEmpty()) {
                    dbRef.child(name).child("availaibility")
                        .setValue(false)
                }
                // User is unavailable for donation
            }
        }

        accountInfo.setOnClickListener {
            val intent  = Intent(context, account_InfoFragment::class.java)
            startActivity(intent)
        }

    }

}