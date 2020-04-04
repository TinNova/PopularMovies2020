package com.tin.popularmovies.ui.detail

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Callback
import com.squareup.picasso.Picasso
import com.tin.popularmovies.ItemDecorator
import com.tin.popularmovies.R
import com.tin.popularmovies.ViewModelFactory
import com.tin.popularmovies.api.models.Movie
import com.tin.popularmovies.ui.home.HomeActivity.Companion.MOVIE_ID
import com.tin.popularmovies.ui.home.HomeActivity.Companion.MOVIE_TRANSITION
import dagger.android.AndroidInjection
import kotlinx.android.synthetic.main.activity_detail.*
import javax.inject.Inject


class DetailActivity : AppCompatActivity() {

    @Inject
    internal lateinit var viewModelFactory: ViewModelFactory<DetailViewModel>
    private lateinit var viewModel: DetailViewModel

    private val castAdapter = CastAdapter()

    private val trailerAdapter: TrailerAdapter by lazy {
        TrailerAdapter {
            playTrailer(it)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail)
        AndroidInjection.inject(this)
        viewModel = ViewModelProvider(this, viewModelFactory).get(DetailViewModel::class.java)

        intent.extras?.let {
            val movie = it.get(MOVIE_ID) as Movie
            viewModel.onViewLoaded(movie.id)
            movie_image.transitionName = it.get(MOVIE_TRANSITION) as String
            Picasso.get().load(movie.poster_path).into(movie_image)
            Picasso.get().load(movie.backdrop_path).into(backdrop_image)
            movie_title.text = movie.title
            movie_rating.text = movie.vote_average.toString()
            movie_release_date.text = movie.release_date
            movie_synopsis.text = movie.overview
        }

        observeViewState()
        setupRecyclerView()
    }

    private fun observeViewState() {
        viewModel.viewState.observe(this, Observer<DetailViewState> {
            it?.let {
                when (it.isPresenting) {
                    true -> showData(it.detailData)
//                    false -> movie_recycler_view.gone()
                }
                when (it.isLoading) {
//                    true -> loading_icon.visible()
//                    false -> loading_icon.gone()
                }
                when (it.isNetworkError) {
//                    true -> network_error.visible()
//                    false -> network_error.gone()
                }
            }
        })
    }

    private fun showData(it: DetailData) {
        castAdapter.setData(it.cast)
        trailerAdapter.setData(it.trailers)
    }

    private fun playTrailer(trailerUrl: Uri) {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.data = trailerUrl

        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        } else {
            Toast.makeText(this, "Can't Play Trailer", Toast.LENGTH_LONG).show()
        }
    }

    private fun setupRecyclerView() {
        cast_recyclerView.adapter = castAdapter
        setupLinearLayout(cast_recyclerView)
        trailer_recyclerview.adapter = trailerAdapter
        setupLinearLayout(trailer_recyclerview)
    }

    private fun setupLinearLayout(castRecyclerview: RecyclerView) {
        castRecyclerview.run {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            setHasFixedSize(true)
            val itemDecorator = object : ItemDecorator(R.dimen.margin_default) {}
            addItemDecoration(itemDecorator)
        }
    }

    override fun onBackPressed() {
        supportFinishAfterTransition()
    }
}
