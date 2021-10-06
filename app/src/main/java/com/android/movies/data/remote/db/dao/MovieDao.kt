package com.android.movies.data.remote.db.dao


import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.android.movies.data.remote.db.entity.MovieEntity

@Dao
interface MovieDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(movieEntity: List<MovieEntity>)

    @Query("Select * from Movie Where movieType = :movieType")
    suspend fun getMovieListByType(movieType: String): List<MovieEntity>

    @Query("Select * from Movie Where id = :id")
    suspend fun getMoveById(id: Int): MovieEntity


    @Query("UPDATE Movie SET isFav=:isFav WHERE id = :id AND movieType = :movieType")
    suspend fun updateFavMovie(isFav: Boolean, id: Int, movieType: String)
}