package com.adas.redconnect.api

data class NotificationData(
    val topic: String?=null,
    val data : HashMap<String,String>?=null,
)
