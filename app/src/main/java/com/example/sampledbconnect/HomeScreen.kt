package com.example.sampledbconnect

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardOptions
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
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.google.firebase.Firebase
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.database


@Composable
fun HomeScreen10(){

}

@Composable
fun HomeScreen12() {
    val database = Firebase.database
    val myRef = database.getReference("Contacts")

    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }

    var fetchedData by remember { mutableStateOf("") } // to show fetched data

    val context = LocalContext.current

    // Attach listener once using LaunchedEffect
    LaunchedEffect(Unit) {
        myRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val stringBuilder = StringBuilder()
                for (contactSnapshot in snapshot.children) {
                    val contact = contactSnapshot.getValue(Contact::class.java)
                    val nameKey = contactSnapshot.key
                    if (contact != null) {
                        stringBuilder.append("Name: $nameKey\nEmail: ${contact.email}\nPhone: ${contact.phone}\n\n")
                    }
                }
                fetchedData = stringBuilder.toString()
                Log.d("FirebaseRead", "Data:\n$fetchedData")
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FirebaseRead", "Failed to read value.", error.toException())
            }
        })
    }

    Column(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        TextField(
            value = name,
            onValueChange = { name = it },
            label = { Text("Enter Name") }
        )

        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Enter Email") }
        )

        TextField(
            value = phone,
            onValueChange = { phone = it },
            label = { Text("Enter Phone") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        Button(
            onClick = {
                val contactsRef = database.reference.child("Contacts")
                val contactRef = contactsRef.child(name)
                val contact = Contact(email, phone)
               contactRef.setValue(contact)
                  Toast.makeText(context, "Save Contact", Toast.LENGTH_LONG).show()
                Log.d("FirebaseSubmit", "Submitting: $name, $email, $phone")

                name = ""
                email = ""
                phone = ""
            },
            modifier = Modifier.padding(16.dp),


        ) {
            Text("Submit")
        }

        // Show the fetched data
        if (fetchedData.isNotEmpty()) {
            Text(text = "Fetched Contacts:\n$fetchedData")
        }
    }
}




data class Contact(
    val email: String,
    val phone: String,
)


@Composable
fun HomeScreen(){
    // Write a message to the database
    val database = Firebase.database
    val myRef = database.getReference()


    var name by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }

    Column (
        modifier = Modifier.padding(16.dp),

        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        TextField(
            value = name,
            onValueChange = {newText -> name = newText},
            label = { Text(text = "Enter Name") }
        )

        TextField(
            value = email,
            onValueChange = {newText -> email = newText},
            label = { Text(text = "Enter eMail") }
        )

        TextField(
            value = phone,
            onValueChange = {newText -> phone = newText},
            label = { Text(text = "Enter Phone") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        val context = LocalContext.current
        Button(
            onClick = {
                val contactsRef = database.reference.child("Contacts")
                val contactRef = contactsRef.child(name)
                val contact = Contact(email, phone)
                contactRef.setValue(contact)

                Toast.makeText(context, "Save Contact", Toast.LENGTH_LONG).show()

                name = ""
                email = ""
                phone = ""
            },
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Submit")
        }
    }

}

//
//data class Contact(
//    val email: String = "",
//    val phone: String = ""
//)


@Composable
fun HomeScreen1(){
    val database = Firebase.database

    var Address by remember { mutableStateOf("") }
    var sample by remember { mutableStateOf("") }
    var id by remember { mutableStateOf("") }

    Column (
        modifier = Modifier.padding(16.dp),

        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        TextField(
            value = Address,
            onValueChange = {newText -> Address = newText},
            label = { Text(text = "Enter Address") }
        )

        TextField(
            value = sample,
            onValueChange = {newText -> sample = newText},
            label = { Text(text = "Enter sample") }
        )

        TextField(
            value = id,
            onValueChange = {newText -> id = newText},
            label = { Text(text = "Enter id") },
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )

        val context = LocalContext.current
        Button(
            onClick = {
                val contacts1Ref = database.reference.child("Contacts1")
                val contact1Ref = contacts1Ref.child(Address)
                val contact1 = Contact1(sample, id)
                contact1Ref.setValue(contact1)

                Toast.makeText(context, "Save Contact1", Toast.LENGTH_LONG).show()

                Address = ""
                sample = ""
                id = ""
            },
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Submit")
        }
    }
}

data class Contact1(
    val sample: String, // Provide a default value (e.g., null)
    val id: String  // Provide a default value (e.g., null)
) {
    // Firebase needs a no-argument constructor for deserialization too,
    // although in this case we are serializing. Adding default values
    // achieves this for the primary constructor.
}


//data class Contact(val email: String, val phone: String)

@Composable
fun HomeScreen0(){
// Write a message to the database
    val database = Firebase.database
    val myRef = database.getReference("message")

    // myRef.setValue("Hello, World!")

    var text by remember { mutableStateOf("") }

    Box {
        Column (
            Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
        ) {
            TextField(
                value = text,
                onValueChange = {newText -> text = newText},
                label = { Text(text = "Enter Name") }
            )

            val context = LocalContext.current

            Button(
                onClick = {
                    myRef.setValue(text)
                    Toast.makeText(context, "Save Contact", Toast.LENGTH_LONG).show()
                },
                modifier = Modifier.padding(top = 16.dp),


            ) {
                Text("Submit")
            }
        }
    }

}