package com.adas.redconnect

import android.content.Context.MODE_PRIVATE
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.adas.redconnect.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class HomeFragment : Fragment() {


    private lateinit var database: FirebaseDatabase
    private lateinit var dbRef: DatabaseReference
    private lateinit var auth: FirebaseAuth
    //private lateinit var recyclerView: RecyclerView
    private lateinit var appointmentsAdapter: AppointmentsAdapter
    private lateinit var appointmentsList: MutableList<Appointment>
    private var homeBinding : FragmentHomeBinding? = null
    private val binding get() = homeBinding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {

        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {

        homeBinding = FragmentHomeBinding.inflate(inflater, container, false)

        database = FirebaseDatabase.getInstance()
        auth = FirebaseAuth.getInstance()

//        binding.appointmentsRv.layoutManager = LinearLayoutManager(requireContext())
//
//        appointmentsList = mutableListOf()
//        appointmentsAdapter = AppointmentsAdapter(appointmentsList)
//        binding.appointmentsRv.adapter = appointmentsAdapter
//
//        //addAppt = view.findViewById(R.id.addAppointment)
//
//        loadMessages()
//
////        addAppt.setOnClickListener {
////            sendMessage("Hello")
////        }

        return binding.root
    }
   
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbRef=FirebaseDatabase.getInstance().getReference("donor")
        auth = FirebaseAuth.getInstance()


//        dbRef.child(auth.currentUser!!.uid).child("bloodgroup").get().addOnSuccessListener { snapshot ->
//            if (snapshot.exists()) {
//                val bloodGroup = snapshot.value.toString()
//                binding.userBloodGrp.text = bloodGroup
//            } else {
//                binding.userBloodGrp.text = "N/A" // Or some placeholder text
//              }
//        }.addOnFailureListener {
//            binding.userBloodGrp.text = "Error" // In case of any errors
//        }
//        dbRef.child(auth.currentUser!!.uid).child("name").get().addOnSuccessListener { snapshot ->
//            if (snapshot.exists()) {
//                val name= snapshot.value.toString()
//                binding.userName.text = "Hi, $name!"
//            } else {
//                binding.userName.text = "N/A" // Or some placeholder text
//            }
//        }.addOnFailureListener {
//            binding.userName.text = "Error" // In case of any errors
//        }

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
                //recyclerView.scrollToPosition(appointmentsList.size - 1)
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
            id = appointmentId,
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
            
}