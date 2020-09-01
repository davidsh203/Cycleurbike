package com.example.cycleurbike.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.cycleurbike.R;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import java.util.ArrayList;
import java.util.List;

public class YouTube extends YouTubeBaseActivity {

    private static final String TAG = "YouTube";

    YouTubePlayerView mYouTubePlayerView;
    Button btnPlay;
    YouTubePlayer.OnInitializedListener mOnInitializedListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_you_tube);
        Log.d(TAG, "onCreate: Starting.");
        btnPlay = (Button) findViewById(R.id.btnPlay1);
        //btnPlay = (Button) findViewById(R.id.btnPlay2);
        mYouTubePlayerView = (YouTubePlayerView) findViewById(R.id.youtubePlay1);
        //mYouTubePlayerView = (YouTubePlayerView) findViewById(R.id.youtubePlay2);
        mOnInitializedListener = new YouTubePlayer.OnInitializedListener() {
            @Override
            public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
                Log.d(TAG, "onClick: Done initializing.");
                //youTubePlayer.loadPlaylist("PLQkwcJG4YTCQ6emtoqSZS2FVwZR9FT3BV");


                List<String> videoList = new ArrayList<>();
                videoList.add("wv-iUZEfiE8");
                videoList.add("wEZ7XdHO3GI");

                //youTubePlayer.loadVideo("wv-iUZEfiE8");
            }


            @Override
            public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
                Log.d(TAG, "onClick: Failed to initializing.");

            }
        };
        btnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: Initializing YouTube Player.");
                mYouTubePlayerView.initialize(YouTubeConfig.getApiKey(),mOnInitializedListener);
            }
        });

    }
}
