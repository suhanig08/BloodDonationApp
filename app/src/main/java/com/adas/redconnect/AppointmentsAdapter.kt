package com.adas.redconnect

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class AppointmentsAdapter(private val appointmentsList: List<Appointment>):
    RecyclerView.Adapter<AppointmentsAdapter.AppointmentsViewHolder>() {

    inner class AppointmentsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val hospitalName: TextView = itemView.findViewById(R.id.hospitalName)
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
        holder.hospitalName.text = appointment.msg
    }
}