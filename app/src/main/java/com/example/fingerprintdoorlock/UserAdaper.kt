package com.example.fingerprintdoorlock

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class UserAdapter(private val userList: List<User>) : RecyclerView.Adapter<UserAdapter.UserViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.fragment_history_item, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = userList[position]
        holder.unlockDate.text = user.unlockTime.split(" ")[0]
        holder.unlockTime.text = user.unlockTime.split(" ")[1]
    }

    override fun getItemCount(): Int {
        return userList.size
    }

    class UserViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val unlockDate: TextView = itemView.findViewById(R.id.unlock_date)
        val unlockTime: TextView = itemView.findViewById(R.id.unlock_time)
    }
}