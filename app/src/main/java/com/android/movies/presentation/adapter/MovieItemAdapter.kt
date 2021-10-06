package com.android.movies.presentation.adapter

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.android.movies.BuildConfig
import com.android.movies.R
import com.android.movies.databinding.HolderPhotoBinding
import com.android.movies.domain.model.MovieList
import com.android.movies.presentation.viewModel.MovieViewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.Target
import kotlin.math.roundToInt


internal class MovieItemAdapter(
    private val onItemClickListener: OnItemClickListener,
    private val onFavItemClickListener: OnFavClickListener,
    private val drawable: Int,
    private val requireContext: Context
) :
    PagingDataAdapter<MovieList, MovieItemAdapter.MovieViewHolder>(MOVIE_COMPARATOR) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieViewHolder {
        val binding =
            HolderPhotoBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return MovieViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MovieViewHolder, position: Int) {
        val currentItem = getItem(position)

        if (currentItem != null) {
            holder.bind(currentItem)
        }
    }

    inner class MovieViewHolder(private val binding: HolderPhotoBinding) :
        RecyclerView.ViewHolder(binding.root) {
        init {
            val scale = requireContext.resources.displayMetrics.density
            binding.card.layoutParams.width = ((190 * scale + 0.5f).roundToInt())

            binding.root.setOnClickListener {
                val position = bindingAdapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val item = getItem(position)
                    if (item != null) {
                        onItemClickListener.onItemClick(item.id)
                    }
                }
            }
        }

        fun bind(movie: MovieList) {
            var isFav  = movie.isFav

            binding.movieViewModel = MovieViewModel(movie)
            setImage(isFav, binding.fav)

            binding.fav.setOnClickListener {
                isFav = !isFav
                setImage(isFav,binding.fav)
                onFavItemClickListener.onFavClick(isFav,movie.id,movie.movieType)
            }

            binding.apply {
                val requestOptions = RequestOptions()
                requestOptions.placeholder(drawable)
                requestOptions.error(drawable)
                Glide.with(itemView)
                    .load(BuildConfig.IMAGE_BASE_URL+movie.posterPath)
                    .centerCrop()
                    .apply(requestOptions)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .listener(object : RequestListener<Drawable> {
                        override fun onLoadFailed(
                            e: GlideException?,
                            model: Any?,
                            target: Target<Drawable>?,
                            isFirstResource: Boolean
                        ): Boolean {
                            binding.photoProgressBar.visibility = View.GONE
                            return false
                        }

                        override fun onResourceReady(
                            resource: Drawable?,
                            model: Any?,
                            target: Target<Drawable>?,
                            dataSource: com.bumptech.glide.load.DataSource?,
                            isFirstResource: Boolean
                        ): Boolean {
                            binding.photoProgressBar.visibility = View.GONE
                            return false
                        }

                    })
                    .into(binding.photoImageView)
            }
        }

        private fun setImage(isFav: Boolean, fav: ImageView) {
            if (isFav) {
                fav.setImageDrawable(requireContext.getDrawable(R.drawable.ic_star_full_vector))
            } else {
                fav.setImageDrawable(requireContext.getDrawable(R.drawable.ic_star_empty_white_vector))
            }
        }
    }

    interface OnFavClickListener{
        fun onFavClick(isFav: Boolean, id: Int, movieType: String)
    }

    interface OnItemClickListener {
        fun onItemClick(id: Int)
    }

    companion object {
        private val MOVIE_COMPARATOR = object : DiffUtil.ItemCallback<MovieList>() {
            override fun areItemsTheSame(oldItem: MovieList, newItem: MovieList) =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: MovieList, newItem: MovieList) =
                oldItem == newItem
        }
    }
}
