package com.example.trumancranor.flixster.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.trumancranor.flixster.BuildConfig;
import com.example.trumancranor.flixster.R;
import com.example.trumancranor.flixster.adapters.MovieArrayAdapter;
import com.example.trumancranor.flixster.models.Movie;

import com.google.android.youtube.player.YouTubePlayerFragment;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnItemClick;
import cz.msebera.android.httpclient.Header;

import static com.example.trumancranor.flixster.R.id.swipeContainer;

public class MovieStreamActivity extends AppCompatActivity {
    ArrayList<Movie> movies;
    MovieArrayAdapter movieAdapter;
    @BindView(R.id.lvMovieStream) ListView lvItems;
    @BindView(R.id.swipeContainer) SwipeRefreshLayout swipeContainer;

    private static final String url = "https://api.themoviedb.org/3/movie/now_playing?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_stream);

        ButterKnife.bind(this);

        movies = new ArrayList<Movie>();
        movieAdapter = new MovieArrayAdapter(this, movies);
        lvItems.setAdapter(movieAdapter);

        //Capture this context for use in the OnItemClickListener
        final Context parentContext = this;

        swipeContainer.setOnRefreshListener(() -> asyncLoadData());

        asyncLoadData();
    }

    @OnItemClick(R.id.lvMovieStream)
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Movie movie = movies.get(position);

        if (movie.isPopular()) {
            Intent youtubeIntent = YoutubeVideoActivity.intentForVideo(this, movie.getTrailerKey());
            startActivity(youtubeIntent);
        } else {
            Intent intent = MovieDetailActivity.intentForMovie(this, movies.get(position));
            startActivity(intent);
        }
    }

    private void asyncLoadData() {
        /* Add the 'refresh' animation, even if we're loading data for app-start */
        swipeContainer.setRefreshing(true);

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(url, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray movieJsonResults = null;
                try {
                    movieJsonResults = response.getJSONArray("results");
                    movies.addAll(Movie.fromJSONArray(movieJsonResults));
                    movieAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                swipeContainer.setRefreshing(false);
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                super.onFailure(statusCode, headers, responseString, throwable);
                swipeContainer.setRefreshing(false);
            }
        });
    }
}
