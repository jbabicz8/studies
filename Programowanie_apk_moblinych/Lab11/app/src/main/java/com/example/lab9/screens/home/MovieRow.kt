package com.example.lab9.screens.home

import androidx.compose.animation.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.example.lab9.models.Movie

@Composable
fun MovieRow(movie: Movie, onItemClick: (Int) -> Unit) {
    var expanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp, horizontal = 8.dp)
            .clickable { onItemClick(movie.id) },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Column {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    modifier = Modifier
                        .size(100.dp)
                        .padding(4.dp),
                    shape = RoundedCornerShape(12.dp),
                    shadowElevation = 4.dp
                ) {
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(movie.images.firstOrNull())
                            .crossfade(true)
                            .build(),
                        contentDescription = "Movie Poster",
                        contentScale = ContentScale.Crop
                    )
                }

                Column(modifier = Modifier.padding(4.dp).weight(1f)) {
                    Text(text = movie.title, style = MaterialTheme.typography.headlineSmall)
                    Text(text = "Reżyser: ${movie.director}", style = MaterialTheme.typography.bodySmall)
                    Text(text = "Rok: ${movie.year}", style = MaterialTheme.typography.bodySmall)

                    Icon(
                        imageVector = if (expanded) Icons.Filled.KeyboardArrowUp else Icons.Filled.KeyboardArrowDown,
                        contentDescription = "Expand",
                        modifier = Modifier
                            .size(25.dp)
                            .clickable { expanded = !expanded },
                        tint = Color.DarkGray
                    )
                }
            }

            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically() + fadeIn(),
                exit = shrinkVertically() + fadeOut()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(buildAnnotatedString {
                        withStyle(style = SpanStyle(color = Color.DarkGray, fontSize = 13.sp, fontWeight = FontWeight.Bold)) {
                            append("Opis: ")
                        }
                        withStyle(style = SpanStyle(color = Color.DarkGray, fontSize = 13.sp)) {
                            append(movie.plot)
                        }
                    })
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
                    Text(text = "Gatunek: ${movie.genere}", style = MaterialTheme.typography.bodySmall)
                    Text(text = "Obsada: ${movie.actors}", style = MaterialTheme.typography.bodySmall)
                    Text(text = "Czas trwania: ${movie.runtime}", style = MaterialTheme.typography.bodySmall)
                }
            }
        }
    }
}