package com.adas.redconnect

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.widget.doOnTextChanged
import com.adas.redconnect.databinding.ActivityHospitalAuthBinding

class HospitalAuth : AppCompatActivity() {
    private lateinit var binding: ActivityHospitalAuthBinding
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

        binding.hospNameEt.doOnTextChanged { text, start, before, count ->
            if(text.toString().trim().isEmpty()){
                binding.nextBtn.isEnabled = false
                binding.nextBtn.setBackgroundResource(R.drawable.btn_unselectable_bg)
                binding.nextBtn.alpha = 0.2F
            } else{
                binding.nextBtn.isEnabled = true
                binding.nextBtn.setBackgroundResource(R.drawable.btn_bg)
                binding.nextBtn.alpha = 1F
            }
        }

        binding.nextBtn.setOnClickListener {
            val intent = Intent(this, LocationActivity::class.java)
            startActivity(intent)
            finish()
        }
        
    }
}