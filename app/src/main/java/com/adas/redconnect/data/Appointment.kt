package com.adas.redconnect.data

data class Appointment(
    val id : String = "",
    val msg : String = "",
    val donorId: String = "",
    val donorName:String="",
    val hospitalId: String = "",
    val hospitalName: String = "",
    val donorBloodType: String = "",
    val date: String = "",
    val time: String = "",
    val timestamp: String = ""
)
