package com.android.movies.data.remote.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.android.movies.data.remote.db.dao.MovieDao
import com.android.movies.data.remote.db.entity.MovieEntity

@Database(entities = [MovieEntity::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract val movieDao: MovieDao

    companion object {
        const val DB_NAME = "MovieDetail.db"
    }
}
