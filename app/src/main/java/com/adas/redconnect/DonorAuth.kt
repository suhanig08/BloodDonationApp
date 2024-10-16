package com.adas.redconnect

import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.adas.redconnect.Data.LocationsItem
import com.adas.redconnect.databinding.ActivityDonorAuthBinding
import com.adas.redconnect.interfaces.LocationInterface
import com.google.firebase.FirebaseException
import com.google.firebase.FirebaseTooManyRequestsException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException
import com.google.firebase.auth.FirebaseAuthMissingActivityForRecaptchaException
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class DonorAuth : AppCompatActivity() {
    private lateinit var binding: ActivityDonorAuthBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var phNum: String
    private lateinit var countrycodePicker: com.hbb20.CountryCodePicker

//    val retrofitBuilder = Retrofit.Builder()
//        .baseUrl("https://api.locationiq.com/v1/")
//        .addConverterFactory(GsonConverterFactory.create())
//        .build()
    override fun onCreate(savedInstanceState: Bundle?) {


    binding = ActivityDonorAuthBinding.inflate(layoutInflater)
    super.onCreate(savedInstanceState)
    enableEdgeToEdge()
    setContentView(binding.root)

    countrycodePicker = binding.ccp
    auth = FirebaseAuth.getInstance()

    binding.NextBtn.setOnClickListener {
        val name=binding.nameEt.text
        val age=binding.ageEt.text
        phNum = binding.phoneEt.text.toString()
        countrycodePicker.registerCarrierNumberEditText(binding.phoneEt)
        if (countrycodePicker.isValidFullNumber&&name.isNotEmpty()&&age.isNotEmpty()) {

                val i = Intent(this, OtpActivity::class.java)
                i.putExtra("phoneNum", countrycodePicker.fullNumberWithPlus)
                startActivity(i)
        } else {
            if(name.isEmpty()) {
                binding.nameEt.error="Please enter your name"
            }
            if(age.isEmpty()) {
                binding.ageEt.error="Please enter your age"
            }
            if(binding.phoneEt.text.isEmpty()){
                binding.phoneEt.error="Please enter your phone number"
            }
            else if(!countrycodePicker.isValidFullNumber){
                binding.phoneEt.error = "Please enter a valid phone number"
            }
            // Show a message to the user indicating the phone number is required

        }
    }
}
}



//        val locationBuilder = retrofitBuilder.create(LocationInterface::class.java)
//            .getLocations(binding.locationEt.text.toString())
//
//        var locations = arrayListOf<String>()
//
//        locationBuilder.enqueue(object : Callback<List<LocationsItem>?> {
//            override fun onResponse(p0: Call<List<LocationsItem>?>, response:
//            Response<List<LocationsItem>?>
//            ) {
//                if(applicationContext != null){
//                    if(response.isSuccessful){
//                        response.body()?.let { objectList ->
//                            for (item in objectList) {
//                                Log.d("item", item.toString())
//                                locations.add(item.display_name)
//                            }
//                        }
//                    }
//                    else {
//                        Log.d("responseError", response.errorBody().toString())
//                    }
//                }
//            }

//            override fun onFailure(p0: Call<List<LocationsItem>?>, p1: Throwable) {
//                TODO("Not yet implemented")
//            }
//        })

