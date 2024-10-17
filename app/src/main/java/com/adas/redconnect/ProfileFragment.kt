package com.adas.redconnect

import android.content.Intent
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Im
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.google.android.material.materialswitch.MaterialSwitch


class ProfileFragment : Fragment() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        val toggleAvailability: MaterialSwitch = view.findViewById(R.id.toggle_availability)
        val accountInfo: ImageView = view.findViewById(R.id.accInfo)
        val donorHistory: ImageView = view.findViewById(R.id.donationHistory)
        val manageAddress: ImageView = view.findViewById(R.id.manageAddress)
        val settings: ImageView = view.findViewById(R.id.settings)


        // Handle toggle availability switch
        toggleAvailability.setOnCheckedChangeListener { _, isChecked ->
            // Logic for availability toggle
            if (isChecked) {
                // User is available for donation
            } else {
                // User is unavailable for donation
            }
        }

        accountInfo.setOnClickListener {
            val accInfoFrag  = account_InfoFragment()

            requireActivity().supportFragmentManager.beginTransaction().replace(R.id.profileFragment,accInfoFrag).addToBackStack(null).commit()

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

        return view
    }

    companion object {

        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            ProfileFragment().apply {

            }
    }
}