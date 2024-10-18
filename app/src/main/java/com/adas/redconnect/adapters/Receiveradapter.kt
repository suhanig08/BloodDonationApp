package com.adas.redconnect.adapters

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.adas.redconnect.QuestionnaireActivity
import com.adas.redconnect.databinding.ItemSelectorBinding

class ReceiverAdapter(
    private val requestList: List<Map<String, Any>>,
    private val onAcceptClick: (Map<String, Any>) -> Unit
) : RecyclerView.Adapter<ReceiverAdapter.ReceiverViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ReceiverViewHolder {
        val binding = ItemSelectorBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ReceiverViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ReceiverViewHolder, position: Int) {
        holder.bind(requestList[position])
    }

    override fun getItemCount(): Int {
        return requestList.size
    }

    inner class ReceiverViewHolder(private val binding: ItemSelectorBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(requestData: Map<String, Any>) {
            // Use safe calls and default values in case of null
            val bloodGroup = requestData["blood_group"] as? String ?: "N/A"
            val mobileNumber = requestData["mobile_number"] as? String ?: "Unknown"
            val requiredDate = requestData["required_date"] as? String ?: "Unknown Date"
            val units = requestData["units"] as? Long ?: 0L // Assuming 'units' is stored as Long in Firestore
            val isCritical = requestData["is_critical"] as? Boolean ?: false
            val createdAt = requestData["created_at"] as? String ?: "Unknown Time"

            binding.bloodGroup.text = bloodGroup
            binding.mobileNumber.text = mobileNumber
            binding.requiredDate.text = requiredDate
            binding.units.text = "$units Units"
            binding.createdAt.text = "Created at: $createdAt"

            // Set critical status text and color
            binding.criticalStatus.text = if (isCritical) "Critical: Yes" else "Critical: No"
            binding.criticalStatus.setTextColor(
                if (isCritical) binding.root.context.getColor(android.R.color.holo_red_dark)
                else binding.root.context.getColor(android.R.color.darker_gray)
            )

            // Handle accept button click
            binding.acceptButton.setOnClickListener {
                // Navigate to QuestionnaireActivity
                val context = binding.root.context
                val intent = Intent(context, QuestionnaireActivity::class.java)
                context.startActivity(intent)

                // Trigger the callback
                onAcceptClick(requestData)
            }
        }
    }
}
