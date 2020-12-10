package ru.sima_land.spb.market;

import android.content.pm.ActivityInfo;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.Nullable;

import com.facebook.react.ReactActivity;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.extractor.ExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.AdaptiveTrackSelection;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.BandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultBandwidthMeter;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;

public class FullscreenVideoActivity extends ReactActivity{
  PlayerView playerView;
  ProgressBar progressBar;
  ImageView btnFullScreen;
  ImageView btnClosePlayerActivity;
  SimpleExoPlayer simpleExoPlayer;
  boolean flag = false;

  @Override
  protected void onCreate(@Nullable final Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    super.onBackPressed();
    String videoUri = getIntent().getStringExtra("videoUri");
    setContentView(R.layout.activity_fullscreen);
    playerView = findViewById(R.id.video_player);
    progressBar = findViewById(R.id.progress_bar);
    btnFullScreen = playerView.findViewById(R.id.btn_fullscreen);
    btnClosePlayerActivity = playerView.findViewById(R.id.btn_close_video);

    getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

    Uri videoUrl = Uri.parse(videoUri);

    LoadControl loadControl = new DefaultLoadControl();

    BandwidthMeter bandwidthMeter = new DefaultBandwidthMeter();

    TrackSelector trackSelector = new DefaultTrackSelector(
      new AdaptiveTrackSelection.Factory(bandwidthMeter)
    );

    simpleExoPlayer = ExoPlayerFactory.newSimpleInstance(
      getApplicationContext(), trackSelector, loadControl
    );

    DefaultHttpDataSourceFactory factory = new DefaultHttpDataSourceFactory(
      "exoplayer_video"
    );

    ExtractorsFactory extractorsFactory = new DefaultExtractorsFactory();

    MediaSource mediaSource = new ExtractorMediaSource(videoUrl, factory, extractorsFactory, null, null);

    playerView.setPlayer(simpleExoPlayer);

    playerView.setKeepScreenOn(true);

    simpleExoPlayer.prepare(mediaSource);

    simpleExoPlayer.getPlayWhenReady();

    simpleExoPlayer.addListener(new Player.EventListener() {
      @Override
      public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
        if (playbackState == Player.STATE_BUFFERING) {
          progressBar.setVisibility(View.VISIBLE);
        } else if (playbackState == Player.STATE_READY) {
          progressBar.setVisibility(View.GONE);
        }
      }
    });


  btnFullScreen.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
      if (flag) {
        btnFullScreen.setImageDrawable(getResources().getDrawable(R.drawable.ic_fullscreen));
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        flag = false;
      } else {
        btnFullScreen.setImageDrawable(getResources().getDrawable(R.drawable.ic_exit));
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        flag = true;
      }
    }
  });

    btnClosePlayerActivity.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View view) {
        finish();
      }
    });
  }


  @Override
  public void onBackPressed() {
    finish();
  }

  @Override
  protected void onPause() {
    super.onPause();
    simpleExoPlayer.setPlayWhenReady(false);
    simpleExoPlayer.getPlaybackState();
  }

  @Override
  protected void onRestart() {
    super.onRestart();
    simpleExoPlayer.setPlayWhenReady(true);
    simpleExoPlayer.getPlaybackState();
  }
}
