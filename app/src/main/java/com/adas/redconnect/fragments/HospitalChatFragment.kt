package com.adas.redconnect.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.adas.redconnect.Appointment
import com.adas.redconnect.AppointmentsAdapter
import com.adas.redconnect.databinding.FragmentHospitalChatBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class HospitalChatFragment : Fragment() {
    private lateinit var hospitalChatBinding: FragmentHospitalChatBinding
    private lateinit var appointmentsAdapter: AppointmentsAdapter
    private lateinit var appointmentsList: MutableList<Map<String, Any>>
    private lateinit var mAuth: FirebaseAuth
    private val db = FirebaseFirestore.getInstance()
    private var listenerRegistration: ListenerRegistration? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        hospitalChatBinding = FragmentHospitalChatBinding.inflate(inflater, container, false)
        return hospitalChatBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        appointmentsList = mutableListOf()

        // Set up RecyclerView
        hospitalChatBinding.appointmentsRv.layoutManager = LinearLayoutManager(requireContext())
        appointmentsAdapter = AppointmentsAdapter(appointmentsList, fragmentManager = parentFragmentManager, currentScreen = "hospital") { requestData ->
            // Handle item click if needed
        }
        hospitalChatBinding.appointmentsRv.adapter = appointmentsAdapter

        // Load appointments from Firestore
        loadAppointments()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Remove listener when fragment is destroyed
        listenerRegistration?.remove()
    }

    private fun loadAppointments() {
        // Reference to the Appointments collection in Firestore
        val appointmentsRef = db.collection("Appointments")

        // Real-time listener for appointments collection
        listenerRegistration = appointmentsRef.addSnapshotListener { snapshots, error ->
            if (error != null) {
                // Handle error
                return@addSnapshotListener
            }

            // Clear the current list and add new data
            appointmentsList.clear()

            snapshots?.let { querySnapshot ->
                for (document in querySnapshot) {
                    val appointment = document.data
                    appointmentsList.add(appointment)
                }

                // Notify the adapter about the data change
                appointmentsAdapter.notifyDataSetChanged()

                // Optionally scroll to the latest message
                // hospitalChatBinding.appointmentsRv.scrollToPosition(appointmentsList.size - 1)
            }
        }
    }
}
