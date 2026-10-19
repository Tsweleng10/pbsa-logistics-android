package com.logilink.pbsalogistics

import android.content.Context
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ProgressBar
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)

        val nameInput = findViewById<EditText>(R.id.settingsNameInput)
        val phoneInput = findViewById<EditText>(R.id.settingsPhoneInput)
        val saveButton = findViewById<Button>(R.id.saveSettingsButton)
        val progressBar = findViewById<ProgressBar>(R.id.settingsProgressBar)

        // Load saved name into the text box
        val sharedPrefs = getSharedPreferences("PBSA_PREFS", Context.MODE_PRIVATE)
        nameInput.setText(sharedPrefs.getString("DRIVER_NAME", ""))

        saveButton.setOnClickListener {
            val newName = nameInput.text.toString().trim()
            val newPhone = phoneInput.text.toString().trim()
            val token = sharedPrefs.getString("JWT_TOKEN", "") ?: ""

            if (newName.isEmpty()) {
                Toast.makeText(this, "Name cannot be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            progressBar.visibility = View.VISIBLE
            saveButton.isEnabled = false

            // Call the API
            val request = UpdateSettingsRequest(newName, newPhone)
            ApiClient.apiService.updateSettings("Bearer $token", request).enqueue(object : Callback<User> {
                override fun onResponse(call: Call<User>, response: Response<User>) {
                    progressBar.visibility = View.GONE
                    saveButton.isEnabled = true

                    if (response.isSuccessful) {
                        Toast.makeText(this@SettingsActivity, "Settings saved successfully!", Toast.LENGTH_SHORT).show()

                        // Update the local name cache
                        sharedPrefs.edit().putString("DRIVER_NAME", newName).apply()

                        // Go back to the Dashboard
                        finish()
                    } else {
                        Toast.makeText(this@SettingsActivity, "Failed to save settings", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<User>, t: Throwable) {
                    progressBar.visibility = View.GONE
                    saveButton.isEnabled = true
                    Toast.makeText(this@SettingsActivity, "Network Error", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }
}