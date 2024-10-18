package com.adas.redconnect.fragments

import android.content.Context.MODE_PRIVATE
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.adas.redconnect.ChoiceActivity
import com.adas.redconnect.R
import com.adas.redconnect.databinding.FragmentHospitalHomeBinding

class HospitalHomeFragment : Fragment() {
    private var hospitalHomeBinding : FragmentHospitalHomeBinding? = null
    private val binding get() = hospitalHomeBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        hospitalHomeBinding = FragmentHospitalHomeBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.logoutBtn.setOnClickListener {
            signOut()
        }

        binding.showAllCv.setOnClickListener {

        }

    }

    private fun signOut() {
        val sharedPreferences = activity?.getSharedPreferences("ChoicePref", MODE_PRIVATE)
        val editor = sharedPreferences?.edit()
        editor?.putBoolean("hospLoggedIn", false)?.apply()
        val intent = Intent(context, ChoiceActivity::class.java)
        startActivity(intent)
        activity?.finish()
    }


}