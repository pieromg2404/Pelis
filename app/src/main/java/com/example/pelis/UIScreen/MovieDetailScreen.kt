package com.example.pelis.UIScreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberImagePainter
import com.example.pelis.Singleton.SelectedMovieSingleton

@Composable
fun MovieDetailScreen() {
    val selectedMovie = SelectedMovieSingleton.getSelectedMovie()

    selectedMovie?.let { movie ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = movie.title ?:"vacio", style = MaterialTheme.typography.headlineLarge)
            Spacer(modifier = Modifier.height(16.dp))
            Image(
                painter = rememberImagePainter("https://image.tmdb.org/t/p/w500${movie.poster_path?:"vacio"}"),
                contentDescription = null,
                modifier = Modifier
                    .width(120.dp)
                    .height(200.dp),
                contentScale = ContentScale.Crop
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "Rating: ${movie.vote_average?:"vacio"}", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Release Date: ${movie.release_date?:"vacio"}", color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "${movie.overview?:"vacio"}", maxLines = 5, overflow = TextOverflow.Ellipsis)
        }
    } ?: run {
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            CircularProgressIndicator()
        }
    }
}