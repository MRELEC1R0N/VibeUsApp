package com.example.chattingapplication.feature.ProfilePage

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Edit
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import com.example.chattingapplication.R
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.chattingapplication.UserData
import com.google.firebase.auth.FirebaseAuth

@Composable
fun HomeScreen(navController: NavController) {
    val personalProfileViewModel: PersonalProfileViewModel = viewModel()
    val userId = FirebaseAuth.getInstance().currentUser?.uid // Get current user ID

    // Fetch user data
    userId?.let {
        personalProfileViewModel.fetchUserData(it)
    }

    // Observe user data
    val userData by personalProfileViewModel.userData.observeAsState()

    Scaffold(
        topBar = {
            // TopAppBar code remains unchanged
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .padding(innerPadding)
                .fillMaxSize()
        ) {
            item {
                userData?.let { user ->
                    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
                    val userImageHeight = screenHeight * 0.6f

                    Image(
                        painter = painterResource(id = R.drawable.boyprofile),
                        contentDescription = "User Image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(userImageHeight)
                            .clip(RoundedCornerShape(bottomStart = 30.dp, bottomEnd = 30.dp)),
                        contentScale = ContentScale.Crop
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    Divider(color = Color.Gray, thickness = 1.dp)

                    // User details card
                    UserDetailsCard(user)

                    // More about me card
                    MoreAboutMeCard(user)

                    Spacer(modifier = Modifier.height(16.dp))
                } ?: run {
                    // Show loading or placeholder UI while fetching data
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) { CircularProgressIndicator()
                    }                }

                // My binge list card
                Text(text = "My binge list", style = MaterialTheme.typography.h6)
                Spacer(modifier = Modifier.height(8.dp))

                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    items(3) {
                        Card(
                            modifier = Modifier
                                .size(150.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            elevation = 4.dp
                        ) {
                            // Example image card
                            Image(
                                painter = painterResource(id = R.drawable.profile_picture),
                                contentDescription = "Movie",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }

                    // Add more (+) card
                    item {
                        Card(
                            modifier = Modifier
                                .size(150.dp)
                                .clip(RoundedCornerShape(8.dp)),
                            backgroundColor = Color.White,
                            elevation = 4.dp
                        ) {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    contentDescription = "Add More",
                                    tint = Color.Blue,
                                    modifier = Modifier.size(40.dp)
                                )
                            }
                        }
                    }
                }

                // My playlist card
                Text(text = "My playlist", style = MaterialTheme.typography.h6)
                Spacer(modifier = Modifier.height(8.dp))

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Example artist cards
                    ArtistCard(artistName = "A.R. Rahman")
                    ArtistCard(artistName = "Sonu Nigam")
                    ArtistCard(artistName = "Pritam")

                    // Add artist card
                    AddArtistCard()
                }

                // Written prompts card
                WrittenPromptsCard(onPenClick = {
                    // TODO: Handle the pen icon click event here
                })
            }
        }
    }
}




@Composable
fun UserDetailsCard(user: UserData?) {
    user?.let {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            elevation = 4.dp,
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text(text = "Basic details", style = MaterialTheme.typography.h6)

                Spacer(modifier = Modifier.height(16.dp))

                // Detail Rows
                DetailRow(label = "Name", value = it.name ?: "N/A")
                DetailRow(label = "Gender", value = it.gender ?: "N/A")
                DetailRow(label = "Pronouns", value = it.pronouns ?: "N/A")
                DetailRow(label = "Work", value = it.work ?: "N/A")
                DetailRow(label = "College", value = it.college ?: "N/A")
                DetailRow(label = "Hometown", value = it.hometown ?: "N/A")
                DetailRow(label = "Languages I know", value = (it.languages ?: "N/A").toString())
            }
        }
    }
}

@Composable
fun MoreAboutMeCard(user: UserData?) {
    user?.let {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            elevation = 4.dp,
            shape = RoundedCornerShape(16.dp)
        ) {
            Column(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth()
            ) {
                Text(text = "More about me", style = MaterialTheme.typography.h6)

                Spacer(modifier = Modifier.height(16.dp))

                // More about me Rows
                DetailRow(label = "Dating Intention", value = it.datingIntention ?: "N/A")
                DetailRow(label = "Religious Beliefs", value = it.religiousBeliefs ?: "N/A")
                DetailRow(label = "Height", value = it.height ?: "N/A")
                DetailRow(label = "Drinking", value = it.drinking ?: "N/A")
                DetailRow(label = "Smoking", value = it.smoking ?: "N/A")
            }
        }
    }
}


@Composable
fun DetailRow(label: String, value: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = label, style = MaterialTheme.typography.body2)
        Text(text = value, style = MaterialTheme.typography.body1, fontWeight = FontWeight.Bold)
    }
}



@Composable
fun WrittenPromptsCard(onPenClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        elevation = 4.dp,
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth()
        ) {
            Text(
                text = "Written prompts",
                style = MaterialTheme.typography.h6
            )

            Spacer(modifier = Modifier.height(4.dp))

            Text(
                text = "Stand out with a bio or some witty responses",
                style = MaterialTheme.typography.body2,
                color = Color.Gray
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Prompt 1
            PromptRow(
                question = "My love language is",
                answer = "Travelling ✈️",
                onPenClick = onPenClick
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Prompt 2
            PromptRow(
                question = "If I had a million dollars",
                answer = "I would buy ford mustang boss 429",
                onPenClick = onPenClick
            )
        }
    }
}

@Composable
fun PromptRow(question: String, answer: String, onPenClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(text = question, style = MaterialTheme.typography.body2)
            Text(text = answer, style = MaterialTheme.typography.body1, fontWeight = FontWeight.Bold)
        }

        // Pen Icon
        IconButton(onClick = onPenClick) {
            Icon(
                imageVector = Icons.Default.Edit, // Using default Edit icon as a placeholder for pen icon
                contentDescription = "Edit",
                tint = Color.Blue // Blue tint for the pen icon
            )
        }
    }
}



@Composable
fun ArtistCard(artistName: String) {
    Card(
        modifier = Modifier
            .width(100.dp)
            .height(50.dp),
        shape = RoundedCornerShape(16.dp),
        elevation = 4.dp
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = artistName, style = MaterialTheme.typography.body1)
        }
    }
}

@Composable
fun AddArtistCard() {
    Card(
        modifier = Modifier
            .width(100.dp)
            .height(50.dp),
        shape = RoundedCornerShape(16.dp),
        backgroundColor = Color.White,
        elevation = 4.dp
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = "Add Artist",
                tint = Color.Blue,
                modifier = Modifier.size(30.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(text = "Add artist", style = MaterialTheme.typography.body2)
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewHomeScreen() {
    val navController = rememberNavController()
    HomeScreen(navController = navController)
}


