package com.example.sampledbconnect

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database
import androidx.compose.foundation.lazy.items
import com.google.firebase.auth.FirebaseAuth


@Composable
fun NotesScreen(
    userName: String,
    onLogout: () -> Unit // ✅ callback
) {
    val context = LocalContext.current
    val database = Firebase.database
    val notesRef = database.getReference("Notes Save").child(userName)

    var input by remember { mutableStateOf("") }
    var notesList by remember { mutableStateOf(listOf<NoteItem>()) }

    LaunchedEffect(Unit) {
        notesRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val tempList = mutableListOf<NoteItem>()
                for (noteSnapshot in snapshot.children) {
                    val note = noteSnapshot.getValue(NoteItem::class.java)
                    note?.let { tempList.add(it) }
                }
                notesList = tempList.sortedBy { it.index }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Failed to load notes", Toast.LENGTH_SHORT).show()
            }
        })
    }

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
            notesRef.get().addOnSuccessListener { snapshot ->
                val currentCount = snapshot.childrenCount.toInt()
                val newNote = NoteItem(index = currentCount + 1, note = input)
                notesRef.child(currentCount.toString()).setValue(newNote)
                    .addOnSuccessListener {
                        Toast.makeText(context, "Note added", Toast.LENGTH_SHORT).show()
                        input = ""
                    }
            }
        }) {
            Text("Save Note")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text("Your Notes:", fontSize = 20.sp)
        LazyColumn {
            items(notesList) { item ->
                Text(text = "Note ${item.index}: ${item.note}", modifier = Modifier.padding(8.dp))
            }
        }

        Button(onClick = {
            FirebaseAuth.getInstance().signOut()
            Toast.makeText(context, "Logged out", Toast.LENGTH_SHORT).show()
            onLogout() // ✅ notify MainActivity
        }) {
            Text("Logout")
        }
    }
}

data class NoteItem(
    val index: Int = 0,
    val note: String = ""
)

//@Composable
//fun NotesScreen(userName: String) {
//    val context = LocalContext.current
//    val database = Firebase.database
//    val notesRef = database.getReference("Notes Save").child(userName)
//
//    var input by remember { mutableStateOf("") }
//    var notesList by remember { mutableStateOf(listOf<NoteItem>()) }
//
//    // 🔄 Load notes from Firebase
//    LaunchedEffect(Unit) {
//        notesRef.addValueEventListener(object : ValueEventListener {
//            override fun onDataChange(snapshot: DataSnapshot) {
//                val tempList = mutableListOf<NoteItem>()
//                for (noteSnapshot in snapshot.children) {
//                    val note = noteSnapshot.getValue(NoteItem::class.java)
//                    note?.let { tempList.add(it) }
//                }
//                notesList = tempList.sortedBy { it.index } // optional: sort by index
//            }
//
//            override fun onCancelled(error: DatabaseError) {
//                Toast.makeText(context, "Failed to load notes", Toast.LENGTH_SHORT).show()
//            }
//        })
//    }
//
//    Column(
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center,
//        modifier = Modifier.padding(16.dp)
//    ) {
//        TextField(
//            value = input,
//            onValueChange = { input = it },
//            label = { Text("Enter your note") }
//        )
//
//        Button(onClick = {
//            notesRef.get().addOnSuccessListener { snapshot ->
//                val currentCount = snapshot.childrenCount.toInt()
//                val newNote = NoteItem(index = currentCount + 1, note = input)
//                notesRef.child(currentCount.toString()).setValue(newNote)
//                    .addOnSuccessListener {
//                        Toast.makeText(context, "Note added", Toast.LENGTH_SHORT).show()
//                        input = ""
//                    }
//            }
//        }) {
//            Text("Save Note")
//        }
//
//        Spacer(modifier = Modifier.height(24.dp))
//
//        // 🔽 Display Notes
//        Text("Your Notes:", fontSize = 20.sp)
//        LazyColumn {
//            items(notesList) { item ->
//                Text(text = "Note ${item.index}: ${item.note}", modifier = Modifier.padding(8.dp))
//            }
//        }
//
//        Button(onClick = {
//            FirebaseAuth.getInstance().signOut()
//            Toast.makeText(context, "Logged out", Toast.LENGTH_SHORT).show()
//            // You may want to restart MainActivity or navigate to LoginScreen manually
//        }) {
//            Text("Logout")
//        }
//
//    }
//}
