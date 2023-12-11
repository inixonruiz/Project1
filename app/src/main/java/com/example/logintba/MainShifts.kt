package com.example.logintba

import android.content.Intent
import android.os.Bundle
import android.widget.DatePicker
import android.widget.Toast
import androidx.activity.ComponentActivity
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.logintba.ui.theme.LoginTBATheme
import com.google.firebase.auth.FirebaseAuth

class MainShifts : ComponentActivity() {
    private lateinit var database: FirebaseDatabase
    private lateinit var dateReference: DatabaseReference


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_shifts)
        val user = FirebaseAuth.getInstance().currentUser?.uid

        // Initialize Firebase
        database = FirebaseDatabase.getInstance()
        dateReference = database.getReference("dates").child(user.toString())
    }

    fun onSendButtonClick(view: android.view.View) {
        val datePicker = findViewById<DatePicker>(R.id.datePicker)
        val selectedDate = "${datePicker.year}-${datePicker.month + 1}-${datePicker.dayOfMonth}"
        val user = FirebaseAuth.getInstance().currentUser?.uid

        // Push the date to the database
        val dateId = dateReference.push().key
        dateId?.let {
            dateReference.child(it).setValue(selectedDate)
            //dateReference.child(it).child(it).setValue(user)
            Toast.makeText(this, "Date sent successfully", Toast.LENGTH_SHORT).show()

            // Navigate to ViewDatesActivity
            val intent = Intent(this, ViewDatesActivity::class.java)
            startActivity(intent)
        }
    }

    fun onViewDatesButtonClick(view: android.view.View) {
        // Handle the click of the "View Dates" button
        val intent = Intent(this, ViewDatesActivity::class.java)
        startActivity(intent)
    }
}