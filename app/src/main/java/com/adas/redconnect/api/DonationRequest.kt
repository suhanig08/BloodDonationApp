package com.adas.redconnect.api

data class DonationRequest(
    val recency: Int,
    val frequency: Int,
    val monetary: Double,
    val time: Int
)