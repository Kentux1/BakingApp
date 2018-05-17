package com.example.tiago.bakingapp.fragments;


import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.tiago.bakingapp.R;
import com.example.tiago.bakingapp.models.Recipe;
import com.example.tiago.bakingapp.models.Step;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelection;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import butterknife.BindDrawable;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class RecipeStepDetailFragment extends Fragment implements Player.EventListener {

    private static final String LOG_TAG = RecipeStepDetailFragment.class.getSimpleName();

    private Context mContext;
    private Recipe mSelectedRecipe;
    private Step mSelectedStep;
    private int mStepId;
    private Unbinder mUnbinder;
    private int mStepCount;
    private String mStepDescription;
    private String mVideoUrl;
    private String mThumbnailUrl;
    private SimpleExoPlayer mExoPlayer;
    private MediaSessionCompat mMediaSessionCompat;
    private PlaybackStateCompat.Builder mPlaybackStateCompatBuilder;
    private BandwidthMeter mBandwithMeter;
    private TrackSelector mTrackSelector;
    private long mPlayerPosition;

    @BindView(R.id.step_desc_text_view) TextView mStepDescriptionTextView;
    @BindView(R.id.step_detail_relative_layout) RelativeLayout mRelativeLayout;
    @BindView(R.id.recipe_exoplayer_view) SimpleExoPlayerView mExoPlayerView;
    @BindView(R.id.no_media_image_view) ImageView mNoMediaImageView;

    @BindDrawable(R.mipmap.ic_launcher) Drawable mDefaultImage;

    public RecipeStepDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mContext = context;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putLong("player_position", mPlayerPosition);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            ArrayList<Recipe> recipes = getArguments().getParcelableArrayList("selected_recipe");

            if (recipes != null) {
                mSelectedRecipe = recipes.get(0);
                mStepId = getArguments().getInt("selected_step");
                mStepCount = getArguments().getInt("step_count");

                getStepDetails();
            }
        }

        if (savedInstanceState != null) {
            mPlayerPosition = savedInstanceState.getLong("player_position");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_recipe_step_detail, container, false);
        mUnbinder = ButterKnife.bind(this, rootView);

        if (savedInstanceState == null) {
            createMediaPlayer();
        }

        displaySteps();

        return rootView;
    }

    public void getStepDetails() {
        mSelectedStep = mSelectedRecipe.getmRecipeStep().get(mStepId);
        mStepDescription = mSelectedStep.getmStepDescription();
        mVideoUrl = mSelectedStep.getmVideoUrl();
        mThumbnailUrl = mSelectedStep.getmVideoThumbnailUrl();
    }

    private void displaySteps() {
        if (mStepId > 0) {
            int index = mStepDescription.indexOf(". ");
            mStepDescription = mStepDescription.substring(index + 2);
        }

        mStepDescriptionTextView.setText(mStepDescription);
    }

    public void createMediaPlayer() {
        if (!mVideoUrl.isEmpty()) {
            ButterKnife.apply(mNoMediaImageView, VISIBILITY, View.GONE);

            initializeMediaSession();
            initializePlayer(Uri.parse(mVideoUrl));
        } else {
            ButterKnife.apply(mNoMediaImageView, VISIBILITY, View.VISIBLE);

            if (!mThumbnailUrl.isEmpty()) {
                Picasso.get()
                        .load(mThumbnailUrl)
                        .placeholder(mDefaultImage)
                        .error(mDefaultImage)
                        .into(mNoMediaImageView);
            } else {
                mNoMediaImageView.setImageDrawable(mDefaultImage);
            }
        }
    }

    private void initializeMediaSession() {
        mMediaSessionCompat = new MediaSessionCompat(mContext, LOG_TAG);

        mMediaSessionCompat.setFlags(MediaSessionCompat.FLAG_HANDLES_MEDIA_BUTTONS |
                MediaSessionCompat.FLAG_HANDLES_TRANSPORT_CONTROLS);
        mMediaSessionCompat.setMediaButtonReceiver(null);

        mPlaybackStateCompatBuilder = new PlaybackStateCompat.Builder()
                .setActions(
                        PlaybackStateCompat.ACTION_PLAY |
                                PlaybackStateCompat.ACTION_PAUSE |
                                PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS |
                                PlaybackStateCompat.ACTION_PLAY_PAUSE);

        mMediaSessionCompat.setPlaybackState(mPlaybackStateCompatBuilder.build());

        mMediaSessionCompat.setCallback(new MediaSessionCallback());

        mMediaSessionCompat.setActive(true);
    }

    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {
            TrackSelection.Factory videoTrackSelectionFactory = new AdaptiveTrackSelection.Factory(mBandwithMeter);
            mTrackSelector = new DefaultTrackSelector(videoTrackSelectionFactory);

            mExoPlayer = ExoPlayerFactory.newSimpleInstance(mContext, mTrackSelector);

            mExoPlayerView.setPlayer(mExoPlayer);

            mBandwithMeter = new DefaultBandwidthMeter();

            String userAgent = Util.getUserAgent(mContext, LOG_TAG);

            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(mContext, userAgent);
            MediaSource mediaSource = new ExtractorMediaSource.Factory(dataSourceFactory).createMediaSource(mediaUri);
            mExoPlayer.prepare(mediaSource);

            mExoPlayer.setPlayWhenReady(true);
            mExoPlayer.seekTo(mPlayerPosition);
        }
    }

    private class MediaSessionCallback extends MediaSessionCompat.Callback {
        @Override
        public void onPlay() {
            super.onPlay();
            mExoPlayer.setPlayWhenReady(true);
        }

        @Override
        public void onPause() {
            super.onPause();
            mExoPlayer.setPlayWhenReady(false);
        }

        @Override
        public void onSkipToPrevious() {
            super.onSkipToPrevious();
            mExoPlayer.seekTo(0);
        }
    }


    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

    }

    @Override
    public void onRepeatModeChanged(int repeatMode) {

    }

    @Override
    public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {

    }

    @Override
    public void onPositionDiscontinuity(int reason) {

    }

    @Override
    public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

    }

    @Override
    public void onSeekProcessed() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mUnbinder.unbind();
    }



    @Override
    public void onPause() {
        super.onPause();
        if (mExoPlayer != null) mPlayerPosition = mExoPlayer.getCurrentPosition();
        releasePlayer();
    }

    @Override
    public void onResume() {
        super.onResume();
        if ((Util.SDK_INT <= 23 || mExoPlayer == null)) {
            createMediaPlayer();
        }
    }
    private void releasePlayer() {
        if (mExoPlayer != null) {
            mPlayerPosition = mExoPlayer.getCurrentPosition();
        }

        if (mExoPlayer != null) {
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
        if (mMediaSessionCompat != null) {
            mMediaSessionCompat.setActive(false);
        }

        if (mTrackSelector != null) {
            mTrackSelector = null;
        }
    }

    private static final ButterKnife.Setter<View, Integer> VISIBILITY = new
            ButterKnife.Setter<View, Integer>() {
                @Override
                public void set(@NonNull View view, Integer value, int index) {
                    view.setVisibility(value);
                }
            };
}
