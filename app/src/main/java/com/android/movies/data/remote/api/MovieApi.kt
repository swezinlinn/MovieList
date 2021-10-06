package com.android.movies.data.remote.api

import com.android.movies.data.model.ResultResponse
import com.android.movies.BuildConfig
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface MovieApi {

    @GET(BuildConfig.BASE_URL+"upcoming")
    suspend fun getUpcomingMovie(
        @Query("api_key") apiKey : String,
        @Query("page") page: Int,
    ) : ResultResponse

    @GET(BuildConfig.BASE_URL+"popular")
    suspend fun getPopularMovie(
        @Query("api_key") apiKey : String,
        @Query("page") page: Int,
        ) : ResultResponse
}