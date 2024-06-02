package com.example.pelis.Singleton

import com.example.pelis.Model.Movie

object SelectedMovieSingleton {
    private var selectedMovie: Movie? = null

    fun setSelectedMovie(movie: Movie) {
        selectedMovie = movie
    }

    fun getSelectedMovie(): Movie? {
        return selectedMovie
    }
}