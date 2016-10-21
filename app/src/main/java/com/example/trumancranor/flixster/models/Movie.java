package com.example.trumancranor.flixster.models;

import android.content.Context;
import android.content.res.Configuration;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;


public class Movie implements Serializable {


    public String getPosterPath() {
        return String.format("https://image.tmdb.org/t/p/w342%s", posterPathSuffix);
    }

    public String getBackdropPath() {
        return String.format("https://image.tmdb.org/t/p/w780%s", backdropPathSuffix);
    }

    public String getStreamPageImagePath(Context context) {
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
    private int id;
    private double rating;

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
