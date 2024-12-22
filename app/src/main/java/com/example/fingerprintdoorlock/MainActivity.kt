package com.example.fingerprintdoorlock

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    private lateinit var historyFragment: HistoryFragment

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
    }

    private fun openFragment(fragment: Fragment) {
        val transaction = supportFragmentManager.beginTransaction()
        transaction.replace(R.id.fragment_container, fragment)
        transaction.addToBackStack(null)
        transaction.commit()
    }

    fun updateHistoryFragment(unlockHistory: List<User>) {
        historyFragment.updateHistory(unlockHistory)
    }
}