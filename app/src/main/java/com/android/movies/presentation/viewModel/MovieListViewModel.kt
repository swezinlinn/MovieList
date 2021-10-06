package com.android.movies.presentation.viewModel

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.android.movies.data.repository.MovieRepo
import com.android.movies.domain.model.MovieList
import com.android.movies.util.Constants
import com.android.movies.domain.repo.MoviePagingRepository
import com.android.movies.presentation.BaseViewModel
import com.android.movies.util.Resource
import com.android.movies.util.decode
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class MovieListViewModel @ViewModelInject constructor(
    private val moviePagingRepository: MoviePagingRepository,
    private val movieRepo: MovieRepo
) : BaseViewModel() {
    private val TAG = MovieListViewModel::class.java.simpleName

    lateinit var postUpcomingMovie  :LiveData<PagingData<MovieList>>
    lateinit var postPopularMovie  :LiveData<PagingData<MovieList>>
    val movie: MutableLiveData<Resource<MovieList>> = MutableLiveData()

    fun getMovieList(movieType: String){
        if(movieType == Constants.UPCOMING) {
            postUpcomingMovie = viewModelScope.let {
                moviePagingRepository.getMovieList(decode(Constants.KEY), movieType)
                    .cachedIn(viewModelScope)
            }
        }else{
            postPopularMovie = viewModelScope.let {
                moviePagingRepository.getMovieList(decode(Constants.KEY), movieType)
                    .cachedIn(viewModelScope)
            }
        }
    }

    suspend fun updateFavMovie(isFav: Boolean, id: Int, movieType: String){
        val job = viewModelScope.launch {
            movieRepo.updateFavMovie(isFav, id, movieType)
        }
        job.join()
        getMovieList(movieType)
    }

    suspend fun getMovieById(id: Int){
        withContext(Dispatchers.IO) {
            try {
                movie.postValue(Resource.success(movieRepo.getMoveById(id)))
            } catch (ex: Exception) {
                ex.printStackTrace()
            }
        }
    }

}