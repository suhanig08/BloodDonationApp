package com.adas.redconnect.fragments

import android.os.Bundle
import android.os.StrictMode
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.adas.redconnect.AccessObject
import com.adas.redconnect.R
import com.adas.redconnect.api.Notification
import com.adas.redconnect.api.NotificationApi
import com.adas.redconnect.api.NotificationData
import com.adas.redconnect.databinding.FragmentRequestBloodBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import java.text.SimpleDateFormat
import java.util.*

class RequestBloodFragment : Fragment() {

    private var _binding: FragmentRequestBloodBinding? = null
    private val binding get() = _binding!!
    private lateinit var db: FirebaseFirestore
    private val auth : FirebaseAuth = FirebaseAuth.getInstance()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRequestBloodBinding.inflate(inflater, container, false)
        val view = binding.root

        db = FirebaseFirestore.getInstance()

        setupBloodGroupSpinner()

        binding.sendRequestButton.setOnClickListener {
            sendBloodRequest()
        }
        val policy= StrictMode.ThreadPolicy.Builder().permitAll().build()
        StrictMode.setThreadPolicy(policy)


        return view
    }

    private fun setupBloodGroupSpinner() {
        ArrayAdapter.createFromResource(
            requireContext(),
            R.array.blood_groups_array,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            binding.spinnerBloodGroup.adapter = adapter
        }
    }

    private fun sendBloodRequest() {
        val bloodGroup = binding.spinnerBloodGroup.selectedItem.toString()
        val mobileNumber = binding.editTextMobileNumber.text.toString().trim()
        val requiredDate = binding.editTextRequiredDate.text.toString().trim()
        val units = binding.editTextUnits.text.toString().trim()
        val isCritical = binding.switchCritical.isChecked

        if (mobileNumber.isEmpty() || requiredDate.isEmpty() || units.isEmpty()) {
            Toast.makeText(requireContext(), "Please fill in all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val currentTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
        val requestData = hashMapOf(
            "blood_group" to bloodGroup,
            "mobile_number" to mobileNumber,
            "required_date" to requiredDate,
            "units" to units,
            "is_critical" to isCritical,
            "created_at" to currentTime, // Include both date and time of request
            "hospitalId" to (auth.currentUser?.uid ?: "")
        )

        // Launch coroutine to add the request to Firestore
        CoroutineScope(Dispatchers.IO).launch {
            try {
                db.collection("BloodRequests").add(requestData).await()
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Request sent successfully!", Toast.LENGTH_SHORT).show()
                    clearForm()
                    // After the request is successfully created, send notifications to all donors
                    sendNotificationToAllDonors(bloodGroup, units, requiredDate)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Error sending request: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
    private suspend fun sendNotificationToAllDonors(bloodGroup: String, units: String, requiredDate: String) {
        CoroutineScope(Dispatchers.IO).launch {
            val accessToken = AccessObject.getAccessToken()
            if (accessToken != null) {
                val notification = Notification(
                    message = NotificationData(
                        "all_users",
                        hashMapOf(
                            "title" to "Urgent Blood Request",
                            "body" to " $units units of $bloodGroup blood by $requiredDate. Please donate if you can.",

                        )
                    )
                )
                NotificationApi.sendNotification().notification(notification).enqueue(
                    object : Callback<Notification> {
                        override fun onResponse(
                            call: Call<Notification>,
                            p1: retrofit2.Response<Notification>
                        ) {
                            Toast.makeText(requireContext(), "Notification sent", Toast.LENGTH_SHORT).show()
                        }

                        override fun onFailure(call: Call<Notification>, t: Throwable) {
                                Toast.makeText(requireContext(), "Notification failed", Toast.LENGTH_SHORT).show()
                        }
                    }
                )
            } else {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Failed to get access token", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun clearForm() {
        binding.editTextMobileNumber.text.clear()
        binding.editTextRequiredDate.text.clear()
        binding.editTextUnits.text.clear()
        binding.switchCritical.isChecked = false
        binding.spinnerBloodGroup.setSelection(0)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
