package com.logilink.pbsalogistics

import android.content.Context
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DispatcherActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dispatcher)

        val prefs = getSharedPreferences("PBSA_PREFS", Context.MODE_PRIVATE)
        val token = "Bearer " + (prefs.getString("JWT_TOKEN", "") ?: "")

        val refInput = findViewById<EditText>(R.id.refInput)
        val originInput = findViewById<EditText>(R.id.originInput)
        val destInput = findViewById<EditText>(R.id.destInput)
        val createBtn = findViewById<Button>(R.id.createLoadBtn)
        val logsText = findViewById<TextView>(R.id.logsText)

        // 1. Create a load
        createBtn.setOnClickListener {
            val req = CreateLoadRequest(
                reference = refInput.text.toString(),
                driver_id = 1, // Hardcoded to Driver 1 for the demo
                origin = originInput.text.toString(),
                destination = destInput.text.toString(),
                cargo_description = "Assigned by Dispatcher",
                weight_kg = 15000
            )

            ApiClient.apiService.createLoad(token, req).enqueue(object : Callback<Load> {
                override fun onResponse(call: Call<Load>, response: Response<Load>) {
                    if (response.isSuccessful) {
                        Toast.makeText(this@DispatcherActivity, "Load Assigned!", Toast.LENGTH_SHORT).show()
                        loadLogs(token, logsText) // Refresh
                    } else {
                        Toast.makeText(this@DispatcherActivity, "Failed (Check Reference uniqueness)", Toast.LENGTH_SHORT).show()
                    }
                }
                override fun onFailure(call: Call<Load>, t: Throwable) {
                    Toast.makeText(this@DispatcherActivity, "Network error", Toast.LENGTH_SHORT).show()
                }
            })
        }

        loadLogs(token, logsText)
    }

    private fun loadLogs(token: String, textView: TextView) {
        ApiClient.apiService.getAllELDLogs(token).enqueue(object : Callback<List<ELDLog>> {
            override fun onResponse(call: Call<List<ELDLog>>, response: Response<List<ELDLog>>) {
                if (response.isSuccessful && response.body() != null) {
                    val logs = response.body()!!
                    if (logs.isEmpty()) {
                        textView.text = "No recent activity."
                    } else {
                        val sb = StringBuilder()
                        for (log in logs) {
                            sb.append("• ${log.driver_name}: ${log.duty_status}\n")
                        }
                        textView.text = sb.toString()
                    }
                }
            }
            override fun onFailure(call: Call<List<ELDLog>>, t: Throwable) {}
        })
    }
}