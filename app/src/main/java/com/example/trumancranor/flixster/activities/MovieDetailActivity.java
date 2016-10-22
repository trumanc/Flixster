package com.example.trumancranor.flixster.activities;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.trumancranor.flixster.R;
import com.example.trumancranor.flixster.models.Movie;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class MovieDetailActivity extends AppCompatActivity {

    private static final String ARG_MOVIE = "movie";
    Movie movie;
    @BindView(R.id.tvOverview) TextView overview;
    @BindView(R.id.ratingBar) RatingBar ratingBar;
    @BindView(R.id.ivImage) ImageView image;
    @BindView(R.id.tvTagline) TextView tagline;
    @BindView(R.id.playButton) ImageView playButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.movie_detail_fragment);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        Movie movie = (Movie) intent.getSerializableExtra(ARG_MOVIE);

        this.movie = movie;

        setTitle(movie.getOriginalTitle() + " (" + movie.getReleaseDate().substring(0, 4) + ")");
        overview.setText(movie.getOverview());

        if (movie.getTrailerKey() == null) {
            playButton.setVisibility(View.INVISIBLE);
        }

        ratingBar.setNumStars(10);
        ratingBar.setStepSize(0.5f);
        ratingBar.setRating((float) movie.getStars());

        Picasso.with(this)
                .load(movie.getDetailPageImagePath(this))
                .placeholder(R.drawable.clapboard)
                .into(image);
        loadExtraDetailsAsync(movie);
    }

    private void loadExtraDetailsAsync(final Movie movie) {

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(getRequestUrl(movie.getId()), new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                try {
                    tagline.setText(response.getString("tagline"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
            }
        });
    }

    public static Intent intentForMovie(Context context, Movie movie) {
        Intent intent = new Intent(context, MovieDetailActivity.class);
        intent.putExtra(ARG_MOVIE, movie);
        return intent;
    }

    private static String getRequestUrl(int movieId) {
        return new Uri.Builder()
                .path("https://api.themoviedb.org/3/movie/")
                .appendPath(Integer.toString(movieId))
                .appendQueryParameter("api_key", "a07e22bc18f5cb106bfe4cc1f83ad8ed")
                .build().toString();

    }

    public void imageClicked(View image) {
        if (movie.getTrailerKey() != null) {
            Intent youtubeIntent = YoutubeVideoActivity.intentForVideo(this, movie.getTrailerKey());
            startActivity(youtubeIntent);
        } else {
            Toast.makeText(this, "No youtube trailer for this movie", Toast.LENGTH_SHORT).show();
        }
    }
}
