package com.example.sampledbconnect

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.sampledbconnect.ui.theme.SampleDbConnectTheme
import com.google.firebase.Firebase
import com.google.firebase.FirebaseApp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.auth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.database

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        FirebaseApp.initializeApp(this)

        //  TEST DB WRITE
        FirebaseDatabase.getInstance().reference
            .child("connection_test")
            .setValue("Connected! + 4")
            .addOnSuccessListener {
                Log.d("FirebaseCheck", "✅ Firebase connected and write successful")
            }
            .addOnFailureListener {
                Log.e("FirebaseCheck", "❌ Firebase write failed: ${it.message}")
            }

        setContent {
            var loggedInUid by remember { mutableStateOf<String?>(null) }
            var showLoginScreen by remember { mutableStateOf(true) }

            SampleDbConnectTheme {
                enableEdgeToEdge()

                when {
                    loggedInUid != null -> {
                        // ✅ Authenticated → Show Notes
                        NotesScreen(
                            userName = loggedInUid!!,
                            onLogout = { loggedInUid = null } // ✅ This will take user back to LoginScreen
                        )
                    }

                    showLoginScreen -> {
                        // ✅ Login Screen with switch option
                        LoginScreen(
                            onLoginSuccess = { uid -> loggedInUid = uid },
                            onSwitchToSignUp = { showLoginScreen = false }
                        )
                    }

                    else -> {
                        // ✅ SignUp Screen with switch option
                        SignUpScreen(
                            onSignUpSuccess = { uid -> loggedInUid = uid },
                            onSwitchToLogin = { showLoginScreen = true }
                        )
                    }
                }
            }
        }
    }
}

