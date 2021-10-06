package com.android.movies.domain.model

import com.android.movies.util.Constants

data class MovieList(
    var id: Int = 0,
    var title: String = "",
    var description: String = "",
    var posterPath: String = "",
    var backDropPath: String = "",
    var popularity: Double = 0.0,
    var isFav : Boolean = false,
    var movieType : String = Constants.UPCOMING
    )