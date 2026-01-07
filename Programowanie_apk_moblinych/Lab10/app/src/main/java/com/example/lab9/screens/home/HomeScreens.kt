package com.example.lab9.screens.home

import android.util.Log
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.lab9.models.Movie
import com.example.lab9.models.getMovies
import com.example.lab9.navigation.MovieScreens

private const val TAG = "HomeScreen"


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, movieList: List<Movie> = getMovies()) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Movies TopAppBar Wykład") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF61555E),
                    titleContentColor = Color.White
                ),
            )
        },
        bottomBar = {
            BottomAppBar(
                containerColor = Color(0xFF61555E),
                contentColor = Color.White,
                content = {
                    Text(
                        text = "Movies bottomBar Wykład",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        textAlign = TextAlign.Center
                    )
                }
            )
        },
        content = { paddingValues ->
            MainContent(
                modifier = Modifier.padding(paddingValues),
                navController = navController,
                movieList = movieList
            )
        }
    )
}

@Composable
fun MainContent(modifier: Modifier = Modifier, navController: NavController, movieList: List<Movie>) {
    LazyColumn(
        modifier = modifier.fillMaxSize(),
        contentPadding = PaddingValues(top = 8.dp)
    ) {
        items(items = movieList) { movie ->
            MovieRow(
                movie = movie,
                onItemClick = { movieId ->
                    Log.d(TAG, "MainContent: $movieId")
                    navController.navigate(MovieScreens.DetailsScreen.name + "/$movieId")
                }
            )
        }
    }
}