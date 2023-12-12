package com.example.logintba

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class ViewShiftsActivity : AppCompatActivity() {

    private lateinit var dateListView: ListView
    private lateinit var database: FirebaseDatabase
    private lateinit var btnGoBack: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_shifts)

        dateListView = findViewById(R.id.dateListView)
        btnGoBack = findViewById(R.id.btnGoBack)
        database = FirebaseDatabase.getInstance()

        // Read dates from Firebase and display in ListView

        val user = FirebaseAuth.getInstance().currentUser?.uid
        val dateReference = database.getReference("dates")

        dateReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val dates = mutableListOf<String>()
                for (dateSnapshot in snapshot.children) {
                    if (dateSnapshot.key != FirebaseAuth.getInstance().currentUser?.uid){
                    for (date2Snapshot in dateSnapshot.children){
                        val date = date2Snapshot.value as String
                            dates.add(date)
                    }
                }}

                // Display dates in the ListView
                val adapter = ArrayAdapter(this@ViewShiftsActivity,android.R.layout.simple_list_item_1, dates)
                dateListView.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {
                // Handle error
            }
        })

        // Set a click listener for the "Go Back" button
        btnGoBack.setOnClickListener {
            // Navigate to MainActivity
            val intent = Intent(this, MainShifts::class.java)
            startActivity(intent)

            // Finish the current activity to remove it from the back stack
            finish()
        }

        //Set a Click listener for the List View
        dateListView.onItemClickListener = AdapterView.OnItemClickListener { _, _, position, _ ->
            // Get the selected date from the list
            val selectedDate = dateListView.getItemAtPosition(position) as String

            Toast.makeText(applicationContext,"Selected: $selectedDate",Toast.LENGTH_LONG).show()

            // Navigate to the ShiftDetailsActivity to display the selected date's details
            //val intent = Intent(this, ViewShiftsActivity::class.java)
            //intent.putExtra("date", selectedDate)
            //startActivity(intent)
        }

}}