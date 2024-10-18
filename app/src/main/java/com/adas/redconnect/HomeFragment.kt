package com.adas.redconnect

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.adas.redconnect.databinding.FragmentHomeBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*

class HomeFragment : Fragment() {

    private lateinit var database: FirebaseDatabase
    private lateinit var auth: FirebaseAuth
    private var homeBinding: FragmentHomeBinding? = null
    private val binding get() = homeBinding!!

    // Declare your adapter and appointment list
    private lateinit var adapter: AppointmentAdapter
    private val appointmentList = mutableListOf<Appointment>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeBinding = FragmentHomeBinding.inflate(inflater, container, false)

        // Initialize Firebase
        database = FirebaseDatabase.getInstance()
        auth = FirebaseAuth.getInstance()


        // Set up RecyclerView
        binding.appointmentsRv.layoutManager = LinearLayoutManager(requireContext())
        adapter = AppointmentAdapter(appointmentList) { appointment ->
            // Handle the click event for each appointment (chat button)
            //openChatFragment(appointment.id)
        }
        binding.appointmentsRv.adapter = adapter

        // Load the appointments
        loadAppointments()

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

        binding.donateBlood.setOnClickListener {
            val fragment = DonateFragment()
            val transaction = requireActivity().supportFragmentManager.beginTransaction()
            transaction.replace(R.id.frameLayout, fragment) // or add()
            transaction.addToBackStack(null) // if you want to add it to the back stack
            transaction.commit()

        }


        dbRef.child(auth.currentUser!!.uid).child("bloodgroup").get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                val bloodGroup = snapshot.value.toString()
                binding.userBloodGrp.text = bloodGroup
            } else {
                binding.userBloodGrp.text = "N/A" // Or some placeholder text
              }
        }.addOnFailureListener {
            binding.userBloodGrp.text = "Error" // In case of any errors
        }
        dbRef.child(auth.currentUser!!.uid).child("name").get().addOnSuccessListener { snapshot ->
            if (snapshot.exists()) {
                val name= snapshot.value.toString()
                binding.userName.text = "Hi, $name!"
            } else {
                binding.userName.text = "N/A" // Or some placeholder text
            }
        }.addOnFailureListener {
            binding.userName.text = "Error" // In case of any errors
        }


        return binding.root
    }

    private fun loadAppointments() {
        val appointmentsRef = database.getReference("appointments")

        appointmentsRef.orderByChild("donorId").equalTo(auth.currentUser?.uid)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    appointmentList.clear() // Clear the list before adding new data

                    if (snapshot.exists()) {
                        for (appointmentSnapshot in snapshot.children) {
                            val appointment = appointmentSnapshot.getValue(Appointment::class.java)
                            appointment?.let { appointmentList.add(it) }
                        }
                        adapter.notifyDataSetChanged() // Notify the adapter of data change
                    } else {
                        // Handle the case where no appointments are found
                        // Optionally, show a message in the UI
                    }
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle possible errors here
                }
            })
    }

    private fun openChatFragment(appointmentId: String) {
        val chatFragment = ChatFragment() // Replace with your actual ChatFragment class
        val bundle = Bundle().apply {
            putString("appointmentId", appointmentId) // Pass the appointment ID
        }
        chatFragment.arguments = bundle

        // Use FragmentManager or Navigation Component to open the fragment
        requireActivity().supportFragmentManager.beginTransaction()
            .replace(R.id.HomeFragment, chatFragment) // Replace with your container ID
            .addToBackStack(null) // Add to back stack for navigation
            .commit()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        homeBinding = null // Prevent memory leaks
    }
}
