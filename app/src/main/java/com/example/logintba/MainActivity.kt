package com.example.logintba

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

import androidx.appcompat.app.AlertDialog

import com.google.firebase.auth.FirebaseAuth

class MainActivity() : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        firebaseAuth = FirebaseAuth.getInstance()

        val btnLogin : Button = findViewById(R.id.btnLogin)
        val edUsername : EditText = findViewById(R.id.edUsername)
        val edPassword : EditText = findViewById(R.id.edPassword)
        val tvRegister : TextView = findViewById(R.id.tvRegister)

        fun message(user: String?) {
            val builder = AlertDialog.Builder(this)
            builder.setMessage("Successful session started")
            val username = user?.substringBefore("@")
            builder.setTitle("Welcome: $username. Do you want to see your shift times?")
            builder.setCancelable(false)
            builder.setPositiveButton("Yes") {
                    dialog, which ->
                    // Create an Intent object that targets the next form's activity class
                    val intent = Intent(this, MainShifts::class.java)
                    // Use the startActivity method to start the next form's activity
                    startActivity(intent)

                    finish()
            }

            builder.setNegativeButton("No") {
                    dialog, which -> dialog.cancel()
            }

            val alertDialog = builder.create()
            alertDialog.show()
        }

        btnLogin.setOnClickListener {
            val email = edUsername.text.toString().trim()
            val password = edPassword.text.toString().trim()
            val user = firebaseAuth.currentUser
            val emailInfo = firebaseAuth.currentUser?.email

            if (edUsername.text.trim().isNotEmpty() || edPassword.text.trim().isNotEmpty()){
                firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener{
                    if (it.isSuccessful){
                        Toast.makeText(this, "Success Sign-In", Toast.LENGTH_LONG).show()
                        //Toast.makeText(this, "Current User: $user", Toast.LENGTH_LONG).show()
                        message(emailInfo)
                    } else{
                        // If sign in fails, display a message to the user.
                        Toast.makeText(this, "Failure Sign-In", Toast.LENGTH_LONG).show()
                    }
                }
               //Toast.makeText(this, "Input provided", Toast.LENGTH_LONG).show()
            }else{
               Toast.makeText(this, "Input required", Toast.LENGTH_LONG).show()
            }
        }




        tvRegister.setOnClickListener{
            val intent = Intent (this, RegisterActivity::class.java);
            startActivity(intent)
        }
    }
}