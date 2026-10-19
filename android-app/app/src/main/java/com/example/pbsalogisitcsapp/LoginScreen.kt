class LoginViewModel : ViewModel() {
    var email by mutableStateOf("")
    var password by mutableStateOf("")
    var isLoading by mutableStateOf(false)
    var errorMessage by mutableStateOf<String?>(null)
    var loginSuccess by mutableStateOf(false)

    // NEW: Store the token and the full user object here
    var authToken by mutableStateOf("")
    var currentUser by mutableStateOf<User?>(null)

    fun login() {
        if (email.isBlank() || password.isBlank()) {
            errorMessage = "Please fill in all fields"
            return
        }
        isLoading = true
        errorMessage = null

        viewModelScope.launch {
            try {
                val response = RetrofitClient.api.login(LoginRequest(email, password))
                if (response.isSuccessful && response.body() != null) {
                    val body = response.body()!!
                    // Save the token and user data
                    authToken = "Bearer ${body.token}" // Add "Bearer " prefix for Josh's middleware
                    currentUser = body.user
                    loginSuccess = true
                } else {
                    errorMessage = "Login failed: Check credentials"
                }
            } catch (e: Exception) {
                errorMessage = "Network Error: ${e.message}"
            } finally {
                isLoading = false
            }
        }
    }
}