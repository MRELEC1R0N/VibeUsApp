package com.example.chattingapplication.feature.chat

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Scaffold
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.asFlow
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.chattingapplication.R
import com.example.chattingapplication.User
import com.example.chattingapplication.UserViewModel
import com.google.firebase.auth.FirebaseAuth
import com.example.chattingapplication.Message
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MessageScreen(
    userId: String,
    onBackClick: () -> Unit
) {
    val viewModel: UserViewModel = viewModel()
    val user by viewModel.user.asFlow().collectAsState(initial = null)
    var messageText by remember { mutableStateOf("") }
    val currentUserId = FirebaseAuth.getInstance().currentUser?.uid ?: "" // Get current user ID

    LaunchedEffect(userId) {
        viewModel.getUserById(userId)
    }

    // Generate chat ID for this conversation
    val chatId = generateChatId(currentUserId, userId)

    androidx.compose.material3.Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Image(
                            painter = painterResource(id = R.drawable.profile_picture),
                            contentDescription = "Profile Picture",
                            modifier = Modifier.size(30.dp).clip(CircleShape)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(text = user?.username ?: "")
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        },
        bottomBar = {
            Row(
                modifier = Modifier.fillMaxWidth().padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TextField(
                    value = messageText,
                    onValueChange = { messageText = it },
                    placeholder = { Text("Enter message") },
                    modifier = Modifier.weight(1f)
                )
                Spacer(modifier = Modifier.width(8.dp))
                IconButton(onClick = {
                    if (messageText.isNotBlank()) {
                        sendMessage(currentUserId, userId, messageText, chatId)
                        messageText = "" // Clear the input field after sending
                    }
                }) {
                    Icon(Icons.Filled.Send, contentDescription = "Send")
                }
            }
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(innerPadding)
        ) {
            // Call ChatScreen to display messages
            ChatScreen(chatId, currentUserId) { senderId, receiverId ->
                sendMessage(senderId, receiverId, messageText, chatId)
            }
        }
    }
}

// Function to generate a unique chat ID
private fun generateChatId(userId1: String, userId2: String): String {
    val userIds = listOf(userId1, userId2).sorted()
    return "${userIds[0]}_${userIds[1]}"
}

private fun sendMessage(senderId: String, receiverId: String, messageText: String, chatId: String) {
    val databaseReference = FirebaseDatabase.getInstance().getReference("messages").child(chatId)

    // Create the message object
    val message = Message(senderId, receiverId, messageText, System.currentTimeMillis())

    // Check if chat exists and send the message
    databaseReference.addListenerForSingleValueEvent(object : ValueEventListener {
        override fun onDataChange(snapshot: DataSnapshot) {
            if (!snapshot.exists()) {
                // Create new chat if it doesn't exist
                val membersMap = mapOf(senderId to true, receiverId to true)
                databaseReference.setValue(mapOf("members" to membersMap))
            }

            // Send the message under the chat ID
            databaseReference.child("messages").push().setValue(message)
                .addOnSuccessListener {
                    // Message sent successfully
                }
                .addOnFailureListener { error ->
                    // Handle error while sending the message
                    Log.e("MessageScreen", "Error sending message: ${error.message}")
                }
        }

        override fun onCancelled(error: DatabaseError) {
            // Handle error when checking for existing chat
            Log.e("MessageScreen", "Error checking chat existence: ${error.message}")
        }
    })
}
