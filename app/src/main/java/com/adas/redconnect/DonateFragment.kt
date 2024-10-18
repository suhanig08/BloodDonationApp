package com.adas.redconnect

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.adas.redconnect.adapters.ReceiverAdapter
import com.adas.redconnect.databinding.FragmentDonateBinding
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

    private fun acceptRequest(requestData: Map<String, Any>) {
        // Handle the acceptance of the blood request
        Toast.makeText(requireContext(), "Accepted request for ${requestData["blood_group"]}", Toast.LENGTH_SHORT).show()
    }
}
