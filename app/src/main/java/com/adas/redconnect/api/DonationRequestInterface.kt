package com.adas.redconnect.api

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface DonationRequestInterface {
    @POST("predict")
    suspend fun predictDonation(@Body request: DonationRequest): Response<PredictionResponse>
}