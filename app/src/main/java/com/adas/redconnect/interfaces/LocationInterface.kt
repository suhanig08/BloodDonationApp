package com.adas.redconnect.interfaces

import com.adas.redconnect.Data.LocationsItem
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface LocationInterface {

    @GET("autocomplete?key=pk.4ba3fbf25300bc8d266927b57d0a7bfc")
    fun getLocations(
        @Query("q") q : String
    ) : Call<List<LocationsItem>>
}