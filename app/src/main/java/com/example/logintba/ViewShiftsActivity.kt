package com.example.logintba

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
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

    fun AddShift(shift: String?) {
        val user = FirebaseAuth.getInstance().currentUser?.uid
        val dateReference = database.getReference("dates").child(user.toString())

        val builder = AlertDialog.Builder(this)
        //builder.setMessage("Adding a new Shift")

        builder.setTitle("Do you want to add this Shift to your Schedule?")
        builder.setCancelable(false)
        builder.setPositiveButton("Yes") {
                dialog, which ->
            //Push the date to the Database
            val dateId = dateReference.push().key
            dateId?.let {
                dateReference.child(it).setValue(shift)
                //dateReference.child(it).child(it).setValue(user)
                Toast.makeText(this, "Date sent successfully", Toast.LENGTH_SHORT).show()
            // Create an Intent object that targets the next form's activity class
            val intent = Intent(this, MainShifts::class.java)
            // Use the startActivity method to start the next form's activity
            startActivity(intent)
            finish()
        }}

        builder.setNegativeButton("No") {
                dialog, which -> dialog.cancel()
        }

        val alertDialog = builder.create()
        alertDialog.show()
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_view_shifts)

        dateListView = findViewById(R.id.dateListView)
        btnGoBack = findViewById(R.id.btnGoBack)
        database = FirebaseDatabase.getInstance()

        // Read dates from Firebase and display in ListView

        val user = FirebaseAuth.getInstance().currentUser?.uid
        val dateReference = database.getReference("dates")
        //val dateReference2 = database.getReference("dates").child(user.toString())

        dateReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val dates = mutableListOf<String>()
                for (dateSnapshot in snapshot.children) {
                    if (dateSnapshot.key != FirebaseAuth.getInstance().currentUser?.uid) {
                        for (date2Snapshot in dateSnapshot.children) {
                            val date = date2Snapshot.value as String
                            dates.add(date)
                        }
                    }
                }

                // Display dates in the ListView
                val adapter = ArrayAdapter(
                    this@ViewShiftsActivity,
                    android.R.layout.simple_list_item_1,
                    dates
                )
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

            //DateTimeShift = selectedDate
            AddShift(selectedDate)
            //Toast.makeText(applicationContext, "Selected: $selectedDate", Toast.LENGTH_LONG).show()

            // Navigate to the ShiftDetailsActivity to display the selected date's details
            //val intent = Intent(this, ViewShiftsActivity::class.java)
            //intent.putExtra("date", selectedDate)
            //startActivity(intent)
        }
    }
}