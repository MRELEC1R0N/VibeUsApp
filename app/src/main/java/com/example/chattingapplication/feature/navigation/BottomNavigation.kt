package com.example.chattingapplication.feature.navigation

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.BottomNavigation
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material.Surface
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.chattingapplication.R
import com.example.chattingapplication.feature.navigation.NavigationRouts as Screen


@Composable
fun MyBottomNavigation(navController: NavController) {
    val bottomBarShape = RoundedCornerShape(
        topStart = 16.dp,
        topEnd = 16.dp,
        bottomStart = 16.dp,
        bottomEnd = 16.dp
    )
    val items = listOf(
        NavigationItem(label = "Profile", icon = R.drawable.profile_icon, route = Screen.HomeScreen.route),
        NavigationItem(label = "Map", icon = R.drawable.map_icon, route = Screen.MapScreen.route),
        NavigationItem(label = "Chat", icon = R.drawable.chat_icon, route = Screen.ChatScreen.route)
    )


    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .shadow(elevation = 16.dp, shape = bottomBarShape)
            .clip(bottomBarShape),
        color = Color.White
    ) {

        BottomNavigation { // This now refers to the Material library component
            items.forEach { item ->
                BottomNavigationItem(
                    icon = {
                        Icon(
                            painter = painterResource(id = item.icon),
                            contentDescription = item.label
                        )
                    },
                    label = { Text(text = item.label) },
                    selected = navController.currentDestination?.route == item.route,
                    onClick = { navController.navigate(item.route) }
                )
            }
        }
    }
}