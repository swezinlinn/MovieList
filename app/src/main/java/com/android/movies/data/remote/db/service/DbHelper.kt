package com.android.movies.data.remote.db.service

import com.android.movies.data.remote.db.entity.MovieEntity


interface DbHelper {

    suspend fun updateFavMovie(isFav: Boolean, id: Int, movieType: String)

    suspend fun getMoveById(id: Int): MovieEntity
}