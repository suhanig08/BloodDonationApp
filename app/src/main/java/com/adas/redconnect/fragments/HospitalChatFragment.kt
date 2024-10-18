package com.adas.redconnect.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import com.adas.redconnect.Appointment
import com.adas.redconnect.AppointmentsAdapter
import com.adas.redconnect.R
import com.adas.redconnect.adapters.UserAdapter
import com.adas.redconnect.databinding.FragmentHospitalChatBinding
import com.adas.redconnect.data.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HospitalChatFragment : Fragment() {
    private lateinit var userList: ArrayList<User>
    private lateinit var adapter: UserAdapter
    private lateinit var mAuth: FirebaseAuth
    private lateinit var database: FirebaseDatabase
    private lateinit var dbRef: DatabaseReference
    private lateinit var appointmentsAdapter: AppointmentsAdapter
    private lateinit var appointmentsList: MutableList<Appointment>
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
        database = FirebaseDatabase.getInstance()

        binding.appointmentsRv.layoutManager = LinearLayoutManager(requireContext())

        appointmentsList = mutableListOf()
        appointmentsAdapter = AppointmentsAdapter(appointmentsList, parentFragmentManager)
        binding.appointmentsRv.adapter = appointmentsAdapter

        loadMessages()

        var addAppt : EditText = view.findViewById(R.id.addAppt)

        addAppt.setOnClickListener {
            sendMessage("SED LYF :( ")
        }
    }

    private fun loadMessages() {
        val appointmentsRef = database.getReference("appointments")

        appointmentsRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                appointmentsList.clear()
                for (messageSnapshot in snapshot.children) {
                    val message = messageSnapshot.getValue(Appointment::class.java)
                    message?.let { appointmentsList.add(it) }
                }
                appointmentsAdapter.notifyDataSetChanged()
                //recyclerView.scrollToPosition(appointmentsList.size - 1)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle possible errors here
            }
        })
    }

    private fun sendMessage(messageText: String) {

        mAuth = FirebaseAuth.getInstance()
        val appointmentsRef = database.getReference("appointments")
        val appointmentId = appointmentsRef.push().key ?: return
        val appointment = Appointment(
            id = appointmentId,
            hospitalId = "",
            donorId = mAuth.currentUser?.uid ?: "x",
            hospitalName = "",
            msg = "HELLO WORLD :) ",
            donorBloodType = "xx",
            timestamp = System.currentTimeMillis().toString()
        )

        // Save message to Firebase
        appointmentsRef.child(appointmentId).setValue(appointment)
        loadMessages()
    }


}