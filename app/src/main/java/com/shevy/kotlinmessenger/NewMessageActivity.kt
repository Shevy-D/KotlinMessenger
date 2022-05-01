package com.shevy.kotlinmessenger

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import de.hdodenhof.circleimageview.CircleImageView


class NewMessageActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_message)

        supportActionBar?.title = "Select User"

        fetchUsers()
    }

    private fun fetchUsers() {
        val ref = FirebaseDatabase.getInstance().getReference("/users/")
        ref.addListenerForSingleValueEvent(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                val adapter = GroupieAdapter()

                snapshot.children.forEach {
                    Log.d("NewMessage", "${it.toString()}")
                    val user = it.getValue(User::class.java)
                    if (user != null) {
                    adapter.add(UserItem(user))
                    }
                }
                val rcView = findViewById<RecyclerView>(R.id.recyclerview_newmessage)
                rcView.adapter = adapter
            }

            override fun onCancelled(error: DatabaseError) {
                TODO("Not yet implemented")
            }

        })
    }
}

class UserItem(private val user: User) : Item<GroupieViewHolder>() {

    override fun bind(viewHolder: GroupieViewHolder, p1: Int) {
        val name = viewHolder.itemView.findViewById<TextView>(R.id.textView_user_row_new_message)
        name.text = user.username

        val image = viewHolder.itemView.findViewById<CircleImageView>(R.id.image_user_row_new_message)
        Picasso.get().load(user.profileImageUrl).into(image)
    }

    override fun getLayout(): Int {
        return R.layout.user_row_new_message
    }
}
