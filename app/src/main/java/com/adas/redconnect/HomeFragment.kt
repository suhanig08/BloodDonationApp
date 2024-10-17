package com.adas.redconnect

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomeFragment : Fragment() {

    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth
    private lateinit var recyclerView: RecyclerView
    private lateinit var addAppt : EditText
    private lateinit var appointmentsAdapter: AppointmentsAdapter
    private lateinit var appointmentsList: MutableList<Appointment>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        database = FirebaseDatabase.getInstance()
        auth = FirebaseAuth.getInstance()

        recyclerView = view.findViewById(R.id.appointmentsRv)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        appointmentsList = mutableListOf()
        appointmentsAdapter = AppointmentsAdapter(appointmentsList)
        recyclerView.adapter = appointmentsAdapter

        //addAppt = view.findViewById(R.id.addAppointment)

        loadMessages()

//        addAppt.setOnClickListener {
//            sendMessage("Hello")
//        }

        return view
    }

    // Function to load appointments from Firebase
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
                recyclerView.scrollToPosition(appointmentsList.size - 1)
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle possible errors here
            }
        })
    }

    private fun sendMessage(messageText: String) {

        auth = FirebaseAuth.getInstance()
        val appointmentsRef = database.getReference("appointments")
        val appointmentId = appointmentsRef.push().key ?: return
        val appointment = Appointment(
            hospitalId = "",
            donorId = auth.currentUser?.uid ?: "x",
            hospitalName = "",
            msg = "HELLO WORLD :) ",
            donorBloodType = "xx",
            timestamp = System.currentTimeMillis().toString()
        )

        // Save message to Firebase
        appointmentsRef.child(appointmentId).setValue(appointment)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }
}