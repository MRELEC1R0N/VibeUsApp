package com.example.chattingapplication.feature.Singup

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview

import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import com.example.chattingapplication.Movie
import com.example.chattingapplication.R
import com.example.chattingapplication.feature.ReusableCardLayout
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import okhttp3.Request
import org.json.JSONObject // For parsing JSON responses
import java.io.IOException

@Composable
fun MovieSearchScreen(navController: NavController) {
    var query by remember { mutableStateOf("") }
    var movies by remember { mutableStateOf<List<Movie>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }
    var selectedMovies by remember { mutableStateOf<List<Movie>>(emptyList()) }
    var feedbackMessage by remember { mutableStateOf("") } // State for feedback message

    ReusableCardLayout(
        imageResource = R.drawable.boyprofile,
        imageHeightFraction = 0.6f,
        cardHeightFraction = 0.6f
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                label = { Text("Search for a movie") },
                modifier = Modifier.fillMaxWidth()
            )

            Button(onClick = {
                isLoading = true
                fetchMovies(query) { fetchedMovies ->
                    movies = fetchedMovies
                    isLoading = false
                }
            }) {
                Text("Search")
            }

            if (isLoading) {
                CircularProgressIndicator()
            }

            if (feedbackMessage.isNotEmpty()) {
                Text(feedbackMessage, color = MaterialTheme.colorScheme.primary, modifier = Modifier.padding(8.dp))
            }

            SelectedMoviesDisplay(selectedMovies) { movieToRemove ->
                selectedMovies = selectedMovies.filterNot { it == movieToRemove }
                feedbackMessage = "${movieToRemove.title} removed from favorites"
            }

            LazyColumn {
                items(movies) { movie ->
                    ResponsiveMovieCard(
                        movie = movie,
                        onPosterClick = { selectedMovie ->
                            if (selectedMovies.size < 5) {
                                selectedMovies = selectedMovies + selectedMovie
                                storeMoviePoster(selectedMovie.posterUrl)
                                feedbackMessage = "${selectedMovie.title} added to favorites"
                            } else {
                                feedbackMessage = "You can't add more than 5 movies"
                            }
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun SelectedMoviesDisplay(selectedMovies: List<Movie>, onRemove: (Movie) -> Unit) {
    if (selectedMovies.isNotEmpty()) {
        LazyRow(modifier = Modifier.padding(vertical = 8.dp)) {
            items(selectedMovies) { movie ->
                Card(
                    modifier = Modifier.padding(4.dp),
                    elevation = CardDefaults.cardElevation(4.dp),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(8.dp)
                            .clickable { onRemove(movie) }
                    ) {
                        Text(movie.title, style = MaterialTheme.typography.bodyMedium)
                        Spacer(modifier = Modifier.width(8.dp))
                        Icon(Icons.Default.Close, contentDescription = "Remove")
                    }
                }
            }
        }
    }
}

@Composable
fun ResponsiveMovieCard(
    movie: Movie,
    onPosterClick: (Movie) -> Unit
) {
    BoxWithConstraints {
        val isCompact = maxWidth < 600.dp

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            elevation = CardDefaults.cardElevation(4.dp),
            shape = RoundedCornerShape(8.dp),
        ) {
            Row(
                modifier = Modifier
                    .padding(16.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                AsyncImage(
                    model = movie.posterUrl,
                    contentDescription = null,
                    modifier = Modifier
                        .size(if (isCompact) 80.dp else 100.dp)
                        .clickable { onPosterClick(movie) },
                    contentScale = ContentScale.Fit
                )
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(text = movie.title, style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}


private fun fetchMovies(query: String, callback: (List<Movie>) -> Unit) {
    val apiKey = "your_api_key" // Make sure to replace with your actual API key
    val client = OkHttpClient()
    val url = "https://api.themoviedb.org/3/search/movie?api_key=$apiKey&query=$query"

    val request = Request.Builder()
        .url(url)
        .build()

    client.newCall(request).enqueue(object : okhttp3.Callback {
        override fun onFailure(call: okhttp3.Call, e: IOException) {
            e.printStackTrace()
            callback(emptyList())
        }

        override fun onResponse(call: okhttp3.Call, response: okhttp3.Response) {
            response.use {
                if (!it.isSuccessful) throw IOException("Unexpected code $it")

                val jsonResponse = it.body?.string()
                val jsonObject = JSONObject(jsonResponse)
                val results = jsonObject.getJSONArray("results")

                val movies = mutableListOf<Movie>()
                for (i in 0 until results.length()) {
                    val movieJson = results.getJSONObject(i)
                    val title = movieJson.getString("title")
                    val posterPath = movieJson.getString("poster_path")
                    val posterUrl = "https://image.tmdb.org/t/p/w500$posterPath"

                    movies.add(Movie(title, posterUrl))
                }
                callback(movies)
            }
        }
    })
}

private fun storeMoviePoster(posterUrl: String) {
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: return
    val db = FirebaseFirestore.getInstance()

    db.collection("users").document(userId)
        .update("favoriteMovies", FieldValue.arrayUnion(posterUrl))
        .addOnSuccessListener {
            Log.d("Firestore", "Movie poster URL added successfully.")
        }
        .addOnFailureListener { e ->
            e.printStackTrace()
            Log.e("Firestore", "Error adding movie poster URL: ${e.message}")
        }
}




@Preview
@Composable
fun PreviewMovieSearchScreen() {
    MovieSearchScreen(navController = rememberNavController())
}
