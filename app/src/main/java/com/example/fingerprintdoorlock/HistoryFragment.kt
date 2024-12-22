package com.example.fingerprintdoorlock

import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException
import android.widget.Toast

class HistoryFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private val userList = mutableListOf<User>()
    private lateinit var adapter: UserAdapter
    private val client = OkHttpClient()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_history, container, false)
        recyclerView = view.findViewById(R.id.recycler_view)

        recyclerView.layoutManager = LinearLayoutManager(context)
        adapter = UserAdapter(userList)
        recyclerView.adapter = adapter

        // Perform the first fetch
        fetchData()

        return view
    }

    private fun fetchData(attempt: Int = 1) {
        val request = Request.Builder()
            .url("https://03fc-116-111-122-206.ngrok-free.app/api/realtime")
            .get()
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("HistoryFragment", "Attempt $attempt: API request failed: ${e.message}")
                requireActivity().runOnUiThread {
                    Toast.makeText(context, "Attempt $attempt failed to fetch data. Please check your network connection.", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                if (response.isSuccessful) {
                    val responseBody = response.body?.string()
                    if (responseBody != null) {
                        try {
                            val jsonResponse = JSONObject(responseBody)
                            val jsonArray = jsonResponse.getJSONArray("data")
                            val newUsers = mutableListOf<User>()
                            for (i in 0 until jsonArray.length()) {
                                val jsonObject = jsonArray.getJSONObject(i)
                                val user = User(
                                    id = jsonObject.getString("_id"),
                                    status = jsonObject.getString("status"),
                                    message = jsonObject.getString("message"),
                                    createdAt = jsonObject.getString("createdAt")
                                )
                                newUsers.add(user)
                            }

                            // Update UI on the main thread
                            requireActivity().runOnUiThread {
                                updateHistory(newUsers)
                                Toast.makeText(context, "Attempt $attempt: Fetched data successfully!", Toast.LENGTH_SHORT).show()
                            }

                            // Schedule the second fetch if this is the first attempt
                            if (attempt == 1) {
                                Handler(Looper.getMainLooper()).postDelayed({
                                    fetchData(attempt = 2)
                                }, 2000) // Wait for 2 seconds before the second fetch
                            }

                        } catch (e: Exception) {
                            Log.e("HistoryFragment", "Attempt $attempt: Failed to parse JSON: ${e.message}")
                            requireActivity().runOnUiThread {
                                Toast.makeText(context, "Attempt $attempt: Failed to parse data. Please try again later.", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                } else {
                    Log.e("HistoryFragment", "Attempt $attempt: API response not successful: ${response.code}")
                    requireActivity().runOnUiThread {
                        Toast.makeText(context, "Attempt $attempt: API response failed.", Toast.LENGTH_SHORT).show()
                    }
                }
            }
        })
    }

    public fun updateHistory(unlockHistory: List<User>) {
        Log.d("HistoryFragment", "Updating history with ${unlockHistory.size} items")
        userList.clear()
        userList.addAll(unlockHistory)
        adapter.notifyDataSetChanged()
    }
}