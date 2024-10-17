package com.adas.redconnect

import android.os.Bundle
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.fragment.app.Fragment
import com.adas.redconnect.databinding.ActivityHospitalMainBinding
import com.adas.redconnect.fragments.AppointmentFragment
import com.adas.redconnect.fragments.HospitalHomeFragment
import com.adas.redconnect.fragments.RequestFragment

class HospitalMainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityHospitalMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        binding = ActivityHospitalMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        replaceFragment(HospitalHomeFragment())

        binding.bottomNav.setOnItemSelectedListener {
            when(it.itemId){
                R.id.home ->replaceFragment(HospitalHomeFragment())
                R.id.request ->replaceFragment(RequestBloodFragment())
                R.id.appointments ->replaceFragment(AppointmentFragment())

                else->{

                }
            }
            true
        }
    }

    private fun replaceFragment(fragment: Fragment){
        val fragmentManager=supportFragmentManager
        val fragmentTransaction=fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frameLayout,fragment)
        fragmentTransaction.commit()
    }
}