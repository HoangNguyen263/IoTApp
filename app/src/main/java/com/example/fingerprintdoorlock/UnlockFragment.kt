package com.example.fingerprintdoorlock

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import java.text.SimpleDateFormat
import java.util.*

class UnlockFragment : Fragment() {

    private lateinit var buttonUnlock: Button
    private lateinit var textStatus: TextView
    private val unlockHistory = mutableListOf<User>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_unlock, container, false)
        buttonUnlock = view.findViewById(R.id.button_unlock)
        textStatus = view.findViewById(R.id.text_status)

        buttonUnlock.setOnClickListener {
            unlockDoor("User Name")
        }

        return view
    }

    private fun unlockDoor(userName: String) {
        val currentTime = System.currentTimeMillis()
        val unlockTime = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault()).format(Date(currentTime))
        val user = User(name = userName, unlockTime = unlockTime)

        unlockHistory.add(user)
        textStatus.text = "Status: Door Unlocked at $unlockTime"

        // Pass the unlock history to HistoryFragment
        (activity as MainActivity).updateHistoryFragment(unlockHistory)
    }
}