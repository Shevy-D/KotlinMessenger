package com.shevy.kotlinmessenger

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity: AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val backReg = findViewById<TextView>(R.id.back_to_register)

        val loginBtn = findViewById<Button>(R.id.login_button_login)

        loginBtn.setOnClickListener {
            val email = findViewById<EditText>(R.id.email_edittext_login).text.toString()
            val password = findViewById<EditText>(R.id.password_edittext_login).text.toString()
            Log.d("MainAct", "Attempt login email/password: $email/***")

            Firebase.auth.signInWithEmailAndPassword(email, password)
                //.addOnCompleteListener()
        }

        backReg.setOnClickListener {
            // launch the login activity somehow
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }

    }
}