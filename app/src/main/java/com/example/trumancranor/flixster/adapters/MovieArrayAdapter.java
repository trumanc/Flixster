package com.example.trumancranor.flixster.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.trumancranor.flixster.R;
import com.example.trumancranor.flixster.models.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

import static com.example.trumancranor.flixster.R.id.tvOverview;
import static com.example.trumancranor.flixster.R.id.tvTitle;

public class MovieArrayAdapter extends ArrayAdapter<Movie> {

    public MovieArrayAdapter(Context context, List<Movie> movies) {
        super(context, android.R.layout.simple_list_item_1, movies);
    }

    private static class ViewHolder {
        TextView title;
        TextView overview;
        ImageView image;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        Movie movie = getItem(position);
        ViewHolder viewHolder;

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.item_movie, parent, false);

            viewHolder = new ViewHolder();
            viewHolder.title = (TextView) convertView.findViewById(tvTitle);
            viewHolder.overview = (TextView) convertView.findViewById(tvOverview);
            viewHolder.image = (ImageView) convertView.findViewById(R.id.ivMovieImage);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.title.setText(movie.getOriginalTitle());
        viewHolder.overview.setText(movie.getOverview());
        viewHolder.image.setImageResource(0);
        Picasso.with(getContext())
                .load(movie.getResponsiveImagePath(getContext()))
                .placeholder(R.drawable.clapboard)
                .into(viewHolder.image);

        return convertView;
    }
}
