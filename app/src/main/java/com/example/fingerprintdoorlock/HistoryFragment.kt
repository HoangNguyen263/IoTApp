package com.example.fingerprintdoorlock

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken
import org.eclipse.paho.client.mqttv3.IMqttToken
import org.eclipse.paho.client.mqttv3.MqttCallback
import org.eclipse.paho.client.mqttv3.MqttMessage

class HistoryFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private val userList = mutableListOf<User>()
    private lateinit var adapter: UserAdapter
    private lateinit var mqttClientHelper: MqttClientHelper

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_history, container, false)
        recyclerView = view.findViewById(R.id.recycler_view)

        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = UserAdapter(userList)
        recyclerView.adapter = adapter

        mqttClientHelper = MqttClientHelper(requireContext())
        mqttClientHelper.connect(object : MqttCallback {
            override fun connectionLost(cause: Throwable?) {
                // Handle connection lost
            }

            override fun messageArrived(topic: String?, message: MqttMessage?) {
                val unlockTime = message?.toString() ?: return
                val user = User(name = "User Name", unlockTime = unlockTime)
                updateHistory(listOf(user))
            }

            override fun deliveryComplete(token: IMqttDeliveryToken?) {
                // Handle delivery complete
            }
        })

        return view
    }

    fun updateHistory(unlockHistory: List<User>) {
        if (::adapter.isInitialized) {
            userList.clear()
            userList.addAll(unlockHistory)
            adapter.notifyDataSetChanged()
        }
    }
}