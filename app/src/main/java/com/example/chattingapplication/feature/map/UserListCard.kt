package com.example.chattingapplication.feature.map

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.chattingapplication.User
import com.example.chattingapplication.feature.navigation.NavigationRouts as Screen

@Composable
fun UserListCard(
    users: List<User>,
    onClose: () -> Unit,
    navController: NavHostController // Add NavHostController parameter
) {
    var currentUserIndex by remember { mutableStateOf(0) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(modifier = Modifier.fillMaxWidth( ) ,
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically ) {
                Text( text = "Registered Users",
                    style = MaterialTheme.typography. titleMedium,
                    fontWeight = FontWeight.Bold )
                IconButton(onClick = onClose) {
                    Icon( imageVector = Icons.Filled.Close,
                        contentDescription = "Close" ) } }
            Divider(modifier = Modifier.padding(vertical = 8.dp))
            if (users.isEmpty()) { Text("No registered users found.") } else {
                val currentUser = users[currentUserIndex]
                UserItem(user = currentUser, onUserClick = { user ->
                    navController.navigate(Screen.UserInfoScreen.passUserId(user.userId))
                })

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 8.dp),horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    IconButton(
                        onClick = { currentUserIndex = (currentUserIndex - 1).coerceAtLeast(0) },
                        enabled = currentUserIndex > 0
                    ) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Previous")
                    }

                    IconButton(
                        onClick = { currentUserIndex = (currentUserIndex + 1).coerceAtMost(users.size - 1) },
                        enabled = currentUserIndex < users.size - 1
                    ) {
                        Icon(Icons.Filled.ArrowForward, contentDescription = "Next")
                    }
                }
            }
        }
    }
}


@Composable
fun UserItem(user: User, onUserClick: (User) -> Unit) {
    Column(modifier = Modifier
        .clickable { onUserClick(user) }
        .padding(vertical = 4.dp)
    ){
        Text(
            text = user.username,
            style = MaterialTheme.typography.bodyMedium,
            fontWeight = FontWeight.SemiBold
        )
        Text(text = "Email: ${user.email}")
    }
}