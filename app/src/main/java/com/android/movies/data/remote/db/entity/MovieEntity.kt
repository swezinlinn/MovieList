package com.android.movies.data.remote.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.android.movies.util.Constants

@Entity(tableName = "Movie")
data class MovieEntity (@PrimaryKey val id: Int = 0,
                        var title: String = "",
                        var description: String = "",
                        var posterPath: String = "",
                        var backDropPath: String = "",
                        var popularity: Double = 0.0,
                        var isFav : Boolean = false,
                        var movieType : String = Constants.UPCOMING)