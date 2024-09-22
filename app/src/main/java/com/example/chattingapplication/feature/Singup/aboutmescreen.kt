package com.example.chattingapplication.feature.Singup


import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.OutlinedTextField
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import com.example.chattingapplication.R
import com.example.chattingapplication.feature.ReusableCardLayout
import com.example.chattingapplication.feature.navigation.NavigationRouts
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun AboutMeScreen(navController: NavController) {
    var aboutMe by remember { mutableStateOf("") }
    var isSubmitting by remember { mutableStateOf(false) }
    var showErrorDialog by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf("") }

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Top image from Figma
            Image(
                painter = painterResource(id = R.drawable.girlprofile), // Update with your image resource
                contentDescription = "Profile Image",
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp) // Adjust based on design
            )

            Text(
                text = "Tell Us About Yourself",
                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.padding(bottom = 8.dp)
            )

            // TextBox for "About Me"
            OutlinedTextField(
                value = aboutMe,
                onValueChange = {
                    if (it.length <= 200) { // Limit to 200 characters
                        aboutMe = it
                    }
                },
                label = { Text("About Me (max 200 characters)") },
                modifier = Modifier.fillMaxWidth(),
                singleLine = false, // Allow multiple lines
                maxLines = 5, // Limit to 5 lines for better UI
                placeholder = { Text("Tell us about yourself...") }
            )

            // Character count display
            Text(
                text = "${aboutMe.length}/200",
                style = MaterialTheme.typography.bodyMedium,
                color = if (aboutMe.length > 200) Color.Red else Color.Gray,
                modifier = Modifier.align(Alignment.End) // Align count to the end
            )

            // Progress indicator while submitting
            if (isSubmitting) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )
            } else {
                Button(
                    onClick = {
                        isSubmitting = true
                        updateUserAboutMe(aboutMe) { success ->
                            isSubmitting = false // Reset submitting state
                            if (success) {
                                navController.navigate(NavigationRouts.MapScreen.route)
                            } else {
                                errorMessage = "Failed to update. Please try again."
                                showErrorDialog = true
                            }
                        }
                    },
                    modifier = Modifier
                        .align(Alignment.End)
                        .padding(top = 8.dp),
                    colors = ButtonDefaults.buttonColors(
                        backgroundColor = Color(0xFFFF6C00), // Match the color theme
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(12.dp),
                    elevation = ButtonDefaults.elevation(4.dp)
                ) {
                    Text("Submit", style = MaterialTheme.typography.labelLarge)
                }

            }

            // Error dialog for submission failure
            if (showErrorDialog) {
                AlertDialog(
                    onDismissRequest = { showErrorDialog = false },
                    title = { Text("Error") },
                    text = { Text(errorMessage) },
                    confirmButton = {
                        Button(onClick = { showErrorDialog = false }) {
                            Text("OK")
                        }
                    }
                )
            }
        }
    }
}

// Function to update About Me text in Firestore in the existing user document
private fun updateUserAboutMe(aboutMe: String, callback: (Boolean) -> Unit) {
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return callback(false)
    val db = FirebaseFirestore.getInstance()

    val userDataUpdates = hashMapOf<String, Any>(
        "aboutMe" to aboutMe
    )

    db.collection("users").document(userId).update(userDataUpdates)
        .addOnSuccessListener {
            callback(true) // Successfully updated data
        }
        .addOnFailureListener { e ->
            e.printStackTrace()
            callback(false) // Failed to update data
        }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
fun AboutMePreview() {
    AboutMeScreen(navController = rememberNavController())
}
