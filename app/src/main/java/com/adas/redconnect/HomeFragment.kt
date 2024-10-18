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
    private lateinit var auth: FirebaseAuth
    private var homeBinding: FragmentHomeBinding? = null
    private val binding get() = homeBinding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        homeBinding = FragmentHomeBinding.inflate(inflater, container, false)

        database = FirebaseDatabase.getInstance()
        auth = FirebaseAuth.getInstance()

        loadAppointment()

        return binding.root
    }

    private fun loadAppointment() {
        val appointmentsRef = database.getReference("appointments")

        appointmentsRef.orderByChild("donorId").equalTo(auth.currentUser?.uid).addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val appointment = snapshot.children.firstOrNull()?.getValue(Appointment::class.java)
                    appointment?.let {
                        bindAppointmentToView(it)
                    }
                } else {
                    // Handle case where no appointment is found
                    binding.hospitalName.text = "No appointments found"
                    binding.apptDate.text = ""
                    binding.apptTime.text = ""
                }
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle possible errors here
            }
        })
    }

    private fun bindAppointmentToView(appointment: Appointment) {

            binding.hospitalName.text = appointment.hospitalName
            binding.apptDate.text = appointment.date // Assuming you have a date property
            binding.apptTime.text = appointment.time // Assuming you have a time property
            // Set other properties as needed

        binding.chatButton.setOnClickListener {
            openChatFragment(appointment.id)
        }
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
