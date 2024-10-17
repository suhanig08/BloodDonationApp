package com.adas.redconnect.Details_Fragment

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioButton
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import com.adas.redconnect.MainActivity
import com.adas.redconnect.R
import com.adas.redconnect.databinding.FragmentPreviousDonationBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import java.time.LocalDate

class PreviousDonationFragment : Fragment() {
    private var previousDonationBinding: FragmentPreviousDonationBinding? = null
    private val binding get() = previousDonationBinding!!
    private var dateString = ""
    private lateinit var dbRef: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        previousDonationBinding = FragmentPreviousDonationBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        dbRef = FirebaseDatabase.getInstance().getReference("donor")

        binding.calendarView.visibility = View.GONE
        binding.dateChoiceTv.visibility = View.GONE
        binding.doneBtn.visibility = View.GONE

        
        binding.yesOrNoGroup.setOnCheckedChangeListener { group, checkedId ->
            if (checkedId != -1){
                val selectedYesOrNo = view.findViewById<RadioButton>(checkedId)

                if(selectedYesOrNo.text.toString() == "yes"){
                    binding.calendarView.visibility = View.VISIBLE
                    binding.dateChoiceTv.visibility = View.VISIBLE
                    binding.doneBtn.visibility = View.INVISIBLE
                } else {
                    dateString = ""
                    binding.calendarView.visibility = View.INVISIBLE
                    binding.dateChoiceTv.visibility = View.INVISIBLE
                    binding.doneBtn.visibility = View.VISIBLE
                }
            }
        }


        val today = System.currentTimeMillis()
        binding.calendarView.setDate(today)

        binding.calendarView.setOnDateChangeListener { _, year, month, dayOfMonth ->
            // Create a LocalDate instance
            val selectedDate = LocalDate.of(year, month + 1, dayOfMonth)
            dateString = "${selectedDate.dayOfMonth}/${selectedDate
                .monthValue}/${selectedDate.year}"
            Log.e("date", dateString)
            binding.doneBtn.visibility = View.VISIBLE
        }



        binding.doneBtn.setOnClickListener {
            if(dateString.isNotEmpty()){

                val sharedPreferences=activity?.getSharedPreferences("DonorDet",
                    AppCompatActivity.MODE_PRIVATE
                )
                val name=sharedPreferences?.getString("name","")
                if(name!!.isNotEmpty()) {
                    dbRef.child(name).child("dateDonated")
                        .setValue(dateString)
                }
            } else{
                val sharedPreferences=activity?.getSharedPreferences("DonorDet",
                    AppCompatActivity.MODE_PRIVATE
                )
                val name=sharedPreferences?.getString("name","")
                if(name!!.isNotEmpty()) {
                    dbRef.child(name).child("dateDonated")
                        .setValue("")
                }
            }
            val intent = Intent(activity, MainActivity::class.java)
            startActivity(intent)
            activity?.finish()
        }

    }




}