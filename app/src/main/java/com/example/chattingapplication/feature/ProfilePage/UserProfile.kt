package com.example.chattingapplication.feature.ProfilePage

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Face
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.chattingapplication.R
import com.example.chattingapplication.User
import com.example.chattingapplication.UserViewModel
import com.example.chattingapplication.feature.navigation.NavigationRouts
import androidx.lifecycle.asFlow
import androidx.navigation.compose.rememberNavController

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserInfoScreen(
    userId: String,
    onBackClick: () -> Unit,
    navController: NavController,
) {
    val viewModel: UserViewModel = viewModel()
    val user by viewModel.user.observeAsState(initial = null)

    LaunchedEffect(userId) {
        viewModel.getUserById(userId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Profile", textAlign = TextAlign.Center) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(innerPadding)
                .padding(32.dp)
        ) {
            // User Image
            item {
                Image(
                    painter = painterResource(id = R.drawable.profile_picture),
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(3f / 4f)
                        .clip(RoundedCornerShape(16.dp)),
                    contentScale = ContentScale.Crop
                )
            }

            // About Me Section
            item {
                Spacer(modifier = Modifier.height(32.dp))
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp), // Set a height similar to the image
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "About Me",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = Color.Black
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = user?.aboutMe ?: "This user hasn't provided an About Me yet.",
                            style = MaterialTheme.typography.bodyMedium,
                            color = Color.Black
                        )
                    }
                }
            }

            // Languages Section
            item {
                Spacer(modifier = Modifier.height(32.dp))
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Filled.Face,
                                contentDescription = "Languages Icon",
                                modifier = Modifier.size(24.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Languages I Know",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = Color.Black
                            )
                        }
                        Spacer(modifier = Modifier.height(8.dp))
                        Row {
                            Text(
                                text = "Hindi, English, Punjabi", // Replace with actual user languages
                                style = MaterialTheme.typography.bodyLarge.copy(fontWeight = FontWeight.Bold),
                                color = Color.Black
                            )
                        }
                    }
                }
            }

            // My Watch List Section
            item {
                Spacer(modifier = Modifier.height(32.dp))
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "My Watch List",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = Color.Black
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Horizontal LazyRow for movie posters
                        LazyRow(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            items(5) { index -> // Replace with actual movie data
                                MoviePosterItem(index = index)
                            }
                        }
                    }
                }
            }

            // My Favorite Places Section
            item {
                Spacer(modifier = Modifier.height(32.dp))
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .wrapContentHeight(),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp)
                    ) {
                        Text(
                            text = "My Favorite Places to Visit",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = Color.Black
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Horizontal LazyRow for favorite places
                        LazyRow(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            items(5) { index -> // Replace with actual favorite places data
                                PlaceItem(index = index)
                            }
                        }
                    }
                }
            }

            // My Playlist Section
            item {
                Spacer(modifier = Modifier.height(32.dp))
                // My Playlist Card (Smaller than other cards)
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp), // Smaller height for the playlist card
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    shape = RoundedCornerShape(16.dp)
                ) {
                    Column(
                        modifier = Modifier.padding(8.dp)
                    ) {
                        Text(
                            text = "My Playlist",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = Color.Black
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        // Horizontal LazyRow for singer profile pictures
                        LazyRow(
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            items(5) { index -> // Replace with your list of singer data
                                SingerItem(index = index)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun MoviePosterItem(index: Int) {

    val moviePoster = R.drawable.girlprofile
    val movieTitle = "Movie ${index + 1}"

    Column(
        modifier = Modifier
            .padding(end = 16.dp)
            .width(100.dp)
    ) {
        Image(
            painter = painterResource(id = moviePoster),
            contentDescription = movieTitle,
            modifier = Modifier
                .height(140.dp) // Height for the movie poster
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )
        Text(
            text = movieTitle,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(top = 4.dp),
            textAlign = TextAlign.Start
        )
    }
}

@Composable
fun PlaceItem(index: Int) {
    // Sample place image (replace with actual image loading)
    val placeImage = R.drawable.boyprofile // Replace with your image resource
    val placeName = "Place ${index + 1}" // Sample place name, replace with actual data

    Column(
        modifier = Modifier
            .padding(end = 16.dp)
            .width(100.dp) // Adjust width as needed
    ) {
        Image(
            painter = painterResource(id = placeImage),
            contentDescription = placeName,
            modifier = Modifier
                .height(140.dp) // Height for the place image
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )
        Text(
            text = placeName,
            style = MaterialTheme.typography.bodySmall,
            modifier = Modifier.padding(top = 4.dp),
            textAlign = TextAlign.Start
        )
    }
}

@Composable
fun SingerItem(index: Int) {
    // Sample singer image (replace with actual image loading)
    val singerImage = R.drawable.profile_picture // Replace with your image resource
    val singerName = "Singer ${index + 1}" // Sample singer name, replace with actual data

    Card(
        modifier = Modifier
            .padding(end = 8.dp) // Smaller padding for the individual singer card
            .width(60.dp) // Smaller width for the singer card
            .height(80.dp), // Height for the singer card
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp), // Slight elevation for individual cards
        shape = RoundedCornerShape(8.dp)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Image(
                painter = painterResource(id = singerImage),
                contentDescription = singerName,
                modifier = Modifier
                    .fillMaxHeight(0.7f) // Height for the singer profile picture
                    .clip(CircleShape),
                contentScale = ContentScale.Crop
            )
            Text(
                text = singerName,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(top = 4.dp),
                textAlign = TextAlign.Center
            )
        }
    }
}


@Preview()
@Composable
fun PreviewUserInfoScreen() {
    // Mock NavController and other required parameters for preview
    val navController = rememberNavController()

    UserInfoScreen(
        userId = "1",  // Example userId for preview
        onBackClick = { /* Do nothing in preview */ },
        navController = navController
    )
}
