package com.example.chattingapplication.feature.chat

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.chattingapplication.Message
import com.google.firebase.database.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow


class ChatViewModel(private val chatId: String) : ViewModel() {
    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages = _messages.asStateFlow()

    private val databaseReference = FirebaseDatabase.getInstance().getReference("messages").child(chatId)

    init {
        fetchMessages()
    }

    private fun fetchMessages() {
        // Assuming databaseReference is already pointing to the correct chatId in Message node.
        val messagesRef = databaseReference.child("messages")

        messagesRef.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val messageList = mutableListOf<Message>()
                for (snapshot in dataSnapshot.children) {
                    // Log raw snapshot data for debugging
                    Log.d("ChatViewModel", "Snapshot data: ${snapshot.value}")

                    // Deserialize each message
                    val message = snapshot.getValue(Message::class.java)
                    if (message != null) {
                        messageList.add(message)
                    } else {
                        Log.e("ChatViewModel", "Message deserialization failed for snapshot: $snapshot")
                    }
                }
                _messages.value = messageList
                Log.d("ChatViewModel", "Fetched messages: $messageList")
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.e("ChatViewModel", "Error fetching messages: ${databaseError.message}")
            }
        })
    }
}