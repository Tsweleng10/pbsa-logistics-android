# PBSA Logistics Android App 🚛

The official mobile application for PBSA Logistics, developed for the XISD6329 Work Integrated Learning module (Task 2).

This app is designed to be used by **Drivers** (to view and accept loads, and log their ELD hours) and **Dispatchers** (to assign loads and monitor driver activity). It connects securely to a live, cloud-hosted REST API.

## 📖 Project Overview

* **Module:** XISD6329 – Work Integrated Learning (Task 2)
* **Team:** LogiLink
* **Institution:** Rosebank College

### 👥 Team Members & Roles

* **Raesibe Kate Galane** (ST10450352) – Project Manager / UI-UX Designer
* **Fortune Pheagane Lemekwana** (ST10450241) – Frontend Web Developer
* **Joshua Regaugetswe Tsweleng** (ST10451745) – Backend Developer
* **Phuti Magwai** (ST10452585) – Android Developer / QA Tester

---

## 🔗 Project Links

* **Backend API Repository:** [https://github.com/Tsweleng10/pbsa-logistics-api.git](https://github.com/Tsweleng10/pbsa-logistics-api.git)
* **Live API URL:** [https://pbsa-logistics-api-production.up.railway.app/](https://pbsa-logistics-api-production.up.railway.app/)
* **Demonstration Video:** [https://youtube.com/shorts/KGOkvYmBWPA?feature=share](https://youtube.com/shorts/KGOkvYmBWPA?feature=share)
* **Website Repository:** [https://github.com/Tsweleng10/pbsa-logistics-website.git](https://github.com/Tsweleng10/pbsa-logistics-website.git)

---

## 🛠️ Tech Stack & Architecture

* **Language:** Kotlin
* **UI Design:** XML with a custom **Glassmorphism** theme (Navy & Gold branding to match the website)
* **Networking:** Retrofit 2 & Gson (REST API integration)
* **Authentication:** JWT (JSON Web Tokens) stored via SharedPreferences
* **IDE:** Android Studio

---

## ✨ Key Features Implemented

1. **Role-Based Authentication:** Secure login connected to the live API using JWT. The app automatically routes the user to the correct dashboard based on their role (Driver vs. Dispatcher).
2. **Driver Dashboard:**
   * View assigned active loads.
   * **Accept Load** functionality (updates status from `PENDING` to `IN_TRANSIT`).
   * **ELD (Electronic Logging Device)** integration: Clock In / Clock Out functionality.
3. **Dispatcher Hub:**
   * Create and assign new loads to specific drivers.
   * View a live feed of recent driver activity (ELD logs).
4. **User Settings:** Drivers can update their name and phone number, which syncs back to the cloud database.
5. **Invalid Input Handling:** The app gracefully handles network errors and bad credentials without crashing.

---

## 🚀 How to Run the Project Locally

1. **Clone the repository:**
   ```bash
   git clone https://github.com/Tsweleng10/pbsa-logistics-android.git
   ```
2. Open the project in Android Studio.
3. Allow Gradle to sync and download all dependencies (Retrofit, Material Design, etc.).
4. Connect a physical Android device (with USB Debugging enabled) or use an emulator.
5. Click the green **Run** button.

> **Note:** The app is hardcoded to point to the live Railway API. No local backend setup is required to test the app.

---

## 🔑 Test Credentials

Use the following seeded accounts to test the different roles:

**👤 Driver Account**

* Email: `driver1@pbsa.co.za`
* Password: `Password123!`

**👔 Dispatcher Account**

* Email: `dispatch1@pbsa.co.za`
* Password: `Password123!`

---

## 🔒 Security & Compliance

* **JWT Integration:** All API requests (Loads, Settings, ELD) are authenticated via Bearer Tokens.
* **Data Privacy:** The app does not store plain-text passwords locally. Tokens are managed via `SharedPreferences`.

---

© 2026 LogiLink Team – Rosebank College
