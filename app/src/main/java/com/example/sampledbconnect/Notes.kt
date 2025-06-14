package com.example.sampledbconnect

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.Firebase
import com.google.firebase.database.database

//@Composable
//fun Notes(){
//    val database = Firebase.database
//    val myRef = database.getReference("message")
//   // myRef.setValue("Hello, World!")
//
//    Column (
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center,
//
//        modifier = Modifier.padding(16.dp)
//    ){
//        var Inputs by remember { mutableStateOf("") }
//
//        Text(
//            text = "Enter the text",
//            fontSize = 18.sp,
//            color = Color.Black,
//            textAlign = TextAlign.Start,
//        )
//
//        TextField(
//            value = Inputs,
//            onValueChange = {NewInputs -> Inputs = NewInputs},
//            label = { Text(text = "Enter your notes") },
//        )
//
//        val context = LocalContext.current
//        var userName by remember { mutableStateOf("") }
//        var userData by remember { mutableStateOf("") }
//
////        TextField(
////            value = userName,
////            onValueChange = {NewuserName -> userName = NewuserName},
////            label = { Text(text = "Enter your notes") },
////        )
//
//        TextField(
//            value = userData,
//            onValueChange = {NewuserData -> userData = NewuserData},
//            label = { Text(text = "Enter your notes") },
//        )
//
//        Button(
//            onClick = {
////                val notesRef = database.reference.child("Notes")
////                val notes = notesRef.child(Inputs)
////                notes.setValue(Inputs)
////                Inputs = ""
//
//
//                val notesRef = database.reference.child("Notes")
//                val notes = notesRef.child(userName)
//                notes.setValue(userData)
//                userData = ""
//
//                Toast.makeText(context, "Notes Saved", Toast.LENGTH_LONG).show()
//
//              //  myRef.setValue(Inputs)
//            }
//        ) {
//            Text(
//                text = "Share Note"
//            )
//        }
//    }
//
//}

//@Composable
//fun Notes() {
//    val database = Firebase.database
//    val notesRef = database.getReference("Notes")
//
//    var userData by remember { mutableStateOf("") }
//    val context = LocalContext.current
//
//    Column(
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center,
//        modifier = Modifier.padding(16.dp)
//    ) {
//        Text(
//            text = "Enter your note",
//            fontSize = 18.sp,
//            color = Color.Black
//        )
//
//        TextField(
//            value = userData,
//            onValueChange = { userData = it },
//            label = { Text("Note") }
//        )
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        Button(
//            onClick = {
//                if (userData.isNotBlank()) {
//                    // Step 1: Read current count
//                    notesRef.get().addOnSuccessListener { snapshot ->
//                        val currentCount = snapshot.childrenCount.toInt()
//                        val newUserKey = "User${currentCount + 1}"
//
//                        // Step 2: Save the new note
//                        notesRef.child(newUserKey).setValue(userData)
//                            .addOnSuccessListener {
//                                Toast.makeText(context, "Note Saved", Toast.LENGTH_SHORT).show()
//                            }
//                            .addOnFailureListener {
//                                Toast.makeText(context, "Save Failed", Toast.LENGTH_SHORT).show()
//                            }
//
//                        userData = ""
//                    }
//                } else {
//                    Toast.makeText(context, "Please enter a note", Toast.LENGTH_SHORT).show()
//                }
//            }
//        ) {
//            Text("Share Note")
//        }
//    }
//}


@Composable
fun Notes(userName: String) {
    val context = LocalContext.current
    val database = Firebase.database
    val notesRef = database.getReference("Notes1").child(userName)

    var input by remember { mutableStateOf("") }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier.padding(16.dp)
    ) {
        TextField(
            value = input,
            onValueChange = { input = it },
            label = { Text("Enter your note") }
        )

        Button(onClick = {
            // Fetch current notes list for this user
            notesRef.get().addOnSuccessListener { dataSnapshot ->
                val currentCount = dataSnapshot.childrenCount.toInt()
                val newNote = mapOf(
                    "index" to currentCount + 1,
                    "note" to input
                )
                notesRef.child(currentCount.toString()).setValue(newNote)
                    .addOnSuccessListener {
                        Toast.makeText(context, "Note added", Toast.LENGTH_SHORT).show()
                        input = ""
                    }
                    .addOnFailureListener {
                        Toast.makeText(context, "Failed: ${it.message}", Toast.LENGTH_SHORT).show()
                    }
            }
        }) {
            Text("Save Note")
        }
    }
}
