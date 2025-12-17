package com.example.lab9.models

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
            poster = "-",
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
            poster = "-",
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
            poster = "-",
            images = listOf(
                "-",
                "-",
            ),
            rating = "10"
        ),
        Movie(
            id = "tt0416451",
            title = "Life",
            year = "-",
            genre = "-",
            director = "-",
            actors = "-",
            plot = "-",
            poster = "-",
            images = listOf(
                "-",
                "-",
            ),
            rating = "7.7"
        ),
        Movie(
            id = "tt0416452",
            title = "Lolek",
            year = "-",
            genre = "-",
            director = "-",
            actors = "-",
            plot = "-",
            poster = "-",
            images = listOf(
                "-",
                "-",
            ),
            rating = "7.7"
        ),
        Movie(
            id = "tt0416453",
            title = "Bolek",
            year = "-",
            genre = "-",
            director = "-",
            actors = "-",
            plot = "-",
            poster = "-",
            images = listOf(
                "-",
                "-",
            ),
            rating = "7.7"
        ),
        Movie(
            id = "tt0416454",
            title = "Krecik",
            year = "-",
            genre = "-",
            director = "-",
            actors = "-",
            plot = "-",
            poster = "-",
            images = listOf(
                "-",
                "-",
            ),
            rating = "7.7"
        ),
        Movie(
            id = "tt0416455",
            title = "Krecik",
            year = "-",
            genre = "-",
            director = "-",
            actors = "-",
            plot = "-",
            poster = "-",
            images = listOf(
                "-",
                "-",
            ),
            rating = "7.7"
        ),
        Movie(
            id = "tt0416456",
            title = "Idą Święta",
            year = "-",
            genre = "-",
            director = "-",
            actors = "-",
            plot = "-",
            poster = "-",
            images = listOf(
                "-",
                "-",
            ),
            rating = "7.7"
        ),
        Movie(
            id = "tt0416457",
            title = "Wykład",
            year = "-",
            genre = "-",
            director = "-",
            actors = "-",
            plot = "-",
            poster = "-",
            images = listOf(
                "-",
                "-",
            ),
            rating = "7.7"
        )

    )
}
