package com.android.movies.data.repository

import com.android.movies.domain.model.MovieList
import com.android.movies.util.Resource


interface MovieRepo {

    suspend fun updateFavMovie(isFav: Boolean, id: Int, movieType: String)

    suspend fun getMoveById(id: Int): MovieList
}