package com.haijunwei.flutter_txlive_sdk;

import static com.haijunwei.flutter_txlive_sdk.PlayerMessages.*;

import android.content.Context;
import android.util.Log;
import android.util.LongSparseArray;
import android.util.Size;

import androidx.annotation.NonNull;

import io.flutter.FlutterInjector;
import io.flutter.embedding.engine.plugins.FlutterPlugin;
import io.flutter.embedding.engine.plugins.activity.ActivityAware;
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding;
import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.EventChannel;
import io.flutter.view.TextureRegistry;

/** TencentVideoPlayerPlugin */
public class HJVideoPlayerPlugin implements FlutterPlugin, ActivityAware, TencentVideoPlayerApi {
  private static final String TAG = "HJVideoPlayerPlugin";
  private final LongSparseArray<HJVideoPlayer> players = new LongSparseArray<>();
  private HJVideoPlayerPlugin.FlutterState flutterState;
  private FlutterPluginBinding mFlutterPluginBinding;
  private ActivityPluginBinding mActivityPluginBinding;

  @Override
  public void onAttachedToEngine(@NonNull FlutterPluginBinding flutterPluginBinding) {
    mFlutterPluginBinding = flutterPluginBinding;
//    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
//      try {
//        HttpsURLConnection.setDefaultSSLSocketFactory(new CustomSSLSocketFactory());
//      } catch (KeyManagementException | NoSuchAlgorithmException e) {
//        Log.w(
//                TAG,
//                "Failed to enable TLSv1.1 and TLSv1.2 Protocols for API level 19 and below.\n"
//                        + "For more information about Socket Security, please consult the following link:\n"
//                        + "https://developer.android.com/reference/javax/net/ssl/SSLSocket",
//                e);
//      }
//    }

    final FlutterInjector injector = FlutterInjector.instance();
    this.flutterState =
            new HJVideoPlayerPlugin.FlutterState(
                    flutterPluginBinding.getApplicationContext(),
                    flutterPluginBinding.getBinaryMessenger(),
                    flutterPluginBinding.getTextureRegistry());
    flutterState.startListening(this, flutterPluginBinding.getBinaryMessenger());
  }

  @Override
  public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {
    if (flutterState == null) {
      Log.wtf(TAG, "Detached from the engine before registering to it.");
    }
    flutterState.stopListening(binding.getBinaryMessenger());
    flutterState = null;
    disposeAllPlayers();
  }


  @Override
  public void onAttachedToActivity(@NonNull ActivityPluginBinding binding) {
    mActivityPluginBinding = binding;
  }

  @Override
  public void onDetachedFromActivityForConfigChanges() {

  }

  @Override
  public void onReattachedToActivityForConfigChanges(@NonNull ActivityPluginBinding binding) {

  }

  @Override
  public void onDetachedFromActivity() {

  }

  @Override
  public TextureMessage create(CreateMessage msg) {
    TextureRegistry.SurfaceTextureEntry handle =
            flutterState.textureRegistry.createSurfaceTexture();
    EventChannel eventChannel =
            new EventChannel(
                    flutterState.binaryMessenger, "com.haijunwei/tencentVideoPlayer/videoEvents" + handle.id());
    HJVideoPlayer player = new HJVideoPlayer(msg.getType(), eventChannel, handle, mActivityPluginBinding.getActivity());
    players.put(handle.id(), player);
    TextureMessage message = new TextureMessage();
    message.setTextureId(handle.id());
    return message;
  }

  @Override
  public void play(PlayMessage msg) {
    HJVideoPlayer player = players.get(msg.getTextureId());
    String url = "";
    if (msg.getAsset() != null) {

    } else {
      url = msg.getUri();
    }
    player.startPlay(url, msg.getLiveType());
  }

  @Override
  public void resume(TextureMessage msg) {
    HJVideoPlayer player = players.get(msg.getTextureId());
    player.resume();
  }

  @Override
  public void pause(TextureMessage msg) {
    HJVideoPlayer player = players.get(msg.getTextureId());
    player.pause();
  }

  @Override
  public void stop(TextureMessage msg) {
    HJVideoPlayer player = players.get(msg.getTextureId());
    player.stop();
  }

  @Override
  public void seekTo(PositionMessage msg) {
    HJVideoPlayer player = players.get(msg.getTextureId());
    player.seek(msg.getPosition().intValue());
  }

  @Override
  public void setLooping(LoopingMessage msg) {
    HJVideoPlayer player = players.get(msg.getTextureId());
    player.setLooping(msg.getIsLooping());
  }

  @Override
  public void setVolume(VolumeMessage msg) {
    HJVideoPlayer player = players.get(msg.getTextureId());
    player.setVolume(msg.getVolume().floatValue());
  }

  @Override
  public void setPlaybackSpeed(PlaybackSpeedMessage msg) {
    HJVideoPlayer player = players.get(msg.getTextureId());
    player.setPlaybackSpeed(msg.getSpeed().floatValue());
  }


  @Override
  public void snapshot(SnapshotMessage msg, Result<SnapshotResponseMessage> result) {
    HJVideoPlayer player = players.get(msg.getTextureId());
    player.snapshot(new HJVideoPlayer.IVideoSnapshotListener() {
      @Override
      public void onSnapshot(String path, int width, int height) {
        SnapshotResponseMessage message = new SnapshotResponseMessage();
        message.setPath(path);
        message.setWidth((long)width);
        message.setHeight((long)height);
        result.success(message);
      }
    }, msg.getPortrait());
  }

  @Override
  public void dispose(TextureMessage msg) {
    HJVideoPlayer player = players.get(msg.getTextureId());
    player.dispose();
    players.remove(msg.getTextureId());
  }

  private void disposeAllPlayers() {
    for (int i = 0; i < players.size(); i++) {
      players.valueAt(i).dispose();
    }
    players.clear();
  }

  private static final class FlutterState {
    private final Context applicationContext;
    private final BinaryMessenger binaryMessenger;
    private final TextureRegistry textureRegistry;

    FlutterState(
            Context applicationContext,
            BinaryMessenger messenger,
            TextureRegistry textureRegistry) {
      this.applicationContext = applicationContext;
      this.binaryMessenger = messenger;
      this.textureRegistry = textureRegistry;
    }

    void startListening(HJVideoPlayerPlugin methodCallHandler, BinaryMessenger messenger) {
      TencentVideoPlayerApi.setup(messenger, methodCallHandler);
    }

    void stopListening(BinaryMessenger messenger) {
      TencentVideoPlayerApi.setup(messenger, null);
    }
  }
}
