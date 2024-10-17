package com.adas.redconnect.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.adas.redconnect.R
import com.adas.redconnect.adapters.BloodStockAdapter
import com.adas.redconnect.data.BloodStock
import com.adas.redconnect.databinding.FragmentBloodStocksBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class BloodStocksFragment : Fragment() {
    private var bloodStocksBinding: FragmentBloodStocksBinding? = null
    private val binding get() = bloodStocksBinding!!
    private lateinit var adapter: BloodStockAdapter
    private lateinit var dbRef: DatabaseReference
    private lateinit var stockList: ArrayList<BloodStock>
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        bloodStocksBinding = FragmentBloodStocksBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        stockList = ArrayList()
        dbRef = FirebaseDatabase.getInstance().getReference()
        binding.bloodStockRv.layoutManager = LinearLayoutManager(requireContext())




    }

}