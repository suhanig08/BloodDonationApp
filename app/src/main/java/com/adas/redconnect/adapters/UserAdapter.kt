package com.adas.redconnect.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.adas.redconnect.databinding.EachUserBinding
import com.adas.redconnect.firebaseData.User

class UserAdapter(val context: Context, val userList: ArrayList<User>) : RecyclerView
    .Adapter<UserAdapter
    .UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        return UserViewHolder(EachUserBinding.inflate(LayoutInflater.from(parent.context),
            parent, false))
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val currentUser = userList[position]

        holder.itemBinding.nameTv.text = currentUser.name

    }
    class UserViewHolder(val itemBinding: EachUserBinding) : RecyclerView.ViewHolder(itemBinding
        .root){

    }
}