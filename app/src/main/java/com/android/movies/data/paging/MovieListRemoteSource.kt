package com.android.movies.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.android.movies.data.mapper.MovieResponseMapper
import com.android.movies.data.remote.api.MovieApi
import com.android.movies.data.remote.db.dao.MovieDao
import com.android.movies.domain.model.MovieList
import com.android.movies.util.Constants
import com.bumptech.glide.load.HttpException
import java.io.IOException

private const val STARTING_PAGE_INDEX = 1

class MovieDataSource(
    private val movieApi: MovieApi,
    private val apiKey: String,
    private val dao: MovieDao,
    private val movieType: String
) : PagingSource<Int, MovieList>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, MovieList> {
        val position = params.key ?: STARTING_PAGE_INDEX

        return try {
            var photos = emptyList<MovieList>()
            val movieList =
                MovieResponseMapper.mapMovieEntityListToMovieList(dao.getMovieListByType(movieType))
            photos = if (movieList.isEmpty()) {

                val response = if (movieType == Constants.POPULAR) {
                    MovieResponseMapper.mapMovieListResponseToMovieList(
                        movieApi.getPopularMovie(
                            apiKey,
                            position
                        ).results, movieType
                    )
                } else {
                    MovieResponseMapper.mapMovieListResponseToMovieList(
                        movieApi.getUpcomingMovie(
                            apiKey,
                            position
                        ).results, movieType
                    )
                }

                dao.insertAll(MovieResponseMapper.mapMovieListToMovieEntity(response))
                response
            } else {
                movieList
            }

            LoadResult.Page(
                data = photos,
                prevKey = if (position == STARTING_PAGE_INDEX) null else position - 1,
                nextKey = if (photos.isEmpty()) null else position + 1
            )
        } catch (exception: IOException) {
            LoadResult.Error(exception)
        } catch (exception: HttpException) {
            LoadResult.Error(exception)
        }
    }

    override fun getRefreshKey(state: PagingState<Int, MovieList>): Int? {
        return state.anchorPosition?.let {
            state.closestPageToPosition(it)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(it)?.nextKey?.minus(1)
        }
    }
}