package com.adas.redconnect.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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

        binding.showAllCv.setOnClickListener {

        }

    }


}