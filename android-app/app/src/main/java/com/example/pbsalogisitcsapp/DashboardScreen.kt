package com.example.pbsalogisticsapp

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.pbsalogisticsapp.network.*
import kotlinx.coroutines.launch

class EldViewModel(private val mainViewModel: LoginViewModel) : ViewModel() {
    var logs by mutableStateOf<List<EldLog>>(emptyList())
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)

    fun fetchLogs() {
        isLoading = true
        viewModelScope.launch {
            try {
                val response = RetrofitClient.api.getMyLogs(mainViewModel.authToken)
                if (response.isSuccessful) {
                    logs = response.body() ?: emptyList()
                } else {
                    errorMessage = "Failed to load logs"
                }
            } catch (e: Exception) {
                errorMessage = e.message
            } finally {
                isLoading = false
            }
        }
    }

    fun clockIn() {
        viewModelScope.launch {
            try {
                val req = ClockInRequest(duty_status = "Driving", location = "Current Location")
                val response = RetrofitClient.api.clockIn(mainViewModel.authToken, req)
                if (response.isSuccessful) fetchLogs() // Refresh list
            } catch (e: Exception) {
                errorMessage = "Clock in failed: ${e.message}"
            }
        }
    }

    fun clockOut() {
        viewModelScope.launch {
            try {
                val req = ClockOutRequest(notes = "End of shift")
                val response = RetrofitClient.api.clockOut(mainViewModel.authToken, req)
                if (response.isSuccessful) fetchLogs() // Refresh list
            } catch (e: Exception) {
                errorMessage = "Clock out failed: ${e.message}"
            }
        }
    }
}

@Composable
fun DashboardScreen(
    viewModel: LoginViewModel,
    onLogout: () -> Unit
) {
    // Create the ELD ViewModel and pass the login token to it
    val eldViewModel: EldViewModel = viewModel(factory = object : androidx.lifecycle.ViewModelProvider.Factory {
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            return EldViewModel(viewModel) as T
        }
    })

    // Fetch logs when the screen opens
    LaunchedEffect(Unit) {
        eldViewModel.fetchLogs()
    }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {
        // Header
        Text(text = "Driver Dashboard", style = MaterialTheme.typography.headlineMedium)
        Text(text = "Welcome, ${viewModel.currentUser?.full_name ?: "Driver"}", style = MaterialTheme.typography.bodyLarge)
        Spacer(modifier = Modifier.height(16.dp))

        // Action Buttons
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            Button(
                onClick = { eldViewModel.clockIn() },
                modifier = Modifier.weight(1f)
            ) {
                Text("Clock In")
            }
            Button(
                onClick = { eldViewModel.clockOut() },
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text("Clock Out")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
        HorizontalDivider()
        Spacer(modifier = Modifier.height(8.dp))

        Text(text = "My Recent Logs", style = MaterialTheme.typography.titleMedium)

        if (eldViewModel.isLoading) {
            CircularProgressIndicator(modifier = Modifier.align(Alignment.CenterHorizontally))
        } else if (eldViewModel.errorMessage != null) {
            Text(text = eldViewModel.errorMessage!!, color = MaterialTheme.colorScheme.error)
        } else if (eldViewModel.logs.isEmpty()) {
            Text(text = "No logs found. Clock in to start.")
        } else {
            // Log List
            LazyColumn(modifier = Modifier.fillMaxSize()) {
                items(eldViewModel.logs) { log ->
                    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(text = "Status: ${log.duty_status}", fontWeight = FontWeight.Bold)
                            Text(text = "Location: ${log.location ?: "N/A"}")
                            Text(text = "Start: ${log.start_time.replace("T", " ").take(16)}")
                            Text(text = "End: ${log.end_time?.replace("T", " ")?.take(16) ?: "Active"}")
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))
        Button(
            onClick = onLogout,
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.secondary)
        ) {
            Text("Logout")
        }
    }
}