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
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ListenerRegistration

class HomeFragment : Fragment() {

    private lateinit var database: FirebaseDatabase
    private lateinit var dbRef: DatabaseReference
    private lateinit var auth: FirebaseAuth
    private lateinit var home_Binding : FragmentHomeBinding
    private var homeBinding: FragmentHomeBinding? = null
    private val binding get() = home_Binding

    // Declare your adapter and appointment list
    private lateinit var appointmentsAdapter: AppointmentsAdapter
    private lateinit var appointmentsList: MutableList<Map<String, Any>>

    private var listenerRegistration: ListenerRegistration? = null
    private val db = FirebaseFirestore.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        homeBinding = FragmentHomeBinding.inflate(inflater, container, false)
        home_Binding = FragmentHomeBinding.inflate(inflater, container, false)

        // Initialize Firebase
        database = FirebaseDatabase.getInstance()
        auth = FirebaseAuth.getInstance()


        return home_Binding.root
    }
   
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbRef=FirebaseDatabase.getInstance().getReference("donor")
        auth = FirebaseAuth.getInstance()

        appointmentsList = mutableListOf()

        // Set up RecyclerView
        binding.appointmentsRv.layoutManager = LinearLayoutManager(requireContext())
        appointmentsAdapter = AppointmentsAdapter(appointmentsList, fragmentManager = parentFragmentManager, currentScreen = "donor", context = requireContext()) { requestData ->
            // Handle item click if needed
        }
        binding.appointmentsRv.adapter = appointmentsAdapter

        // Load appointments from Firestore
        loadAppointments()

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

        
    }

    private fun loadAppointments() {
        // Get the current user's UID
        val currentUserId = FirebaseAuth.getInstance().currentUser?.uid


        // Reference to the Appointments collection in Firestore
        val appointmentsRef = db.collection("Appointments")
            .whereEqualTo("donorId", currentUserId)  // Filter by donorId

        // Real-time listener for filtered appointments collection
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

                // Optionally scroll to the latest appointment
                // hospitalChatBinding.appointmentsRv.scrollToPosition(appointmentsList.size - 1)
            }
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
        listenerRegistration?.remove()
        homeBinding = null // Prevent memory leaks
    }
}
