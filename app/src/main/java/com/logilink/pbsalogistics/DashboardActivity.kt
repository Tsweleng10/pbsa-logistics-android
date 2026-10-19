package com.logilink.pbsalogistics

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DashboardActivity : AppCompatActivity() {

    private lateinit var token: String
    private lateinit var loadRouteText: TextView
    private lateinit var loadDetailsText: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        val sharedPrefs = getSharedPreferences("PBSA_PREFS", Context.MODE_PRIVATE)
        token = "Bearer " + (sharedPrefs.getString("JWT_TOKEN", "") ?: "")

        val driverName = sharedPrefs.getString("DRIVER_NAME", "Driver") ?: "Driver"

        val driverNameText = findViewById<TextView>(R.id.driverNameText)
        val clockInButton = findViewById<Button>(R.id.clockInButton)
        val clockOutButton = findViewById<Button>(R.id.clockOutButton)
        val settingsButton = findViewById<Button>(R.id.openSettingsButton)

        loadRouteText = findViewById(R.id.loadRouteText)
        loadDetailsText = findViewById(R.id.loadDetailsText)

        driverNameText.text = driverName

        // 1. Fetch Loads when screen opens
        fetchMyLoads()

        // 2. Settings Button
        settingsButton.setOnClickListener {
            startActivity(Intent(this, SettingsActivity::class.java))
        }

        // 3. Clock In Button
        clockInButton.setOnClickListener {
            val request = ClockInRequest("DRIVING", "Johannesburg Depot", null)
            ApiClient.apiService.clockIn(token, request).enqueue(object : Callback<Any> {
                override fun onResponse(call: Call<Any>, response: Response<Any>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@DashboardActivity, "Clocked In Successfully!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@DashboardActivity, "Already clocked in or error", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<Any>, t: Throwable) {
                    Toast.makeText(this@DashboardActivity, "Network Error", Toast.LENGTH_SHORT).show()
                }
            })
        }

        // 4. Clock Out Button
        clockOutButton.setOnClickListener {
            val request = ClockOutRequest("Shift ended")
            ApiClient.apiService.clockOut(token, request).enqueue(object : Callback<Any> {
                override fun onResponse(call: Call<Any>, response: Response<Any>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@DashboardActivity, "Clocked Out!", Toast.LENGTH_SHORT).show()
                    } else {
                        Toast.makeText(this@DashboardActivity, "You are not clocked in", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<Any>, t: Throwable) {
                    Toast.makeText(this@DashboardActivity, "Network Error", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    private fun fetchMyLoads() {
        ApiClient.apiService.getMyLoads(token).enqueue(object : Callback<List<Load>> {
            override fun onResponse(call: Call<List<Load>>, response: Response<List<Load>>) {
                if (response.isSuccessful && response.body() != null) {
                    val loads = response.body()!!
                    if (loads.isNotEmpty()) {
                        // Show the most recent load
                        val activeLoad = loads[0]
                        loadRouteText.text = "${activeLoad.origin} ➔ ${activeLoad.destination}"
                        loadDetailsText.text = "Ref: ${activeLoad.reference} | Status: ${activeLoad.status}"
                    } else {
                        loadRouteText.text = "No Active Load"
                        loadDetailsText.text = "Waiting for dispatch..."
                    }
                }
            }
            override fun onFailure(call: Call<List<Load>>, t: Throwable) {
                loadRouteText.text = "Network Error"
            }
        })
    }

    // Refresh loads when returning from Settings or another screen
    override fun onResume() {
        super.onResume()
        fetchMyLoads()
    }
}