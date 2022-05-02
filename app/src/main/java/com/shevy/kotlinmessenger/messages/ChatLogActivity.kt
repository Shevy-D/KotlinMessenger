package com.shevy.kotlinmessenger.messages

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.shevy.kotlinmessenger.R
import com.shevy.kotlinmessenger.models.User
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item

class ChatLogActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)

        //val username = intent.getStringExtra(NewMessageActivity.USER_KEY)
        val user = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)
        supportActionBar?.title = user?.username

        setupDummyData()
    }

    private fun setupDummyData() {
        val adapter = GroupieAdapter()
        val chatLog = findViewById<RecyclerView>(R.id.recyclerview_chat_log)

        adapter.add(ChatFromItem())
        adapter.add(ChatToItem())
        adapter.add(ChatFromItem())
        adapter.add(ChatToItem())

        chatLog.adapter = adapter
    }
}

class ChatFromItem: Item<GroupieViewHolder>() {
    override fun bind(p0: GroupieViewHolder, p1: Int) {

    }

    override fun getLayout(): Int {
        return R.layout.chat_from_row
    }
}

class ChatToItem: Item<GroupieViewHolder>() {
    override fun bind(p0: GroupieViewHolder, p1: Int) {

    }

    override fun getLayout(): Int {
        return R.layout.chat_to_row
    }
}