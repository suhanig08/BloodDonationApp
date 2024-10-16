package com.adas.redconnect

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.adas.redconnect.databinding.ActivityChoiceBinding
import com.adas.redconnect.databinding.ActivityDonorAuthBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class ChoiceActivity : AppCompatActivity() {
    private lateinit var binding:ActivityChoiceBinding
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPreferences = getSharedPreferences("ChoicePref", MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        binding = ActivityChoiceBinding.inflate(layoutInflater)

        enableEdgeToEdge()
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.donorBtn.setOnClickListener {
            dbRef=FirebaseDatabase.getInstance().getReference("donor")
            editor.putString("choice", "donor")
            editor.apply()
            val i= Intent(this, DonorAuth::class.java)
            startActivity(i)
            finish()
        }

        binding.hospitalBtn.setOnClickListener {
            dbRef=FirebaseDatabase.getInstance().getReference("hospital")
            editor.putString("choice", "hospital")
            editor.apply()
            val i= Intent(this, HospitalAuth::class.java)
            startActivity(i)
            finish()
        }
    }
}