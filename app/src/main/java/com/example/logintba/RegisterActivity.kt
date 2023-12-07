package com.example.logintba

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser

class RegisterActivity : AppCompatActivity() {
    private lateinit var firebaseAuth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        firebaseAuth = FirebaseAuth.getInstance()


        val tvLogin : TextView = findViewById(R.id.tvLogin)
        val btnRegister : Button = findViewById(R.id.btnRegister)


        btnRegister.setOnClickListener {
            val editEmail : EditText = findViewById(R.id.editEmail)
            val editPassword : EditText = findViewById(R.id.editPassword)
            val editCPassword : EditText = findViewById(R.id.editCPassword)


            if(editEmail.text.isNotEmpty() && editCPassword.text.isNotEmpty() && editPassword.text.isNotEmpty()){
                //Toast.makeText(this,"Input Provided", Toast.LENGTH_LONG).show()
                //registerUser();
                firebaseAuth.createUserWithEmailAndPassword(editEmail.text.toString(), editPassword.text.toString()).addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Toast.makeText(this,"Register successful",Toast.LENGTH_LONG).show()
                        val currentUser: FirebaseUser = firebaseAuth.currentUser!!
                        val user = firebaseAuth.currentUser
                        Toast.makeText(this,"Registered User: $user",Toast.LENGTH_LONG).show()

                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "createUserWithEmail:failure", task.exception)
                        Toast.makeText(this,"Register failed "+task.exception,Toast.LENGTH_LONG).show()
                    }
                }
            }else{
                Toast.makeText(this,"Input Required", Toast.LENGTH_LONG).show()
            }
        }

        tvLogin.setOnClickListener{
            val intent = Intent (this, MainActivity::class.java)
            startActivity(intent)
        }
    }

    public override fun onStart() {
        super.onStart()
        // Check if user is signed in (non-null) and update UI accordingly.
        val currentUser: FirebaseUser = firebaseAuth.currentUser!!
    }


}