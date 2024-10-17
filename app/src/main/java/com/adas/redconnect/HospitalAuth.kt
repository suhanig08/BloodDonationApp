package com.adas.redconnect

import android.content.Intent
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doOnTextChanged
import com.adas.redconnect.databinding.ActivityHospitalAuthBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class HospitalAuth : AppCompatActivity() {
    private lateinit var binding: ActivityHospitalAuthBinding
    private lateinit var dbRef: DatabaseReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        binding = ActivityHospitalAuthBinding.inflate(layoutInflater)
        setContentView(binding.root)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        dbRef = FirebaseDatabase.getInstance().getReference()

        val sharedPreferences = getSharedPreferences("ChoicePref", MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        // Enable/disable Next button based on input
        binding.hospNameEt.doOnTextChanged { text, _, _, _ ->
            if (text.toString().trim().isEmpty()) {
                binding.nextBtn.isEnabled = false
                binding.nextBtn.setBackgroundResource(R.drawable.btn_unselectable_bg)
                binding.nextBtn.alpha = 0.2F
            } else {
                binding.nextBtn.isEnabled = true
                binding.nextBtn.setBackgroundResource(R.drawable.btn_bg)
                binding.nextBtn.alpha = 1F
            }
        }

        binding.nextBtn.setOnClickListener {
            // Save hospital name and set logged-in flag
            editor.putString("hospitalName", binding.hospNameEt.text.toString())
            editor.putBoolean("isLoggedIn", true)
            editor.apply()

            // Navigate to LocationActivity
            val intent = Intent(this, LocationActivity::class.java)
            intent.putExtra("hospitalName", binding.hospNameEt.text.toString())
            startActivity(intent)
            finish()
        }
    }
}
