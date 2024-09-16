package com.example.chattingapplication.feature.chat

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.chattingapplication.Message

@Composable
fun ChatScreen(
    chatId: String,
    currentUserId: String,
    onMessageSent: (String, String) -> Unit
) {
    val viewModel: ChatViewModel = viewModel(factory = ChatViewModelFactory(chatId))
    val messages by viewModel.messages.collectAsState()

    // Create a LazyListState to manage scrolling
    val lazyListState = rememberLazyListState()

    LaunchedEffect(messages) {
        // Scroll to the bottom when new messages are added
        if (messages.isNotEmpty()) {
            lazyListState.scrollToItem(0)
        }
    }

    LazyColumn(
        state = lazyListState,
        modifier = Modifier.fillMaxSize().padding(8.dp), // Add padding for better visibility
        verticalArrangement = Arrangement.Bottom, // Align items to the bottom
        reverseLayout = true // This will reverse the order of items
    ) {
        items(messages.reversed()) { message ->
            ChatBubble(message = message, isCurrentUser = message.senderId == currentUserId)
        }
    }
}

@Composable
fun ChatBubble(message: Message, isCurrentUser: Boolean) {
    Row(
        horizontalArrangement = if (isCurrentUser) Arrangement.End else Arrangement.Start,
        modifier = Modifier.fillMaxWidth()
    ) {
        Surface(
            color = if (isCurrentUser) Color.Blue else Color.Gray,
            shape = RoundedCornerShape(16.dp)
        ) {
            Text(
                text = message.messageText,
                color = Color.White,
                modifier = Modifier.padding(12.dp)
            )
        }
    }
}