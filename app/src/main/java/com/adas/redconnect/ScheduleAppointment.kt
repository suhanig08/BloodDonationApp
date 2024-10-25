package com.adas.redconnect

import android.os.Bundle
import android.widget.Button
import android.widget.DatePicker
import android.widget.TimePicker
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.util.*

class ScheduleAppointment : AppCompatActivity() {

    private lateinit var confirmButton: Button
    private lateinit var selectedDate: String
    private lateinit var selectedTime: String
    private val auth = FirebaseAuth.getInstance()
    private val db = FirebaseFirestore.getInstance()
    private var requestId: String? = null // The ID of the blood request

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_schedule_appointment)

        // Get the blood request ID from the intent
        val sharedPreferences = getSharedPreferences("Request", MODE_PRIVATE)
        requestId = sharedPreferences.getString("docid", null)

        val datePicker = findViewById<DatePicker>(R.id.datePicker)
        val timePicker = findViewById<TimePicker>(R.id.timePicker)
        timePicker.setIs24HourView(true)

        confirmButton = findViewById(R.id.confirmButton)

        confirmButton.setOnClickListener {
            // Get the selected date
            val day = datePicker.dayOfMonth
            val month = datePicker.month + 1 // Month is zero-indexed, so add 1
            val year = datePicker.year
            selectedDate = "$day/$month/$year"

            // Get the selected time
            val hour = timePicker.hour
            val minute = timePicker.minute
            selectedTime = String.format("%02d:%02d", hour, minute)

            // Confirm the appointment and move the request to the Appointments collection
            confirmAppointment()
        }
    }

    private fun confirmAppointment() {
        if (requestId == null) {
            Toast.makeText(this, "Error: Request ID is missing", Toast.LENGTH_SHORT).show()
            return
        }

        // Generate a unique ID for the new appointment
        val appointmentId = db.collection("Appointments").document().id

        // Fetch the blood request document
        db.collection("BloodRequests").document(requestId!!).get().addOnSuccessListener { document ->
            if (document.exists()) {
                // Get the request data
                val requestData = document.data

                // Create appointment data with the generated appointmentId
                val appointmentData = hashMapOf(
                    "appointmentId" to appointmentId, // Adding the unique appointment ID
                    "blood_group" to requestData?.get("blood_group"),
                    "mobile_number" to requestData?.get("mobile_number"),
                    "required_date" to requestData?.get("required_date"),
                    "units" to requestData?.get("units"),
                    "is_critical" to requestData?.get("is_critical"),
                    "created_at" to requestData?.get("created_at"),
                    "scheduled_date" to selectedDate,
                    "scheduled_time" to selectedTime,
                    "donorId" to (auth.currentUser?.uid ?: ""),
                    "hospitalId" to requestData?.get("hospitalId")
                )

                // Add to Appointments collection with the generated appointmentId
                db.collection("Appointments").document(appointmentId).set(appointmentData).addOnSuccessListener {
                    // Remove the request from BloodRequests collection after confirming appointment
                    db.collection("BloodRequests").document(requestId!!).delete().addOnSuccessListener {
                        Toast.makeText(this, "Appointment scheduled successfully!", Toast.LENGTH_SHORT).show()
                        finish() // Close the activity after successful scheduling
                    }.addOnFailureListener { e ->
                        Toast.makeText(this, "Failed to delete blood request: ${e.message}", Toast.LENGTH_SHORT).show()
                    }
                }.addOnFailureListener { e ->
                    Toast.makeText(this, "Failed to schedule appointment: ${e.message}", Toast.LENGTH_SHORT).show()
                }
            } else {
                Toast.makeText(this, "Blood request not found", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener { e ->
            Toast.makeText(this, "Error fetching request: ${e.message}", Toast.LENGTH_SHORT).show()
        }
    }
}
