package com.example.fingerprintdoorlock

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import okhttp3.*
import org.json.JSONArray
import org.json.JSONObject
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private lateinit var historyFragment: HistoryFragment
    private val client = OkHttpClient()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        historyFragment = HistoryFragment()

        val bottomNavigation: BottomNavigationView = findViewById(R.id.bottom_navigation)
        bottomNavigation.setOnNavigationItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav_unlock -> {
                    val unlockFragment = UnlockFragment()
                    openFragment(unlockFragment)
                    true
                }
                R.id.nav_history -> {
                    openFragment(historyFragment)
                    true
                }
                else -> false
            }
        }

        // Open default fragment
        if (savedInstanceState == null) {
            bottomNavigation.selectedItemId = R.id.nav_unlock
        }

        fetchUnlockHistory()
    }

    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    private fun fetchUnlockHistory() {
        val request = Request.Builder()
            .url("https://03fc-116-111-122-206.ngrok-free.app/api/realtime")
            .build()

        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.e("MainActivity", "Failed to fetch data: ${e.message}")
                runOnUiThread {
                    Toast.makeText(this@MainActivity, "Failed to fetch data. Please check your network connection.", Toast.LENGTH_LONG).show()
                }
            }

            override fun onResponse(call: Call, response: Response) {
                if (!response.isSuccessful) {
                    Log.e("MainActivity", "Unexpected code $response")
                    runOnUiThread {
                        Toast.makeText(this@MainActivity, "Server error. Please try again later.", Toast.LENGTH_LONG).show()
                    }
                    return
                }

                val responseData = response.body?.string()
                if (responseData != null) {
                    try {
                        val jsonArray = JSONArray(responseData)
                        val unlockHistory = mutableListOf<User>()
                        for (i in 0 until jsonArray.length()) {
                            val jsonObject = jsonArray.getJSONObject(i)
                            val user = User(
                                id = jsonObject.getJSONObject("_id").getString("\$oid"),
                                status = jsonObject.getString("status"),
                                message = jsonObject.getString("message"),
                                createdAt = jsonObject.getJSONObject("createdAt").getString("\$date")
                            )
                            unlockHistory.add(user)
                        }
                        runOnUiThread {
                            updateHistoryFragment(unlockHistory)
                        }
                    } catch (e: Exception) {
                        Log.e("MainActivity", "Failed to parse JSON: ${e.message}")
                        runOnUiThread {
                            Toast.makeText(this@MainActivity, "Failed to parse data. Please try again later.", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            }
        })
    }

    fun updateHistoryFragment(unlockHistory: List<User>) {
        historyFragment.updateHistory(unlockHistory)
    }
}