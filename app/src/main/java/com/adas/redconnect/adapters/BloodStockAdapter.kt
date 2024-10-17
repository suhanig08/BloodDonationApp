package com.adas.redconnect.adapters

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.adas.redconnect.databinding.EachBloodStockBinding
import java.util.ArrayList

class BloodStockAdapter(val stockList: ArrayList<String>) : RecyclerView.Adapter<BloodStockAdapter
    .BloodStockViewholder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BloodStockViewholder {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        return stockList.size
    }

    override fun onBindViewHolder(holder: BloodStockViewholder, position: Int) {
        val currBg = stockList[position]
        holder.itemBinding.bloodGroupTv
    }

    class BloodStockViewholder(val itemBinding: EachBloodStockBinding) : RecyclerView.ViewHolder
        (itemBinding.root) {

    }
}