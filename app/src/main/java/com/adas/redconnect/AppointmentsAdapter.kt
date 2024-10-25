package com.adas.redconnect

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.adas.redconnect.databinding.AppointmentItemBinding

class AppointmentsAdapter(private val appointmentsList: List<Map<String, Any>>,
                          private val fragmentManager: FragmentManager,
                          private val currentScreen: String,
                          private val onAcceptClick: (Map<String, Any>) -> Unit):
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

                onAcceptClick(requestData)

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

                onAcceptClick(requestData)

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