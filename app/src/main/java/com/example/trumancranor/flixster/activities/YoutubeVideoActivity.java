package com.example.trumancranor.flixster.activities;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.trumancranor.flixster.BuildConfig;
import com.example.trumancranor.flixster.R;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import butterknife.BindView;
import butterknife.ButterKnife;

public class YoutubeVideoActivity extends YouTubeBaseActivity {
    private static final String ARG_VIDEO_KEY = "video_key";
    @BindView(R.id.player) YouTubePlayerView player;

    public static Intent intentForVideo(Context context, String videoKey) {
        Intent intent = new Intent(context, YoutubeVideoActivity.class);
        intent.putExtra(ARG_VIDEO_KEY, videoKey);
        return intent;
    }
    @Override
    protected void onCreate(Bundle bundle) {
        super.onCreate(bundle);
        setContentView(R.layout.youtube_player);
        ButterKnife.bind(this);

        Intent intent = getIntent();
        String videoKey = intent.getStringExtra(ARG_VIDEO_KEY);
        if (videoKey == null) {
            Toast.makeText(this, "This movie doesn't have a trailer :(", Toast.LENGTH_SHORT).show();
            finish();
        }

        player.initialize(BuildConfig.YOUTUBE_API_KEY,
                new YouTubePlayer.OnInitializedListener() {
                    @Override
                    public void onInitializationSuccess(YouTubePlayer.Provider provider,
                                                        YouTubePlayer youTubePlayer, boolean b) {
                        youTubePlayer.cueVideo(videoKey);
                    }

                    @Override
                    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                                        YouTubeInitializationResult youTubeInitializationResult) {
                        Toast.makeText(YoutubeVideoActivity.this, "Is YouTube installed on your device?",
                                Toast.LENGTH_SHORT).show();
                        finish();
                    }
                });
    }
}
