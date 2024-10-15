package com.adas.redconnect

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.adas.redconnect.Data.LocationsItem
import com.adas.redconnect.databinding.ActivityDonorAuthBinding
import com.adas.redconnect.interfaces.LocationInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DonorAuth : AppCompatActivity() {
    private lateinit var binding: ActivityDonorAuthBinding

    val retrofitBuilder = Retrofit.Builder()
        .baseUrl("https://api.locationiq.com/v1/")
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    override fun onCreate(savedInstanceState: Bundle?) {

        binding = ActivityDonorAuthBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)



        val locationBuilder = retrofitBuilder.create(LocationInterface::class.java)
            .getLocations(binding.locationEt.text.toString())

        var locations = arrayListOf<String>()

        locationBuilder.enqueue(object : Callback<List<LocationsItem>?> {
            override fun onResponse(p0: Call<List<LocationsItem>?>, response:
            Response<List<LocationsItem>?>
            ) {
                if(applicationContext != null){
                    if(response.isSuccessful){
                        response.body()?.let { objectList ->
                            for (item in objectList) {
                                Log.d("item", item.toString())
                                locations.add(item.display_name)
                            }
                        }
                    }
                    else {
                        Log.d("responseError", response.errorBody().toString())
                    }
                }
            }

            override fun onFailure(p0: Call<List<LocationsItem>?>, p1: Throwable) {
                TODO("Not yet implemented")
            }
        })



    }
}