package com.adas.redconnect

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity.MODE_PRIVATE
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.adas.redconnect.databinding.AppointmentItemBinding

class AppointmentsAdapter(private val appointmentsList: List<Map<String, Any>>,
                          private val fragmentManager: FragmentManager,
                          private val currentScreen: String,
                          private val context : Context,
                          private val onConfirmClick: (Map<String, Any>) -> Unit):
    RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private val VIEW_TYPE_DONOR = 1
    private val VIEW_TYPE_HOSPITAL = 2

    inner class AppointmentsViewHolder_Hospital(private val binding : AppointmentItemBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(requestData : Map<String, Any>){
            val appt_ID = requestData["appointmentId"] as? String ?: "N/A"
            val hospitalName = requestData["hospitalName"] as? String ?: "N/A"
            val selectedDate = requestData["scheduled_date"] as? String ?: "N/A"
            val selectedTime = requestData["scheduled_time"] as? String ?: "N/A"

            binding.hospitalName.text = hospitalName
            binding.apptDate.text = selectedDate
            binding.apptTime.text = selectedTime

            binding.chatButton.setOnClickListener {
                val chatFragment = ChatFragment.newInstance(appt_ID)

                // Replace the current fragment with the ChatFragment
                fragmentManager.beginTransaction()
                    .replace(R.id.hospitalChatFragment, chatFragment)
                    .addToBackStack(null) // Optional: add to back stack to enable back navigation
                    .commit()


            }

            binding.tickButton.visibility = View.VISIBLE
            binding.tickButton.setOnClickListener {
                AlertDialog.Builder(context).apply {
                    setTitle("Confirmation")
                    setMessage("Are you sure you want to confirm this action?")

                    // Add a Confirm button with an action
                    setPositiveButton("Confirm") { dialog, _ ->


                        onConfirmClick(requestData)
                        dialog.dismiss()  // Close the dialog
                    }

                    // Add a Cancel button to dismiss the dialog
                    setNegativeButton("Cancel") { dialog, _ ->
                        dialog.dismiss()
                    }

                }.create().show()
            }
        }

    }

    inner class AppointmentsViewHolder_Donor(private val binding : AppointmentItemBinding)
        : RecyclerView.ViewHolder(binding.root) {

        fun bind(requestData : Map<String, Any>){
            val appt_ID = requestData["appointmentId"] as? String ?: "N/A"
            val hospitalName = requestData["hospitalName"] as? String ?: "N/A"
            val selectedDate = requestData["scheduled_date"] as? String ?: "N/A"
            val selectedTime = requestData["scheduled_time"] as? String ?: "N/A"

            binding.hospitalName.text = hospitalName
            binding.apptDate.text = selectedDate
            binding.apptTime.text = selectedTime

            binding.chatButton.setOnClickListener {
                val chatFragment = ChatFragment.newInstance(appt_ID)

                // Replace the current fragment with the ChatFragment
                fragmentManager.beginTransaction()
                    .replace(R.id.HomeFragment, chatFragment)
                    .addToBackStack(null) // Optional: add to back stack to enable back navigation
                    .commit()

            }
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val binding = AppointmentItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return if (viewType == VIEW_TYPE_HOSPITAL) {
            AppointmentsViewHolder_Hospital(binding)
        } else {
            AppointmentsViewHolder_Donor(binding)
        }
    }

    override fun getItemCount(): Int {
        return appointmentsList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

        if (holder  is AppointmentsViewHolder_Hospital)
            holder.bind(appointmentsList[position])
        else if (holder is AppointmentsViewHolder_Donor)
            holder.bind(appointmentsList[position])

    }

    override fun getItemViewType(position: Int): Int {
         return if (currentScreen == "donor") VIEW_TYPE_DONOR else VIEW_TYPE_HOSPITAL
    }
}