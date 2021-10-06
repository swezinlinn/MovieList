package com.android.movies.data.remote.db.service

import com.android.movies.data.remote.db.dao.MovieDao
import com.android.movies.data.remote.db.entity.MovieEntity

class DbHelperImpl(private val movieDao: MovieDao) : DbHelper {

    override suspend fun updateFavMovie(isFav: Boolean, id: Int, movieType: String) {
        movieDao.updateFavMovie(isFav, id, movieType)
    }

    override suspend fun getMoveById(id: Int): MovieEntity {
        return movieDao.getMoveById(id)
    }
}