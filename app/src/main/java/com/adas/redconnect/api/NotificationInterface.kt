package com.adas.redconnect.api
import com.adas.redconnect.AccessObject
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST

interface NotificationInterface {
    @POST("/v1/projects/red-connect-3994a/messages:send")
    @Headers(
        "Content-Type:application/json",
        "Accept:application/json",
    )
    fun notification(
        @Body notification: Notification,
        @Header("Authorization") accessToken: String= "Bearer ${AccessObject.getAccessToken()}"
    ): Call<Notification>


}