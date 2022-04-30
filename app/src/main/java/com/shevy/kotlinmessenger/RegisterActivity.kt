package com.shevy.kotlinmessenger

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.View
import android.widget.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import de.hdodenhof.circleimageview.CircleImageView
import java.util.*

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        val register = findViewById<Button>(R.id.register_button_register)
        val alreadyReg = findViewById<TextView>(R.id.already_have_account_textView)
        val selectPhoto = findViewById<ImageButton>(R.id.selectphoto_button_register)

        register.setOnClickListener {
            performRegister()
        }

        alreadyReg.setOnClickListener {
            Log.d("RegisterAct", "Try to show login activity")

            // launch the login activity somehow
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        selectPhoto.setOnClickListener {
            Log.d("RegisterAct", "Try to show photo selector")

            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
        }
    }

    var selectedPhotoUri: Uri? = null

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0 && resultCode == Activity.RESULT_OK && data != null) {
            //proceed and check what the selected image was...
            Log.d("RegisterAct", "Photo was selected")

            selectedPhotoUri = data.data

            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)

            //val bitmapDrawable = BitmapDrawable(bitmap)
            //val selectPhoto = findViewById<Button>(R.id.selectphoto_button_register)
            //findViewById<ImageButton>(R.id.selectphoto_button_register).setBackgroundDrawable(bitmapDrawable)
            findViewById<CircleImageView>(R.id.selectphoto_imageview_register).setImageBitmap(bitmap)
            findViewById<TextView>(R.id.select_photo_textView).visibility = View.INVISIBLE
        }
    }

    private fun performRegister() {
        val email = findViewById<EditText>(R.id.email_edittext_register).text.toString()
        val password = findViewById<EditText>(R.id.password_edittext_register).text.toString()

        Log.d("RegisterAct", "Email is: $email")
        Log.d("RegisterAct", "Password is: $password")

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Please enter text in email/password", Toast.LENGTH_LONG)
                .show()
            return
        }

        Firebase.auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) {
                if (!it.isSuccessful) return@addOnCompleteListener

                //else if successful
                Log.d("Register", "createUserWithEmail:success uid: ${it.result.user?.uid}")

                uploadImageToFirebaseStorage()
            }
            .addOnFailureListener {
                Log.w("Register", "Failed to create user: ${it.message}")
                Toast.makeText(this, "Failed to create user: ${it.message}", Toast.LENGTH_LONG)
                    .show()
            }
    }

    private fun uploadImageToFirebaseStorage() {
        if (selectedPhotoUri == null) return

        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/image/$filename")

        ref.putFile(selectedPhotoUri!!)
            .addOnSuccessListener {
                Log.w("Register", "Successfully uploaded image: ${it.metadata?.path}")

                ref.downloadUrl.addOnSuccessListener {
                    Log.w("Register", "File location: $it")

                    saveUserToFirebaseDatabase(it.toString())
                }
            }
    }

    private fun saveUserToFirebaseDatabase(profileImageUrl: String) {
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val database = Firebase.database.reference
        //val ref = database.child("/users/").child("$uid")

        val user = User(uid,
            findViewById<EditText>(R.id.username_edittext_register).text.toString(),
            profileImageUrl)

        database.child("users").child("$uid").setValue(user)
            .addOnSuccessListener {
                Log.d("Register", "Finally we saves the user to Firebase Database")
            }
    }
}

class User(val uid: String, val username: String, val profileImageUrl: String)