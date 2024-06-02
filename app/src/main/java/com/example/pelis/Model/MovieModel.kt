package com.example.pelis.Model

data class MovieResponse(
    val results: List<Movie>,
    val page: Int,
    val total_pages: Int
)

data class Movie(
    val id: Int,
    val title: String,
    val vote_average: Double,
    val release_date: String,
    val overview: String,
    val poster_path: String
) {
    override fun toString(): String {
        return "Movie(id=$id, title='$title', vote_average=$vote_average, release_date='$release_date', overview='$overview', poster_path='$poster_path')"
    }
}