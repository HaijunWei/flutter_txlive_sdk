package com.haijunwei.flutter_txlive_sdk;

import static com.tencent.rtmp.TXLivePlayer.PLAY_TYPE_LIVE_FLV;
import static com.tencent.rtmp.TXLivePlayer.PLAY_TYPE_LIVE_RTMP;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.SurfaceTexture;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.util.Size;
import android.view.PixelCopy;
import android.view.Surface;
import android.view.View;

import com.tencent.rtmp.ITXLivePlayListener;
import com.tencent.rtmp.ITXVodPlayListener;
import com.tencent.rtmp.TXLiveConstants;
import com.tencent.rtmp.TXLivePlayer;
import com.tencent.rtmp.TXVodPlayer;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Timer;
import java.util.TimerTask;

import io.flutter.plugin.common.EventChannel;
import io.flutter.view.TextureRegistry;

public class HJVideoPlayer implements ITXLivePlayListener, ITXVodPlayListener {
    private static final String TAG = "HJVideoPlayer";

    private TXLivePlayer livePlayer;
    private TXVodPlayer vodPlayer;

    private Surface surface;

    private final TextureRegistry.SurfaceTextureEntry textureEntry;

    private QueuingEventSink eventSink = new QueuingEventSink();

    private final EventChannel eventChannel;

    private PlayerMessages.PlayerType type;
    private boolean isPlaying = false;
    private Activity activity;

    boolean disposed = false;

    private Timer timer = new Timer();

    private int mSurfaceWidth, mSurfaceHeight = 0;
    private int videoWidth, videoHeight = 0;

    public HJVideoPlayer(PlayerMessages.PlayerType type,
                         EventChannel eventChannel,
                         TextureRegistry.SurfaceTextureEntry textureEntry,
                         Activity activity) {
        this.eventChannel = eventChannel;
        this.textureEntry = textureEntry;
        this.activity = activity;
        this.type = type;
        SurfaceTexture surfaceTexture = textureEntry.surfaceTexture();
        surface = new Surface(surfaceTexture);

        if (type == PlayerMessages.PlayerType.vod) {
            vodPlayer = new TXVodPlayer(activity);
            vodPlayer.setSurface(surface);
            vodPlayer.enableHardwareDecode(true);
            vodPlayer.setVodListener(this);
        } else {
            livePlayer = new TXLivePlayer(activity);
            livePlayer.setSurface(surface);
            livePlayer.enableHardwareDecode(true);
            livePlayer.setPlayListener(this);
            livePlayer.setVideoRenderListener(new TXLivePlayer.ITXLivePlayVideoRenderListener() {
                @Override
                public void onRenderVideoFrame(TXLivePlayer.TXLiteAVTexture texture) {
                    int width = texture.width;
                    int height = texture.height;
                    if (width != mSurfaceWidth || height != mSurfaceHeight) {
                        Log.d(TAG, "onRenderVideoFrame: width=" + texture.width + ",height=" + texture.height);
                        livePlayer.setSurfaceSize(width, height);
                        textureEntry.surfaceTexture().setDefaultBufferSize(width, height);
                        mSurfaceWidth = width;
                        mSurfaceHeight = height;
                    }
                }
            }, null);
        }
        setupVideoPlayer(eventChannel, textureEntry);
    }

    private void setupVideoPlayer(
            EventChannel eventChannel,
            TextureRegistry.SurfaceTextureEntry textureEntry) {
        eventChannel.setStreamHandler(
                new EventChannel.StreamHandler() {
                    @Override
                    public void onListen(Object o, EventChannel.EventSink sink) {
                        eventSink.setDelegate(sink);
                    }

                    @Override
                    public void onCancel(Object o) {
                        eventSink.setDelegate(null);
                    }
                });
    }

    private void updatePlayingState() {
        if (this.type == PlayerMessages.PlayerType.vod) {
            if (isPlaying) {
                vodPlayer.resume();
            } else {
                vodPlayer.pause();
            }
        } else {
            if (isPlaying) {
                livePlayer.resume();
                TimerTask task = new TimerTask() {
                    @Override
                    public void run() {
                        long position = livePlayer.getCurrentRenderPts();
                        Map<String, Object> event = new HashMap<>();
                        event.put("event", "progressUpdate");
                        event.put("position", position);
                        activity.runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                eventSink.success(event);
                            }
                        });

                    }
                };
                timer = new Timer();
                timer.scheduleAtFixedRate(task, 0, 1000);
            } else {
                livePlayer.pause();
                timer.cancel();
            }
        }
    }

    void startPlay(String url, PlayerMessages.LiveType type) {
        if (this.type == PlayerMessages.PlayerType.vod) {
            vodPlayer.seek(0);
            vodPlayer.startVodPlay(url);
        } else {
            if (type == PlayerMessages.LiveType.rtmp) {
                livePlayer.startLivePlay(url, PLAY_TYPE_LIVE_RTMP);
            } else {
                livePlayer.startLivePlay(url, PLAY_TYPE_LIVE_FLV);
            }
        }
    }

    void pause() {
        isPlaying = false;
        updatePlayingState();
    }

    void resume() {
        isPlaying = true;
        updatePlayingState();
    }

    void stop() {
        isPlaying = false;
        if (this.type == PlayerMessages.PlayerType.vod) {
            vodPlayer.stopPlay(true);
        } else {
            livePlayer.stopPlay(true);
            timer.cancel();
        }
    }

    void seek(int time) {
        if (this.type == PlayerMessages.PlayerType.vod) {
            vodPlayer.seek(time);
        } else {
            livePlayer.seek(time);
        }
    }

    void setLooping(boolean looping) {
        if (this.type == PlayerMessages.PlayerType.vod) {
            vodPlayer.setLoop(looping);
        }
    }

    void setPlaybackSpeed(float speed) {
        if (this.type == PlayerMessages.PlayerType.vod) {
            vodPlayer.setRate(speed);
        }
    }

    void setVolume(float volume) {
        if (this.type == PlayerMessages.PlayerType.vod) {
            vodPlayer.setAudioPlayoutVolume((int) (volume * 100));
        } else {
            livePlayer.setVolume((int) (volume * 100));
        }
    }

    void snapshot(IVideoSnapshotListener listener, Boolean portrait) {
        View cv = activity.getWindow().getDecorView();
        long time = System.currentTimeMillis();
        int width = videoWidth;
        int height = videoHeight;
        Bitmap bitmap = Bitmap.createBitmap(width, height, Bitmap.Config.RGB_565);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            int finalWidth = width;
            int finalHeight = height;
            PixelCopy.request(surface, bitmap, new PixelCopy.OnPixelCopyFinishedListener() {
                @Override
                public void onPixelCopyFinished(int i) {
                    String filename = "hj_video_player_" + System.currentTimeMillis() + ".png";
                    File file = new File(activity.getCacheDir(), filename);
                    new Thread(){
                        @Override
                        public void run() {
                            try {
                                bitmap.compress(Bitmap.CompressFormat.JPEG, 80, new FileOutputStream(file));
                                activity.runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        listener.onSnapshot(file.getPath(), finalWidth, finalHeight);
                                    }
                                });
                            } catch (FileNotFoundException e) {
                                e.printStackTrace();
                            }

                        }
                    }.start();
                }
            }, new Handler(Looper.getMainLooper()));
        }
    }


    void dispose() {
        stop();
        disposed = true;
        timer.cancel();
        eventSink.setDelegate(null);
        textureEntry.release();
    }

    @Override
    public void onPlayEvent(int i, Bundle bundle) {
        Log.d(TAG, "--- onPlayEvent" + i + bundle.toString());
        switch (i) {
            case TXLiveConstants.PLAY_EVT_PLAY_BEGIN: {
                Map<String, Object> event = new HashMap<>();
                event.put("event", "ready");
                eventSink.success(event);
                break;
            }
            case TXLiveConstants.PLAY_EVT_CHANGE_RESOLUTION: {
                Map<String, Object> event = new HashMap<>();
                event.put("event", "resolutionUpdate");
                event.put("width", bundle.get("EVT_PARAM1"));
                event.put("height", bundle.get("EVT_PARAM2"));
                eventSink.success(event);
                videoWidth = (int)bundle.get("EVT_PARAM1");
                videoHeight = (int)bundle.get("EVT_PARAM2");
                break;
            }
            case TXLiveConstants.PLAY_ERR_NET_DISCONNECT: {
                Map<String, Object> event = new HashMap<>();
                event.put("event", "ended");
                eventSink.success(event);
                break;
            }
            default:
                break;
        }
    }

    @Override
    public void onNetStatus(Bundle bundle) {

    }

    @Override
    public void onPlayEvent(TXVodPlayer txVodPlayer, int i, Bundle bundle) {
        switch (i) {
            case TXLiveConstants.PLAY_EVT_RCV_FIRST_I_FRAME: {
                vodPlayer.pause();
                Map<String, Object> event = new HashMap<>();
                event.put("event", "ready");
                eventSink.success(event);
                break;
            }
            case TXLiveConstants.PLAY_EVT_CHANGE_RESOLUTION: {
                Map<String, Object> event = new HashMap<>();
                event.put("event", "resolutionUpdate");
                event.put("width", bundle.get("EVT_PARAM1"));
                event.put("height", bundle.get("EVT_PARAM2"));
                eventSink.success(event);
                videoWidth = (int)bundle.get("EVT_PARAM1");
                videoHeight = (int) bundle.get("EVT_PARAM2");
                break;
            }
            case TXLiveConstants.PLAY_EVT_PLAY_PROGRESS: {
                Map<String, Object> event = new HashMap<>();
                event.put("event", "progressUpdate");
                event.put("duration", bundle.getInt("EVT_PLAY_DURATION") * 1000);
                event.put("position", bundle.getInt("EVT_PLAY_PROGRESS") * 1000);
                event.put("buffered", bundle.getInt("PLAYABLE_DURATION") * 1000);
                eventSink.success(event);
                break;
            }
            case TXLiveConstants.PLAY_EVT_PLAY_END: {
                Map<String, Object> event = new HashMap<>();
                event.put("event", "ended");
                eventSink.success(event);
                break;
            }
            default:
                break;
        }
    }

    @Override
    public void onNetStatus(TXVodPlayer txVodPlayer, Bundle bundle) {

    }

    public interface IVideoSnapshotListener {
        void onSnapshot(String path, int width, int height);
    }
}
