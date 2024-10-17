package com.adas.redconnect

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.adas.redconnect.databinding.ActivityChoiceBinding

class ChoiceActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChoiceBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sharedPreferences = getSharedPreferences("ChoicePref", MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        // Check if the user is already logged in
        val isLoggedIn = sharedPreferences.getBoolean("isLoggedIn", false)
        if (isLoggedIn) {
            // If the user is already logged in, navigate to MainActivity
            startActivity(Intent(this, MainActivity::class.java))
            finish()
            return
        }

        // If not logged in, proceed with the usual setup
        binding = ActivityChoiceBinding.inflate(layoutInflater)
        enableEdgeToEdge()
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        binding.donorBtn.setOnClickListener {
            editor.putString("choice", "donor")
            editor.apply()
            val i = Intent(this, DonorAuth::class.java)
            startActivity(i)
            finish()
        }

        binding.hospitalBtn.setOnClickListener {
            editor.putString("choice", "hospital")
            editor.apply()
            val i = Intent(this, HospitalAuth::class.java)
            startActivity(i)
            finish()
        }
    }
}
