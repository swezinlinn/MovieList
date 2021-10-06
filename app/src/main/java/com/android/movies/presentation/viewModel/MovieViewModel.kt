package com.android.movies.presentation.viewModel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.android.movies.domain.model.MovieList

class MovieViewModel(val list: MovieList) :ViewModel() {

    private val TAG = MovieViewModel::class.java.name
    val photoData = MutableLiveData<MovieList>()

    init {
        photoData.value = list
    }

}