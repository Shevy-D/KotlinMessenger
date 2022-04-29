package com.shevy.kotlinmessenger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val register = findViewById<Button>(R.id.register_button_register)
        val alreadyReg = findViewById<TextView>(R.id.already_have_account_textView)

        register.setOnClickListener {
            val email = findViewById<EditText>(R.id.email_edittext_register).text.toString()
            val password = findViewById<EditText>(R.id.password_edittext_register).text.toString()

            Log.d("MainAct", "Email is: $email")
            Log.d("MainAct", "Password is: $password")
        }

        alreadyReg.setOnClickListener {
            Log.d("MainAct", "Try to show login activity")

            // launch the login activity somehow
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}