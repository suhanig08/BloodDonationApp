package com.adas.redconnect

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.adas.redconnect.api.DonationRequest
import com.adas.redconnect.api.DonationRequestInterface
import com.adas.redconnect.databinding.ActivityInputDataBinding
import com.google.gson.GsonBuilder
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class InputDataActivity : AppCompatActivity() {
    private lateinit var binding: ActivityInputDataBinding
    private val url = "https://ml-model-for-blood-donation.onrender.com/predict"
    private lateinit var apiService: DonationRequestInterface
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityInputDataBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

//        binding.submitBtn.setOnClickListener {
//            val stringRequest = StringRequest(Request.Method.POST, url,
//                Response.Listener<String> {
//
//                }
//                )
//        }

        val gson = GsonBuilder()
            .setLenient()
            .create()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://ml-model-for-blood-donation.onrender.com/")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
        apiService = retrofit.create(DonationRequestInterface::class.java)
        binding.submitBtn.setOnClickListener{
            lifecycleScope.launch {
                makePredictionRequest()
            }
        }

    }

    suspend fun makePredictionRequest() {
        // Example values for recency, frequency, monetary, and time
        val donationRequest = DonationRequest(binding.recency.text.toString().toInt(), binding.freq
            .text.toString().toInt(),
            binding.monetary.text.toString().toDouble(),
            binding.time.text.toString().toInt())
//        val donationRequest = DonationRequest(1, 20, 3.20, 2)
        Log.i("donation", donationRequest.toString())

        // Call the API
        val response = apiService.predictDonation(donationRequest)

        // Handle the response
        if (response.isSuccessful) {
            val predictionResponse = response.body()
            Log.i("response", "${response.body()}")
//
            if (predictionResponse != null) {
                Log.i("RawResponse", "$predictionResponse")
                val probability = predictionResponse.probability
            }
            predictionResponse?.let {
                val prob = it.probability
                if(prob > 0.6){
                    Toast.makeText(this, "You are eligible, $prob", Toast.LENGTH_SHORT).show()
                } else{
                    Toast.makeText(this, "Not Eligible, $prob", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            Log.e("retrofitError", "${response.errorBody()?.string()}")
        }
    }
}