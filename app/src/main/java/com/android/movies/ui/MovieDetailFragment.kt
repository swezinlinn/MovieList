package com.android.movies.ui

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.android.movies.BuildConfig
import com.android.movies.R
import com.android.movies.databinding.FragmentMovieDetailBinding
import com.android.movies.domain.model.MovieList
import com.android.movies.presentation.viewModel.MovieListViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestOptions
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MovieDetailFragment : Fragment(R.layout.fragment_movie_detail) {
    private var movieId : Int = 0
    private lateinit var _binding: FragmentMovieDetailBinding
    private val binding get() = _binding
    private val viewModel: MovieListViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        _binding = FragmentMovieDetailBinding.bind(view)

        movieId = MovieDetailFragmentArgs.fromBundle(requireArguments()).movieId

        setUpToolbar(binding.detailToolbar)
        setHasOptionsMenu(true)
        lifecycleScope.launch {
            viewModel.getMovieById(movieId)
            viewModel.movie.observe(viewLifecycleOwner)  {
                it.subscribeState(
                    onSuccess = {
                        setUpData(it)
                    },
                )
            }
        }

    }

    private fun setUpData(movieList: MovieList) {
        val requestOptions = RequestOptions()
        requestOptions.placeholder(R.drawable.ic_place_holder)
        requestOptions.error(R.drawable.ic_place_holder)
        Glide.with(requireContext())
            .load(BuildConfig.IMAGE_BASE_URL+movieList.backDropPath)
            .apply(requestOptions)
            .centerCrop()
            .transition(DrawableTransitionOptions.withCrossFade())
            .into(binding.detailToolbarImageView)
        binding.apply {
            setImage(movieList.isFav, binding.fav)

            detailToolbar.title = movieList.title
            popularity.text = movieList.popularity.toString()
            plot.text = movieList.description
        }

        var isFav = movieList.isFav
        binding.fav.setOnClickListener {
            if (isFav) {
                lifecycleScope.launch {
                    viewModel.updateFavMovie(false, movieList.id, movieList.movieType)
                }
            } else {
                lifecycleScope.launch {
                    viewModel.updateFavMovie(true, movieList.id, movieList.movieType)
                }
            }
            isFav = !isFav
            setImage(isFav, binding.fav)
        }
    }

    private fun setImage(isFav: Boolean, fav: ImageView) {
        if (isFav) {
            fav.setImageDrawable(requireContext().getDrawable(R.drawable.ic_star_full_vector))
        } else {
            fav.setImageDrawable(requireContext().getDrawable(R.drawable.ic_star_empty_white_vector))
        }
    }

    private fun setUpToolbar(toolbar: Toolbar) {
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(false)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setHomeButtonEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                findNavController().popBackStack()
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
}