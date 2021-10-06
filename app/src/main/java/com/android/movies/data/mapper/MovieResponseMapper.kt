package com.android.movies.data.mapper

import com.android.movies.data.model.ResultResponse
import com.android.movies.data.remote.db.entity.MovieEntity
import com.android.movies.domain.model.MovieList
import java.util.*

class MovieResponseMapper {
    companion object {
        val mapMovieEntityListToMovieList: ((List<MovieEntity?>?) -> (List<MovieList>)) =
            {
                val tempList = mutableListOf<MovieList>()
                if (it != null) {
                    for (value in it) {
                        if (value != null) {
                            tempList.add(
                                MovieList(
                                    value.id ,
                                    value.title,
                                    value.description,
                                    value.posterPath,
                                    value.backDropPath,
                                    value.popularity,
                                    value.isFav,
                                    value.movieType
                                )
                            )
                        } else {
                            tempList.add(MovieList())
                        }
                    }
                }

                tempList
            }

        val mapMovieListResponseToMovieList: ((List<ResultResponse.Result?>?, movieType: String) -> (List<MovieList>)) =
            {it,movieType ->
                val tempList = mutableListOf<MovieList>()
                if (it != null) {
                    for (value in it) {
                        if (value != null) {
                            tempList.add(
                                MovieList(
                                    value.id ?: 0,
                                    value.title ?: "",
                                    value.overview ?: "",
                                    value.posterPath ?: "",
                                    value.backdropPath ?: "",
                                    value.popularity ?: 0.0,
                                    false,
                                    movieType
                                )
                            )
                        } else {
                            tempList.add(MovieList())
                        }
                    }
                }

                tempList
            }


        val mapMovieListToMovieEntity: ((List<MovieList?>?) -> (List<MovieEntity>)) =
            {
                val tempList = mutableListOf<MovieEntity>()
                if (it != null) {
                    for (value in it) {
                        if (value != null) {
                            tempList.add(
                                MovieEntity(
                                    value.id ,
                                    value.title,
                                    value.description,
                                    value.posterPath,
                                    value.backDropPath,
                                    value.popularity,
                                    value.isFav,
                                    value.movieType
                                )
                            )
                        } else {
                            tempList.add(MovieEntity())
                        }
                    }
                }

                tempList
            }

        val mapMovieEntityToMovieList: ((MovieEntity) -> (MovieList)) = {
            MovieList(it.id,
                it.title,
                it.description,
                it.posterPath,
                it.backDropPath,
                it.popularity,
                it.isFav,
                it.movieType
                )
        }
    }
}