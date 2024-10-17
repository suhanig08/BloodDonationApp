package com.adas.redconnect.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.adas.redconnect.adapters.UserAdapter
import com.adas.redconnect.databinding.FragmentHospitalChatBinding
import com.adas.redconnect.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class HospitalChatFragment : Fragment() {
    private lateinit var userList: ArrayList<User>
    private lateinit var adapter: UserAdapter
    private lateinit var mAuth: FirebaseAuth
    private lateinit var dbRef: DatabaseReference
    private var hospitalChatBinding: FragmentHospitalChatBinding? = null
    private val binding get() = hospitalChatBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        hospitalChatBinding = FragmentHospitalChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userList = ArrayList()
        adapter = UserAdapter(requireContext(), userList)
        mAuth = FirebaseAuth.getInstance()
        dbRef = FirebaseDatabase.getInstance().getReference()

        binding.usersRV.layoutManager = LinearLayoutManager(requireContext())
        binding.usersRV.adapter = adapter


    }
}