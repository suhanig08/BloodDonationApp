package com.adas.redconnect

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.adas.redconnect.databinding.ActivityDonorAuthBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class DonorAuth : AppCompatActivity() {
    private lateinit var binding: ActivityDonorAuthBinding
    private lateinit var auth: FirebaseAuth
    private lateinit var dbRef: DatabaseReference
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

    dbRef= FirebaseDatabase.getInstance().getReference("donor")

    countrycodePicker = binding.ccp
    auth = FirebaseAuth.getInstance()
    val sharedPreferences = getSharedPreferences("DonorDet", MODE_PRIVATE)
    val editor = sharedPreferences.edit()

    binding.NextBtn.setOnClickListener {
        editor.putString("name", binding.nameEt.text.toString())
        editor.apply()
        dbRef.child(binding.nameEt.text.toString()).child("uid").setValue(auth.currentUser!!.uid)
        dbRef.child(binding.nameEt.text.toString()).child("phone").setValue(binding.phoneEt.text.toString())
        dbRef.child(binding.nameEt.text.toString()).child("age").setValue(binding.ageEt.text.toString())
        val name=binding.nameEt.text
        phNum = binding.phoneEt.text.toString()
        countrycodePicker.registerCarrierNumberEditText(binding.phoneEt)
        if (countrycodePicker.isValidFullNumber&&name.isNotEmpty()) {

                val i = Intent(this, OtpActivity::class.java)
                i.putExtra("phoneNum", countrycodePicker.fullNumberWithPlus)
                startActivity(i)
                finish()
        } else {
            if(name.isEmpty()) {
                binding.nameEt.error="Please enter your name"
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

