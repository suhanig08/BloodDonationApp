package com.adas.redconnect

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.adas.redconnect.databinding.FragmentRequestBloodBinding
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext
import org.json.JSONObject
import java.io.InputStream
import java.text.SimpleDateFormat
import java.util.*

class RequestBloodFragment : Fragment() {

    private var _binding: FragmentRequestBloodBinding? = null
    private val binding get() = _binding!!
    private lateinit var db: FirebaseFirestore

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
            "created_at" to currentTime // Include both date and time of request
        )

        // Launch coroutine to add the request to Firestore
        CoroutineScope(Dispatchers.IO).launch {
            try {
                db.collection("BloodRequests").add(requestData).await()
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Request sent successfully!", Toast.LENGTH_SHORT).show()
                    clearForm()
                    // After the request is successfully created, send notifications to all donors
                    sendNotificationToAllDonors()
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Error sending request: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }



    private suspend fun sendNotificationToAllDonors() {
        CoroutineScope(Dispatchers.IO).launch {
            try {
                val querySnapshot = db.collection("users").get().await()
                val fcmTokens = mutableListOf<String>()
                for (document in querySnapshot) {
                    val token = document.getString("fcm_token")
                    if (token != null) {
                        fcmTokens.add(token)
                    }
                }

                if (fcmTokens.isNotEmpty()) {
                    sendFcmNotification(fcmTokens)
                } else {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(requireContext(), "No tokens found", Toast.LENGTH_SHORT).show()
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Toast.makeText(requireContext(), "Error fetching tokens: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    private suspend fun sendFcmNotification(fcmTokens: List<String>) {
        val notificationBody = mapOf(
            "registration_ids" to fcmTokens, // List of all FCM tokens
            "notification" to mapOf(
                "title" to "Blood Request Alert",
                "body" to "A new blood request has been created. Please check the app.",
                "sound" to "default"
            ),
            "data" to mapOf(
                "extra_info" to "Additional information"
            )
        )

        val jsonBody = JSONObject(notificationBody)

        val requestQueue = Volley.newRequestQueue(requireContext())
        val url = "https://fcm.googleapis.com/fcm/send"

        val accessToken = getAccessToken()

        val fcmRequest = object : JsonObjectRequest(Method.POST, url, jsonBody,
            Response.Listener { response ->
                // Handle successful response
                Toast.makeText(requireContext(), "Notification sent!", Toast.LENGTH_SHORT).show()
            },
            Response.ErrorListener { error ->
                // Handle error
                Toast.makeText(requireContext(), "Error sending notification: ${error.message}", Toast.LENGTH_SHORT).show()
            }) {
            override fun getHeaders(): Map<String, String> {
                val headers = HashMap<String, String>()
                headers["Content-Type"] = "application/json"
                headers["Authorization"] = "Bearer $accessToken" // Use the OAuth access token
                return headers
            }
        }

        requestQueue.add(fcmRequest)
    }

    // Function to get OAuth 2.0 access token
    private fun getAccessToken(): String {
        val inputStream: InputStream = requireContext().resources.openRawResource(R.raw.serviceaccountkey) // Your service account JSON file
        val credentials = GoogleCredentials.fromStream(inputStream)
        credentials.refreshIfExpired()
        return credentials.accessToken.tokenValue
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
