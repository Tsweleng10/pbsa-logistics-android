package com.logilink.pbsalogistics


import android.content.Intent
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

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val emailInput = findViewById<EditText>(R.id.emailInput)
        val passwordInput = findViewById<EditText>(R.id.passwordInput)
        val loginButton = findViewById<Button>(R.id.loginButton)
        val progressBar = findViewById<ProgressBar>(R.id.progressBar)

        loginButton.setOnClickListener {
            val email = emailInput.text.toString().trim()
            val password = passwordInput.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter email and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            progressBar.visibility = View.VISIBLE
            loginButton.isEnabled = false

            val request = LoginRequest(email, password)
            ApiClient.apiService.login(request).enqueue(object : Callback<LoginResponse> {
                override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                    progressBar.visibility = View.GONE
                    loginButton.isEnabled = true

                    if (response.isSuccessful) {
                        val body = response.body()
                        Toast.makeText(this@LoginActivity, "Welcome ${body?.user?.full_name}", Toast.LENGTH_LONG).show()

                        // Hide the loading spinner and enable button again
                        progressBar.visibility = View.GONE
                        loginButton.isEnabled = true

                        // Get the driver's name from the API response
                        val driverName = body?.user?.full_name ?: "Driver"

                        // Save token and name
                        val sharedPrefs = getSharedPreferences("PBSA_PREFS", android.content.Context.MODE_PRIVATE)
                        sharedPrefs.edit().putString("JWT_TOKEN", body?.token).apply()
                        sharedPrefs.edit().putString("DRIVER_NAME", body?.user?.full_name).apply()

                        // Route based on role
                        if (body?.user?.role == "DISPATCHER") {
                            startActivity(android.content.Intent(this@LoginActivity, DispatcherActivity::class.java))
                        } else {
                            startActivity(android.content.Intent(this@LoginActivity, DashboardActivity::class.java))
                        }
                        finish() // Close the login screen so they can't go back to it

                    } else {
                        Toast.makeText(this@LoginActivity, "Invalid email or password", Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                    progressBar.visibility = View.GONE
                    loginButton.isEnabled = true
                    Toast.makeText(this@LoginActivity, "Network Error: ${t.message}", Toast.LENGTH_LONG).show()
                }
            })
        }
    }
}