package com.example.trumancranor.flixster.models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;


/**
 * Created by truman_cranor on 10/18/16.
 */

public class Movie {

    public String getPosterPath() {
        return String.format("https://image.tmdb.org/t/p/w342%s", posterPathSuffix);
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public String getOverview() {
        return overview;
    }

    private String posterPathSuffix;
    private String originalTitle;
    private String overview;

    public Movie(JSONObject jsonObject) throws JSONException {
        this.posterPathSuffix = jsonObject.getString("poster_path");
        this.originalTitle = jsonObject.getString("original_title");
        this.overview = jsonObject.getString("overview");
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
