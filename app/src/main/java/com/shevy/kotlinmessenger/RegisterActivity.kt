package com.shevy.kotlinmessenger

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val register = findViewById<Button>(R.id.register_button_register)
        val alreadyReg = findViewById<TextView>(R.id.already_have_account_textView)
        val selectPhoto = findViewById<ImageButton>(R.id.selectphoto_button_register)

        register.setOnClickListener {
            performRegister()
        }

        alreadyReg.setOnClickListener {
            Log.d("MainAct", "Try to show login activity")

            // launch the login activity somehow
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        selectPhoto.setOnClickListener {
            Log.d("MainAct", "Try to show photo selector")

            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            //proceed and check what the selected image was...
            Log.d("RegisterAct", "Photo was selected")

            val uri = data.data

            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)

            val bitmapDrawable = BitmapDrawable(bitmap)
            //val selectPhoto = findViewById<Button>(R.id.selectphoto_button_register)
            findViewById<ImageButton>(R.id.selectphoto_button_register).setBackgroundDrawable(bitmapDrawable)
            findViewById<TextView>(R.id.select_photo_textView).visibility = View.INVISIBLE
        }
    }

    private fun performRegister() {
        val email = findViewById<EditText>(R.id.email_edittext_register).text.toString()
        val password = findViewById<EditText>(R.id.password_edittext_register).text.toString()

        Log.d("MainAct", "Email is: $email")
        Log.d("MainAct", "Password is: $password")

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter text in email/password", Toast.LENGTH_LONG)
                .show()
            return
        }

        Firebase.auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) {
                if (!it.isSuccessful) return@addOnCompleteListener

                //else if successful
                Log.d("Main", "createUserWithEmail:success uid: ${it.result.user?.uid}")
                }
            .addOnFailureListener {
                Log.w("Main", "Failed to create user: ${it.message}")
                Toast.makeText(this, "Failed to create user: ${it.message}", Toast.LENGTH_LONG)
                    .show()
            }
    }
}