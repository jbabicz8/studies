package com.example.lab9.models

import com.example.lab9.R

data class Movie(
    val id: String,
    val title: String,
    val year: String,
    val genre: String,
    val director: String,
    val actors: String,
    val plot: String,
    val poster: String,
    val images: List<String>,
    val rating: String
)

fun getMovies(): List<Movie> {
    return listOf(
        Movie(
            id = "tt0416450",
            title = "Avatar",
            year = "-",
            genre = "-",
            director = "-",
            actors = "-",
            plot = "-",
            poster = R.drawable.avatar.toString(),
            images = listOf(
                "-",
                "-",
            ),
            rating = "10"
        ),
        Movie(
            id = "tt0416450",
            title = "300",
            year = "-",
            genre = "-",
            director = "-",
            actors = "-",
            plot = "-",
            poster = R.drawable.spartan.toString(),
            images = listOf(
                "-",
                "-",
            ),
            rating = "10"
        ),
        Movie(
            id = "tt0416449",
            title = "Harry Potter",
            year = "-",
            genre = "-",
            director = "-",
            actors = "-",
            plot = "-",
            poster = R.drawable.harrypotter.toString(),
            images = listOf(
                "-",
                "-",
            ),
            rating = "10"
        ),
        Movie(
            id = "tt0416451",
            title = "Hobbit",
            year = "-",
            genre = "-",
            director = "-",
            actors = "-",
            plot = "-",
            poster = R.drawable.hobbit.toString(),
            images = listOf(
                "-",
                "-",
            ),
            rating = "7.7"
        ),
        Movie(
            id = "tt0416452",
            title = "Władca Pierścieni: Drużyna pierścienia",
            year = "-",
            genre = "-",
            director = "-",
            actors = "-",
            plot = "-",
            poster = R.drawable.wladcapierscieni.toString(),
            images = listOf(
                "-",
                "-",
            ),
            rating = "7.7"
        ),
        Movie(
            id = "tt0416453",
            title = "Obecność",
            year = "-",
            genre = "-",
            director = "-",
            actors = "-",
            plot = "-",
            poster = R.drawable.obecnosc.toString(),
            images = listOf(
                "-",
                "-",
            ),
            rating = "7.7"
        ),
        Movie(
            id = "tt0416454",
            title = "Interstellar",
            year = "-",
            genre = "-",
            director = "-",
            actors = "-",
            plot = "-",
            poster = R.drawable.interstellar.toString(),
            images = listOf(
                "-",
                "-",
            ),
            rating = "7.7"
        ),
        Movie(
            id = "tt0416455",
            title = "Piraci z Karaibów",
            year = "-",
            genre = "-",
            director = "-",
            actors = "-",
            plot = "-",
            poster = R.drawable.pirates.toString(),
            images = listOf(
                "-",
                "-",
            ),
            rating = "7.7"
        ),
        Movie(
            id = "tt0416456",
            title = "Listy do M",
            year = "-",
            genre = "-",
            director = "-",
            actors = "-",
            plot = "-",
            poster = R.drawable.listy.toString(),
            images = listOf(
                "-",
                "-",
            ),
            rating = "7.7"
        ),
        Movie(
            id = "tt0416457",
            title = "John Wick",
            year = "-",
            genre = "-",
            director = "-",
            actors = "-",
            plot = "-",
            poster = R.drawable.johnwick.toString(),
            images = listOf(
                "-",
                "-",
            ),
            rating = "7.7"
        )

    )
}
