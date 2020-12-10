package ru.sima_land.spb.market.VideoPlayer;

import android.content.Intent;

import androidx.annotation.NonNull;

import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import ru.sima_land.spb.market.FullscreenVideoActivity;

public class VideoModule extends ReactContextBaseJavaModule {
  ReactApplicationContext context = getReactApplicationContext();

  public VideoModule(ReactApplicationContext reactContext) {
    super(reactContext);
  }

  @NonNull
  @Override
  public String getName() {
    return "VideoModule";
  }

  @ReactMethod
  public void openVideoPlayer(String uri) {
    Intent intent = new Intent(context, FullscreenVideoActivity.class);
    if (intent.resolveActivity(context.getPackageManager()) != null) {
      intent.putExtra("videoUri", uri);
      intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
      context.startActivity(intent);
    }
  }
}
