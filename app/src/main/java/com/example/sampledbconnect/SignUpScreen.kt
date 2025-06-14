package com.example.sampledbconnect

import android.widget.Toast
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

@Composable
fun SignUpScreen(
    onSignUpSuccess: (String) -> Unit,
    onSwitchToLogin: () -> Unit
) {
    val context = LocalContext.current
    val auth = FirebaseAuth.getInstance()

    val emailValue = remember { mutableStateOf("") }
    val passwordValue = remember { mutableStateOf("") }

    Column(modifier = Modifier.padding(16.dp)) {
        Text("Register", fontSize = 20.sp)
        Spacer(modifier = Modifier.height(8.dp))

        TextField(
            value = emailValue.value,
            onValueChange = { emailValue.value = it },
            label = { Text("Email") },
            modifier = Modifier.fillMaxWidth()
        )

        TextField(
            value = passwordValue.value,
            onValueChange = { passwordValue.value = it },
            label = { Text("Password") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(16.dp))

        Button(onClick = {
            val email = emailValue.value.trim()
            val password = passwordValue.value.trim()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            val uid = auth.currentUser?.uid ?: ""
                            FirebaseDatabase.getInstance().reference
                                .child("users")
                                .child(uid)
                                .setValue(mapOf("email" to email))

                            onSignUpSuccess(uid)
                        } else {
                            Toast.makeText(context, task.exception?.message, Toast.LENGTH_SHORT).show()
                        }
                    }
            }
        }) {
            Text("Register")
        }

        TextButton(onClick = onSwitchToLogin) {
            Text("Already have an account? Login")
        }
    }
}


//@Preview
//@Composable
//fun SignUpScreen(){
//    Surface (
//        modifier = Modifier
//            .fillMaxSize()
//            .verticalScroll(rememberScrollState())
//    ){
//        Column (
//            modifier = Modifier
//                .padding(16.dp)
//                .fillMaxSize()
//        ) {
//            Box{
//                Column (
//                    modifier = Modifier
//                        .padding(16.dp)
//                        .fillMaxWidth()
//                ){
//                    val emailValue = remember { mutableStateOf("") }
//                    val passwordValue = remember { mutableStateOf("") }
//
//                    Text("Email")
//                    TextField(
//                        value = emailValue.value,
//                        onValueChange = {emailValue.value = it},
//
//                        modifier = Modifier
//                            .fillMaxWidth(),
//                        keyboardOptions = KeyboardOptions.Default,
//                    )
//
//                    Text("Password")
//                    TextField(
//                        value = passwordValue.value,
//                        onValueChange = {passwordValue.value = it},
//
//                        modifier = Modifier
//                            .fillMaxWidth(),
//                        //  visualTransformation = PasswordVisualTransformation(),
//                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
//                    )
//
//                    Button(
//                        onClick = {}
//                    ) {
//                        Text("Register",
//                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
//                        )
//
//                    }
//                }
//            }
//        }
//    }
//}