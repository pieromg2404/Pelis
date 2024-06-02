package com.example.pelis.ViewModels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.Room
import com.example.pelis.Services.AppDatabase
import com.example.pelis.Model.Movie
import com.example.pelis.Services.MovieDao
import com.example.pelis.Services.MovieEntity
import com.example.pelis.Services.RetrofitInstance
import com.example.pelis.Services.isInternetAvailable
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MovieViewModel(application: Application) : AndroidViewModel(application) {
    private val movieDao: MovieDao
    private val _movies = MutableStateFlow(emptyList<Movie>())
    val movies: StateFlow<List<Movie>> get() = _movies
    val _currentPage = MutableStateFlow(1)
    internal val _isSearching = MutableStateFlow(false)
    internal val _isLoadingMore = MutableStateFlow(false)
    private val _searchQuery = MutableStateFlow<String?>(null)
    private val _totalPages = MutableStateFlow(1)

    init {
        val database = Room.databaseBuilder(
            application,
            AppDatabase::class.java, "movie-database"
        ).build()
        movieDao = database.movieDao()
        loadMovies()
    }

    private fun loadMovies() {
        viewModelScope.launch {
            if (isInternetAvailable(getApplication())) {
                fetchMovies()
            } else {
                val localMovies = movieDao.getAllMovies()
                if (localMovies.isNotEmpty()) {
                    _movies.value = localMovies.map { it.toMovie() }
                }
            }
        }
    }

    fun fetchMovies() {
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.getUpcomingMovies(_currentPage.value)
                val newMovies = response.results.take(20)
                _movies.value = _movies.value + newMovies
                _currentPage.value = response.page + 1
                _totalPages.value = response.total_pages
                movieDao.insertAll(newMovies.map { it.toEntity() })
            } catch (e: Exception) {
            }
        }
    }

    fun searchMovies(query: String) {
        _searchQuery.value = query
        _currentPage.value = 1
        _isSearching.value = true
        viewModelScope.launch {
            try {
                val response = RetrofitInstance.api.searchMovies(query, _currentPage.value)
                _movies.value = response.results.take(20)
                _totalPages.value = response.total_pages
                _currentPage.value=_currentPage.value+1
                movieDao.insertAll(_movies.value.map { it.toEntity() })
            } catch (e: Exception) {
            } finally {
                _isSearching.value = false
            }
        }
    }

    fun loadMore() {
        if (_isLoadingMore.value ) return

        _isLoadingMore.value = true
        viewModelScope.launch {
            try {
                val query = _searchQuery.value
                if (query != null) {
                    val response = RetrofitInstance.api.searchMovies(query, _currentPage.value)
                    val newMovies = response.results.take(20)
                    _movies.value = _movies.value + newMovies
                    if (newMovies.isNotEmpty()){
                        _currentPage.value = response.page + 1
                        movieDao.insertAll(newMovies.map { it.toEntity() })
                    }
                } else {
                    fetchMovies()
                }
            } catch (e: Exception) {
            } finally {
                _isLoadingMore.value = false
            }
        }
    }

    fun clearSearch() {
        _searchQuery.value = null
        _currentPage.value = 1
        _movies.value=emptyList<Movie>()
        viewModelScope.launch {
            if (isInternetAvailable(getApplication())) {
                fetchMovies()
            } else {
                val localMovies = movieDao.getAllMovies()
                if (localMovies.isNotEmpty()) {
                    _movies.value = localMovies.map { it.toMovie() }
                }
            }
        }
    }
}


fun Movie.toEntity(): MovieEntity {
    return MovieEntity(id, title, vote_average, release_date, overview, poster_path)
}

fun MovieEntity.toMovie(): Movie {
    return Movie(id, title, vote_average, release_date, overview, poster_path)
}
