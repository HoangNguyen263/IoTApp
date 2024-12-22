package com.example.fingerprintdoorlock

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class HistoryFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private val userList = mutableListOf<User>()
    private lateinit var adapter: UserAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_history, container, false)
        recyclerView = view.findViewById(R.id.recycler_view)

        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = UserAdapter(userList)
        recyclerView.adapter = adapter

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