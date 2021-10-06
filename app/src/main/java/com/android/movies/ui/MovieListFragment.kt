package com.android.movies.ui

import android.os.Bundle
import android.view.*
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.paging.LoadState
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.movies.util.Constants
import com.android.movies.presentation.viewModel.MovieListViewModel
import com.android.movies.R
import com.android.movies.databinding.FragmentMovieListBinding
import com.android.movies.presentation.adapter.MovieItemAdapter
import com.android.movies.presentation.adapter.MovieLoadStateAdapter
import com.facebook.shimmer.ShimmerFrameLayout
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MovieListFragment : Fragment(R.layout.fragment_movie_list), MovieItemAdapter.OnItemClickListener, MovieItemAdapter.OnFavClickListener {

    private lateinit var _binding: FragmentMovieListBinding
    private val binding get() = _binding
    private val viewModel: MovieListViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val popularAdapter = MovieItemAdapter(this,this,R.drawable.ic_place_holder,requireContext())
        val upcomingAdapter = MovieItemAdapter(this,this, R.drawable.ic_place_holder,requireContext())

        _binding = FragmentMovieListBinding.bind(view)
        setHasOptionsMenu(true)
        setUpToolbar(binding.toolbar)

        binding.apply {
            rvPopular.setHasFixedSize(true)
            rvPopular.itemAnimator = null
            rvPopular.layoutManager =  LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            rvPopular.adapter = popularAdapter.withLoadStateFooter(
                MovieLoadStateAdapter { popularAdapter.retry() }
            )
        }

        binding.apply {
            rvUpcoming.setHasFixedSize(true)
            rvUpcoming.itemAnimator = null
            rvUpcoming.layoutManager =  LinearLayoutManager(requireContext(), RecyclerView.HORIZONTAL, false)
            rvUpcoming.adapter = upcomingAdapter.withLoadStateFooter(
                MovieLoadStateAdapter { upcomingAdapter.retry() }
            )
        }


        lifecycleScope.launch {
            viewModel.getMovieList(Constants.POPULAR)
            viewModel.postPopularMovie.observe(viewLifecycleOwner) {
                popularAdapter.submitData(viewLifecycleOwner.lifecycle, it)
            }
        }

        lifecycleScope.launch {
            viewModel.getMovieList(Constants.UPCOMING)
            viewModel.postUpcomingMovie.observe(viewLifecycleOwner) {
                upcomingAdapter.submitData(viewLifecycleOwner.lifecycle, it)
            }
        }
        addLoadState(upcomingAdapter,binding.rvUpcoming,binding.shimmerLayoutUpcoming.shimmerViewContainer,binding.upcoming)
        addLoadState(popularAdapter,binding.rvPopular,binding.shimmerLayoutPopular.shimmerViewContainer,binding.popular)
    }

    private fun addLoadState(adapter: MovieItemAdapter,recyclerView: RecyclerView, shimmer: ShimmerFrameLayout, text: TextView) {
        adapter.addLoadStateListener { loadState ->
            binding.apply {
                when (loadState.source.refresh) {
                    is LoadState.Loading -> startShimmer(shimmer,recyclerView,text)
                    is LoadState.NotLoading -> stopShimmer(shimmer,recyclerView,text)
                    is LoadState.Error -> startShimmer(shimmer,recyclerView,text)
                }
            }
        }
    }

    private fun startShimmer(shimmer : ShimmerFrameLayout, view : RecyclerView, title: TextView){
        shimmer.startShimmerAnimation()
        shimmer.visibility = View.VISIBLE
        view.visibility = View.GONE
        title.visibility = View.GONE
    }

    private fun stopShimmer(shimmer : ShimmerFrameLayout, view : RecyclerView, title: TextView){
        shimmer.stopShimmerAnimation()
        shimmer.visibility = View.GONE
        view.visibility = View.VISIBLE
        title.visibility = View.VISIBLE
    }

    private fun setUpToolbar(toolbar: Toolbar) {
        (activity as AppCompatActivity).setSupportActionBar(toolbar)
        (activity as AppCompatActivity).supportActionBar?.setDisplayShowTitleEnabled(true)
        (activity as AppCompatActivity).supportActionBar?.setDisplayHomeAsUpEnabled(false)
        (activity as AppCompatActivity).supportActionBar?.setHomeButtonEnabled(true)
    }


    override fun onItemClick(id: Int) {
        findNavController().navigate(MovieListFragmentDirections.actionMovieListToMovieDetail(id))
    }

    override fun onFavClick(isFav: Boolean, id: Int, movieType: String) {
        lifecycleScope.launch {
            viewModel.updateFavMovie(isFav, id, movieType)
        }
    }
}