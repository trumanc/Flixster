package com.example.trumancranor.flixster.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.trumancranor.flixster.R;
import com.example.trumancranor.flixster.models.Movie;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import cz.msebera.android.httpclient.Header;

public class MovieArrayAdapter extends ArrayAdapter<Movie> {

    private static final int MOVIE_VIEW_TYPE = 0;
    private static final int POPULAR_MOVIE_VIEW_TYPE = 1;

    public MovieArrayAdapter(Context context, List<Movie> movies) {
        super(context, android.R.layout.simple_list_item_1, movies);
    }

    static class Holder {
        @Nullable
        @BindView(R.id.tvTitle) TextView title;

        @Nullable
        @BindView(R.id.tvOverview) TextView overview;

        @BindView(R.id.ivMovieImage) ImageView image;

        @Nullable
        @BindView(R.id.playButton) ImageView playButton;

        Movie movie; // To check if the movie has changed when we asynchronously fetch more info

        public Holder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    @Override
    public int getViewTypeCount() {
        return 2;
    }

    @Override
    public int getItemViewType(int position) {
        if (getItem(position).isPopular()) {
            return POPULAR_MOVIE_VIEW_TYPE;
        } else {
            return MOVIE_VIEW_TYPE;
        }
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Movie movie = getItem(position);
        Holder viewHolder;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            if (movie.isPopular()) {
                convertView = inflater.inflate(R.layout.item_movie_popular, parent, false);
            } else {
                convertView = inflater.inflate(R.layout.item_movie, parent, false);
            }

            viewHolder = new Holder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (Holder) convertView.getTag();
        }

        viewHolder.movie = movie;
        if (viewHolder.title != null) {
            viewHolder.title.setText(movie.getOriginalTitle());
        }
        if (viewHolder.overview != null) {
            viewHolder.overview.setText(movie.getOverview());
        }
        viewHolder.image.setImageResource(0);
        Picasso.with(getContext())
                .load(movie.getStreamPageImagePath(getContext()))
                .placeholder(R.drawable.clapboard)
                .into(viewHolder.image);
        if (viewHolder.playButton != null) {
            viewHolder.playButton.setVisibility(View.VISIBLE);
        }

        if (movie.getTrailerKey() == null) {
            asyncGetTrailerKey(movie, convertView);
        }
        return convertView;
    }

    private void asyncGetTrailerKey(Movie movie, View view) {
        AsyncHttpClient client = new AsyncHttpClient();
        String videosRequestUrl =
                new Uri.Builder()
                        .encodedPath("https://api.themoviedb.org/3/movie/")
                        .appendPath(Integer.toString(movie.getId()))
                        .appendPath("videos")
                        .appendQueryParameter("api_key", "a07e22bc18f5cb106bfe4cc1f83ad8ed")
                        .build().toString();

        client.get(videosRequestUrl, new JsonHttpResponseHandler(){
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                JSONArray trailers = null;
                try {
                    trailers = response.getJSONArray("results");
                    for (int ind = 0; ind < trailers.length(); ind++) {
                        JSONObject video = trailers.getJSONObject(ind);
                        if (video.getString("site").equals("YouTube")) {
                            movie.setTrailerKey(video.getString("key"));
                            Holder holder = (Holder) view.getTag();
                            if (holder.playButton != null && holder.movie == movie) {
                                holder.playButton.setVisibility(View.VISIBLE);
                            }
                            return;
                        }
                    }
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
}
