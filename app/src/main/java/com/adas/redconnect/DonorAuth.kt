package com.adas.redconnect

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
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

    override fun onCreate(savedInstanceState: Bundle?) {
        binding = ActivityDonorAuthBinding.inflate(layoutInflater)
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(binding.root)

        dbRef = FirebaseDatabase.getInstance().getReference("donor")
        countrycodePicker = binding.ccp
        auth = FirebaseAuth.getInstance()

        val sharedPreferences=getSharedPreferences("DonorDet", MODE_PRIVATE)
        val editor=sharedPreferences.edit()


        binding.NextBtn.setOnClickListener {
            // Save donor details
            editor.putString("name",binding.nameEt.text.toString())
            editor.putString("age",binding.ageEt.text.toString())
            editor.putString("phone",binding.phoneEt.text.toString())
                .apply()

            val name = binding.nameEt.text
            phNum = binding.phoneEt.text.toString()
            countrycodePicker.registerCarrierNumberEditText(binding.phoneEt)

            if (countrycodePicker.isValidFullNumber && name.isNotEmpty()) {
                // Set logged-in flag


                // Navigate to OTP verification activity
                val intent = Intent(this, OtpActivity::class.java)
                intent.putExtra("phoneNum", countrycodePicker.fullNumberWithPlus)
                startActivity(intent)
                finish()
            } else {
                // Show validation errors if inputs are invalid
                if (name.isEmpty()) {
                    binding.nameEt.error = "Please enter your name"
                }
                if (binding.phoneEt.text.isEmpty()) {
                    binding.phoneEt.error = "Please enter your phone number"
                } else if (!countrycodePicker.isValidFullNumber) {
                    binding.phoneEt.error = "Please enter a valid phone number"
                }
            }
        }
    }
}
