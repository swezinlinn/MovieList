package com.android.movies.data.repository

import com.android.movies.data.mapper.MovieResponseMapper
import com.android.movies.data.remote.db.service.DbHelper
import com.android.movies.domain.model.MovieList
import com.android.movies.util.Resource
import kotlinx.coroutines.flow.Flow


class MovieRepoImpl(private val dbHelper: DbHelper) : MovieRepo {


    override suspend fun updateFavMovie(isFav: Boolean, id: Int, movieType: String) {
        dbHelper.updateFavMovie(isFav, id, movieType)
    }

    override suspend fun getMoveById(id: Int): MovieList = MovieResponseMapper.mapMovieEntityToMovieList(dbHelper.getMoveById(id))


}