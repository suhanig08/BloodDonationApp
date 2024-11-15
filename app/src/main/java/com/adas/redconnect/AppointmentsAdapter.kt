package com.adas.redconnect

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.adas.redconnect.data.Appointment

class AppointmentsAdapter(private val appointmentsList: List<Appointment>,
                          private val fragmentManager: FragmentManager):
    RecyclerView.Adapter<AppointmentsAdapter.AppointmentsViewHolder>() {

    inner class AppointmentsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val donorName: TextView = itemView.findViewById(R.id.donorName)
        val apptDate: TextView = itemView.findViewById(R.id.apptDate)
        val apptTime: TextView = itemView.findViewById(R.id.apptTime)
        val chatButton : ImageButton = itemView.findViewById(R.id.chatButton)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppointmentsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.appointment_item, parent, false)
        return AppointmentsViewHolder(view)
    }

    override fun getItemCount(): Int {
        return appointmentsList.size
    }

    override fun onBindViewHolder(holder: AppointmentsViewHolder, position: Int) {
        val appointment = appointmentsList[position]
        holder.donorName.text = appointment.donorName
        holder.apptDate.text = appointment.date
        holder.apptTime.text = appointment.time

        holder.chatButton.setOnClickListener {

            val chatFragment = ChatFragment.newInstance(appointment.id)

            // Replace the current fragment with the ChatFragment
            fragmentManager.beginTransaction()
                .replace(R.id.hospitalChatFragment, chatFragment)
                .addToBackStack(null) // Optional: add to back stack to enable back navigation
                .commit()

        }
    }
}

