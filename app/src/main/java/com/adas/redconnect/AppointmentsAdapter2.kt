package com.adas.redconnect

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AppointmentAdapter(
    private val appointments: List<Appointment>,
    private val onItemClick: (Appointment) -> Unit
) : RecyclerView.Adapter<AppointmentAdapter.AppointmentViewHolder>() {

    inner class AppointmentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val hospitalNameTv: TextView = itemView.findViewById(R.id.hospitalName)
        val appointmentDateTv: TextView = itemView.findViewById(R.id.apptDate)
        val appointmentTimeTv: TextView = itemView.findViewById(R.id.apptTime)
        val chatButton: ImageButton = itemView.findViewById(R.id.chatButton)

        fun bind(appointment: Appointment) {
            hospitalNameTv.text = appointment.hospitalName
            appointmentDateTv.text = appointment.date
            appointmentTimeTv.text = appointment.time

            // Handle chat button click
            chatButton.setOnClickListener {
                onItemClick(appointment)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AppointmentViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.appointment_item, parent, false)
        return AppointmentViewHolder(view)
    }

    override fun onBindViewHolder(holder: AppointmentViewHolder, position: Int) {
        val appointment = appointments[position]
        holder.bind(appointment)
    }

    override fun getItemCount(): Int = appointments.size
}
