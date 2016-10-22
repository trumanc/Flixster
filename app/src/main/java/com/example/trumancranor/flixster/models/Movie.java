package com.example.trumancranor.flixster.models;

import android.content.Context;
import android.content.res.Configuration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;


public class Movie implements Serializable {

    private static final double POPULARITY_THRESHOLD = 10.0;

    public String getPosterPath() {
        return String.format("https://image.tmdb.org/t/p/w342%s", posterPathSuffix);
    }

    public String getBackdropPath() {
        return String.format("https://image.tmdb.org/t/p/w780%s", backdropPathSuffix);
    }

    public String getStreamPageImagePath(Context context) {
        if (isPopular()) {
            return getBackdropPath();
        }

        if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT
                || backdropPathSuffix == null || backdropPathSuffix.isEmpty()) {
            return getPosterPath();
        } else {
            return getBackdropPath();
        }
    }

    public String getDetailPageImagePath(Context context) {
        if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            return getBackdropPath();
        } else {
            return getPosterPath();
        }
    }

    public boolean isPopular() {
        /* Impose a restriction that we will only consider a movie popular if it also has a backdrop image */
        return popularity >= POPULARITY_THRESHOLD && backdropPathSuffix != null && !backdropPathSuffix.isEmpty();
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getOverview() {
        return overview;
    }

    private String posterPathSuffix;

    public int getId() {
        return id;
    }

    private String backdropPathSuffix;
    private String originalTitle;
    private String overview;

    public String getTrailerKey() {
        return trailerKey;
    }

    public void setTrailerKey(String trailerKey) {
        this.trailerKey = trailerKey;
    }

    private String trailerKey;
    private int id;
    private double rating;
    private double popularity;

    public String getReleaseDate() {
        return releaseDate;
    }

    private String releaseDate;

    public Movie(JSONObject jsonObject) throws JSONException {
        this.posterPathSuffix = jsonObject.getString("poster_path");
        this.backdropPathSuffix = jsonObject.getString("backdrop_path");
        this.originalTitle = jsonObject.getString("original_title");
        this.overview = jsonObject.getString("overview");
        this.releaseDate = jsonObject.getString("release_date");
        this.id = jsonObject.getInt("id");
        this.rating = jsonObject.getDouble("vote_average");
        this.popularity = jsonObject.getDouble("popularity");
    }

    public double getStars() {
        return rating;
    }
    public static ArrayList<Movie> fromJSONArray(JSONArray array) {
        ArrayList<Movie> results = new ArrayList<Movie>(array.length());

        for (int ind = 0; ind < array.length(); ind++) {
            try {
                results.add(new Movie(array.getJSONObject(ind)));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return results;
    }
}
