package com.adas.redconnect.fragments

import android.os.Bundle
import android.os.StrictMode
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.core.view.isEmpty
import androidx.fragment.app.Fragment
import com.adas.redconnect.AccessObject
import com.adas.redconnect.R
import com.adas.redconnect.api.Notification
import com.adas.redconnect.api.NotificationApi
import com.adas.redconnect.api.NotificationData
import com.adas.redconnect.databinding.FragmentRequestBloodBinding
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
        val hospitalName = binding.editTextHospitalName.text.toString()
        val spinner = binding.spinnerBloodGroup
        val mobileNumber = binding.editTextMobileNumber.text.toString().trim()
        val requiredDate = binding.editTextRequiredDate.text.toString().trim()
        val units = binding.editTextUnits.text.toString().trim()
        val isCritical = binding.switchCritical.isChecked

        // Get selected position from the spinner
        val selectedPosition = spinner.selectedItemPosition
        var bloodGroup: String? = null

        // Check if an item is selected (position > 0 typically means an item other than the first/default is selected)
        if (selectedPosition == 0) {
            Toast.makeText(context, "Please select a blood group", Toast.LENGTH_SHORT).show()
            return // Exit early if no valid item is selected
        } else {
            bloodGroup = spinner.selectedItem.toString() // Retrieve the selected blood group
        }

        // Validate hospital name, phone number, date, and units
        if (hospitalName.isEmpty()) {
            Toast.makeText(requireContext(), "Please enter the hospital name", Toast.LENGTH_SHORT).show()
            return
        }

        if (mobileNumber.isEmpty() || !isValidPhoneNumber(mobileNumber)) {
            Toast.makeText(requireContext(), "Please enter a valid 10-digit phone number", Toast.LENGTH_SHORT).show()
            return
        }

        if (requiredDate.isEmpty() || !isValidDateFormat(requiredDate)) {
            Toast.makeText(requireContext(), "Please enter the date in the format dd/MM/yyyy", Toast.LENGTH_SHORT).show()
            return
        }

        if (units.isEmpty()) {
            Toast.makeText(requireContext(), "Please enter the number of blood units", Toast.LENGTH_SHORT).show()
            return
        }

        val currentTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date())
        val requestData = hashMapOf(
            "hospital_name" to hospitalName,
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
                    sendNotificationToAllDonors(bloodGroup.toString(), units, requiredDate)
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    Log.e("exception", e.message.toString())
                    Toast.makeText(requireContext(), "Error sending request: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // Function to check if phone number is valid (must have exactly 10 digits)
    private fun isValidPhoneNumber(phoneNumber: String): Boolean {
        return phoneNumber.length == 10 && phoneNumber.all { it.isDigit() }
    }

    // Function to validate date format (dd/MM/yyyy)
    private fun isValidDateFormat(date: String): Boolean {
        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        dateFormat.isLenient = false
        return try {
            dateFormat.parse(date)
            true
        } catch (e: Exception) {
            false
        }
    }

    private fun clearForm() {
        binding.editTextHospitalName.text.clear()
        binding.editTextMobileNumber.text.clear()
        binding.editTextRequiredDate.text.clear()
        binding.editTextUnits.text.clear()
        binding.switchCritical.isChecked = false
        binding.spinnerBloodGroup.setSelection(0)
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
