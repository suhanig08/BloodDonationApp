import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.adas.redconnect.Appointment
import com.adas.redconnect.adapters.ReceiverAdapter
import com.adas.redconnect.databinding.FragmentDonateBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class DonateFragment : Fragment() {

    private lateinit var binding: FragmentDonateBinding
    private lateinit var receiverAdapter: ReceiverAdapter
    private lateinit var requestList: MutableList<Map<String, Any>>
    private val db = FirebaseFirestore.getInstance()
    private lateinit var mAuth: FirebaseAuth
    private lateinit var database: FirebaseDatabase

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDonateBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        requestList = mutableListOf()

        // Set up RecyclerView
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        receiverAdapter = ReceiverAdapter(requestList) { requestData ->
            acceptRequest(requestData)
        }
        binding.recyclerView.adapter = receiverAdapter

        // Initialize Firebase components
        mAuth = FirebaseAuth.getInstance()
        database = FirebaseDatabase.getInstance()

        // Fetch data from Firestore using a suspend function
        CoroutineScope(Dispatchers.Main).launch {
            val bloodRequests = fetchRequestsFromFirestore()
            if (bloodRequests != null) {
                requestList.clear()
                requestList.addAll(bloodRequests)
                receiverAdapter.notifyDataSetChanged()
            } else {
                Toast.makeText(requireContext(), "Error fetching data", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Suspend function to fetch requests from Firestore
    private suspend fun fetchRequestsFromFirestore(): List<Map<String, Any>>? {
        return try {
            // Fetch data from Firestore on the IO thread
            withContext(Dispatchers.IO) {
                val querySnapshot = db.collection("BloodRequests").get().await()
                val retrievedRequests = mutableListOf<Map<String, Any>>()
                for (document in querySnapshot.documents) {
                    document.data?.let { retrievedRequests.add(it) }
                }
                retrievedRequests // return the fetched requests
            }
        } catch (e: Exception) {
            // Log the error and return null if the query fails
            withContext(Dispatchers.Main) {
                Toast.makeText(requireContext(), "Error fetching data: ${e.message}", Toast.LENGTH_SHORT).show()
            }
            null
        }
    }

    // Function to accept a request
    private fun acceptRequest(requestData: Map<String, Any>) {
        // Extract the necessary data (Hospital name, Date, Time, Blood Group) from requestData
        val hospitalName = requestData["hospitalName"] as? String ?: "Unknown Hospital"
        val date = requestData["date"] as? String ?: "Unknown Date"
        val time = requestData["time"] as? String ?: "Unknown Time"
        val bloodGroup = requestData["blood_group"] as? String ?: "Unknown Blood Group"

        // Trigger sendMessage when a request is accepted
        sendMessage(hospitalName, date, time, bloodGroup)
    }

    // Function to send a message and store the appointment details in Firebase
    private fun sendMessage(hospitalName: String, date: String, time: String, bloodGroup: String) {
        val appointmentsRef = database.getReference("appointments")
        val appointmentId = appointmentsRef.push().key ?: return

        // Create an appointment object with additional fields
        val appointment = Appointment(
            id = appointmentId,
            hospitalId = "", // You can set this if you have a hospital ID in requestData
            donorId = mAuth.currentUser?.uid ?: "Unknown Donor",
            hospitalName = hospitalName,
            msg = "Accepted request for $bloodGroup",
            donorBloodType = bloodGroup,
            timestamp = System.currentTimeMillis().toString(),
            date = date, // Include date from requestData
            time = time  // Include time from requestData
        )

        // Save message to Firebase
        appointmentsRef.child(appointmentId).setValue(appointment)
            .addOnSuccessListener {
                Toast.makeText(requireContext(), "Appointment created for $hospitalName on $date at $time", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener {
                Toast.makeText(requireContext(), "Failed to create appointment", Toast.LENGTH_SHORT).show()
            }
    }
}
