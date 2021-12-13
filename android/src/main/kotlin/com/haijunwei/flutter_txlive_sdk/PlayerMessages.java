// Autogenerated from Pigeon (v1.0.12), do not edit directly.
// See also: https://pub.dev/packages/pigeon

package com.haijunwei.flutter_txlive_sdk;

import io.flutter.plugin.common.BasicMessageChannel;
import io.flutter.plugin.common.BinaryMessenger;
import io.flutter.plugin.common.MessageCodec;
import io.flutter.plugin.common.StandardMessageCodec;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.Arrays;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.HashMap;

/** Generated class from Pigeon. */
@SuppressWarnings({"unused", "unchecked", "CodeBlock2Expr", "RedundantSuppression"})
public class PlayerMessages {

  public enum PlayerType {
    vod(0),
    live(1);

    private int index;
    private PlayerType(final int index) {
      this.index = index;
    }
  }

  public enum LiveType {
    rtmp(0),
    flv(1);

    private int index;
    private LiveType(final int index) {
      this.index = index;
    }
  }

  /** Generated class from Pigeon that represents data sent in messages. */
  public static class TextureMessage {
    private Long textureId;
    public Long getTextureId() { return textureId; }
    public void setTextureId(Long setterArg) { this.textureId = setterArg; }

    Map<String, Object> toMap() {
      Map<String, Object> toMapResult = new HashMap<>();
      toMapResult.put("textureId", textureId);
      return toMapResult;
    }
    static TextureMessage fromMap(Map<String, Object> map) {
      TextureMessage fromMapResult = new TextureMessage();
      Object textureId = map.get("textureId");
      fromMapResult.textureId = (textureId == null) ? null : ((textureId instanceof Integer) ? (Integer)textureId : (Long)textureId);
      return fromMapResult;
    }
  }

  /** Generated class from Pigeon that represents data sent in messages. */
  public static class CreateMessage {
    private PlayerType type;
    public PlayerType getType() { return type; }
    public void setType(PlayerType setterArg) { this.type = setterArg; }

    Map<String, Object> toMap() {
      Map<String, Object> toMapResult = new HashMap<>();
      toMapResult.put("type", type == null ? null : type.index);
      return toMapResult;
    }
    static CreateMessage fromMap(Map<String, Object> map) {
      CreateMessage fromMapResult = new CreateMessage();
      Object type = map.get("type");
      fromMapResult.type = type == null ? null : PlayerType.values()[(int)type];
      return fromMapResult;
    }
  }

  /** Generated class from Pigeon that represents data sent in messages. */
  public static class PlayMessage {
    private Long textureId;
    public Long getTextureId() { return textureId; }
    public void setTextureId(Long setterArg) { this.textureId = setterArg; }

    private LiveType liveType;
    public LiveType getLiveType() { return liveType; }
    public void setLiveType(LiveType setterArg) { this.liveType = setterArg; }

    private String uri;
    public String getUri() { return uri; }
    public void setUri(String setterArg) { this.uri = setterArg; }

    private String asset;
    public String getAsset() { return asset; }
    public void setAsset(String setterArg) { this.asset = setterArg; }

    private String packageName;
    public String getPackageName() { return packageName; }
    public void setPackageName(String setterArg) { this.packageName = setterArg; }

    Map<String, Object> toMap() {
      Map<String, Object> toMapResult = new HashMap<>();
      toMapResult.put("textureId", textureId);
      toMapResult.put("liveType", liveType == null ? null : liveType.index);
      toMapResult.put("uri", uri);
      toMapResult.put("asset", asset);
      toMapResult.put("packageName", packageName);
      return toMapResult;
    }
    static PlayMessage fromMap(Map<String, Object> map) {
      PlayMessage fromMapResult = new PlayMessage();
      Object textureId = map.get("textureId");
      fromMapResult.textureId = (textureId == null) ? null : ((textureId instanceof Integer) ? (Integer)textureId : (Long)textureId);
      Object liveType = map.get("liveType");
      fromMapResult.liveType = liveType == null ? null : LiveType.values()[(int)liveType];
      Object uri = map.get("uri");
      fromMapResult.uri = (String)uri;
      Object asset = map.get("asset");
      fromMapResult.asset = (String)asset;
      Object packageName = map.get("packageName");
      fromMapResult.packageName = (String)packageName;
      return fromMapResult;
    }
  }

  /** Generated class from Pigeon that represents data sent in messages. */
  public static class LoopingMessage {
    private Long textureId;
    public Long getTextureId() { return textureId; }
    public void setTextureId(Long setterArg) { this.textureId = setterArg; }

    private Boolean isLooping;
    public Boolean getIsLooping() { return isLooping; }
    public void setIsLooping(Boolean setterArg) { this.isLooping = setterArg; }

    Map<String, Object> toMap() {
      Map<String, Object> toMapResult = new HashMap<>();
      toMapResult.put("textureId", textureId);
      toMapResult.put("isLooping", isLooping);
      return toMapResult;
    }
    static LoopingMessage fromMap(Map<String, Object> map) {
      LoopingMessage fromMapResult = new LoopingMessage();
      Object textureId = map.get("textureId");
      fromMapResult.textureId = (textureId == null) ? null : ((textureId instanceof Integer) ? (Integer)textureId : (Long)textureId);
      Object isLooping = map.get("isLooping");
      fromMapResult.isLooping = (Boolean)isLooping;
      return fromMapResult;
    }
  }

  /** Generated class from Pigeon that represents data sent in messages. */
  public static class VolumeMessage {
    private Long textureId;
    public Long getTextureId() { return textureId; }
    public void setTextureId(Long setterArg) { this.textureId = setterArg; }

    private Double volume;
    public Double getVolume() { return volume; }
    public void setVolume(Double setterArg) { this.volume = setterArg; }

    Map<String, Object> toMap() {
      Map<String, Object> toMapResult = new HashMap<>();
      toMapResult.put("textureId", textureId);
      toMapResult.put("volume", volume);
      return toMapResult;
    }
    static VolumeMessage fromMap(Map<String, Object> map) {
      VolumeMessage fromMapResult = new VolumeMessage();
      Object textureId = map.get("textureId");
      fromMapResult.textureId = (textureId == null) ? null : ((textureId instanceof Integer) ? (Integer)textureId : (Long)textureId);
      Object volume = map.get("volume");
      fromMapResult.volume = (Double)volume;
      return fromMapResult;
    }
  }

  /** Generated class from Pigeon that represents data sent in messages. */
  public static class PlaybackSpeedMessage {
    private Long textureId;
    public Long getTextureId() { return textureId; }
    public void setTextureId(Long setterArg) { this.textureId = setterArg; }

    private Double speed;
    public Double getSpeed() { return speed; }
    public void setSpeed(Double setterArg) { this.speed = setterArg; }

    Map<String, Object> toMap() {
      Map<String, Object> toMapResult = new HashMap<>();
      toMapResult.put("textureId", textureId);
      toMapResult.put("speed", speed);
      return toMapResult;
    }
    static PlaybackSpeedMessage fromMap(Map<String, Object> map) {
      PlaybackSpeedMessage fromMapResult = new PlaybackSpeedMessage();
      Object textureId = map.get("textureId");
      fromMapResult.textureId = (textureId == null) ? null : ((textureId instanceof Integer) ? (Integer)textureId : (Long)textureId);
      Object speed = map.get("speed");
      fromMapResult.speed = (Double)speed;
      return fromMapResult;
    }
  }

  /** Generated class from Pigeon that represents data sent in messages. */
  public static class PositionMessage {
    private Long textureId;
    public Long getTextureId() { return textureId; }
    public void setTextureId(Long setterArg) { this.textureId = setterArg; }

    private Long position;
    public Long getPosition() { return position; }
    public void setPosition(Long setterArg) { this.position = setterArg; }

    Map<String, Object> toMap() {
      Map<String, Object> toMapResult = new HashMap<>();
      toMapResult.put("textureId", textureId);
      toMapResult.put("position", position);
      return toMapResult;
    }
    static PositionMessage fromMap(Map<String, Object> map) {
      PositionMessage fromMapResult = new PositionMessage();
      Object textureId = map.get("textureId");
      fromMapResult.textureId = (textureId == null) ? null : ((textureId instanceof Integer) ? (Integer)textureId : (Long)textureId);
      Object position = map.get("position");
      fromMapResult.position = (position == null) ? null : ((position instanceof Integer) ? (Integer)position : (Long)position);
      return fromMapResult;
    }
  }

  /** Generated class from Pigeon that represents data sent in messages. */
  public static class SnapshotMessage {
    private String path;
    public String getPath() { return path; }
    public void setPath(String setterArg) { this.path = setterArg; }

    Map<String, Object> toMap() {
      Map<String, Object> toMapResult = new HashMap<>();
      toMapResult.put("path", path);
      return toMapResult;
    }
    static SnapshotMessage fromMap(Map<String, Object> map) {
      SnapshotMessage fromMapResult = new SnapshotMessage();
      Object path = map.get("path");
      fromMapResult.path = (String)path;
      return fromMapResult;
    }
  }

  public interface Result<T> {
    void success(T result);
    void error(Throwable error);
  }
  private static class TencentVideoPlayerApiCodec extends StandardMessageCodec {
    public static final TencentVideoPlayerApiCodec INSTANCE = new TencentVideoPlayerApiCodec();
    private TencentVideoPlayerApiCodec() {}
    @Override
    protected Object readValueOfType(byte type, ByteBuffer buffer) {
      switch (type) {
        case (byte)128:         
          return CreateMessage.fromMap((Map<String, Object>) readValue(buffer));
        
        case (byte)129:         
          return LoopingMessage.fromMap((Map<String, Object>) readValue(buffer));
        
        case (byte)130:         
          return PlayMessage.fromMap((Map<String, Object>) readValue(buffer));
        
        case (byte)131:         
          return PlaybackSpeedMessage.fromMap((Map<String, Object>) readValue(buffer));
        
        case (byte)132:         
          return PositionMessage.fromMap((Map<String, Object>) readValue(buffer));
        
        case (byte)133:         
          return SnapshotMessage.fromMap((Map<String, Object>) readValue(buffer));
        
        case (byte)134:         
          return TextureMessage.fromMap((Map<String, Object>) readValue(buffer));
        
        case (byte)135:         
          return VolumeMessage.fromMap((Map<String, Object>) readValue(buffer));
        
        default:        
          return super.readValueOfType(type, buffer);
        
      }
    }
    @Override
    protected void writeValue(ByteArrayOutputStream stream, Object value)     {
      if (value instanceof CreateMessage) {
        stream.write(128);
        writeValue(stream, ((CreateMessage) value).toMap());
      } else 
      if (value instanceof LoopingMessage) {
        stream.write(129);
        writeValue(stream, ((LoopingMessage) value).toMap());
      } else 
      if (value instanceof PlayMessage) {
        stream.write(130);
        writeValue(stream, ((PlayMessage) value).toMap());
      } else 
      if (value instanceof PlaybackSpeedMessage) {
        stream.write(131);
        writeValue(stream, ((PlaybackSpeedMessage) value).toMap());
      } else 
      if (value instanceof PositionMessage) {
        stream.write(132);
        writeValue(stream, ((PositionMessage) value).toMap());
      } else 
      if (value instanceof SnapshotMessage) {
        stream.write(133);
        writeValue(stream, ((SnapshotMessage) value).toMap());
      } else 
      if (value instanceof TextureMessage) {
        stream.write(134);
        writeValue(stream, ((TextureMessage) value).toMap());
      } else 
      if (value instanceof VolumeMessage) {
        stream.write(135);
        writeValue(stream, ((VolumeMessage) value).toMap());
      } else 
{
        super.writeValue(stream, value);
      }
    }
  }

  /** Generated interface from Pigeon that represents a handler of messages from Flutter.*/
  public interface TencentVideoPlayerApi {
    TextureMessage create(CreateMessage msg);
    void play(PlayMessage msg);
    void resume(TextureMessage msg);
    void pause(TextureMessage msg);
    void stop(TextureMessage msg);
    void seekTo(PositionMessage msg);
    void setLooping(LoopingMessage msg);
    void setVolume(VolumeMessage msg);
    void setPlaybackSpeed(PlaybackSpeedMessage msg);
    void snapshot(TextureMessage msg, Result<SnapshotMessage> result);
    void dispose(TextureMessage msg);

    /** The codec used by TencentVideoPlayerApi. */
    static MessageCodec<Object> getCodec() {
      return TencentVideoPlayerApiCodec.INSTANCE;
    }

    /** Sets up an instance of `TencentVideoPlayerApi` to handle messages through the `binaryMessenger`. */
    static void setup(BinaryMessenger binaryMessenger, TencentVideoPlayerApi api) {
      {
        BasicMessageChannel<Object> channel =
            new BasicMessageChannel<>(binaryMessenger, "dev.flutter.pigeon.TencentVideoPlayerApi.create", getCodec());
        if (api != null) {
          channel.setMessageHandler((message, reply) -> {
            Map<String, Object> wrapped = new HashMap<>();
            try {
              ArrayList<Object> args = (ArrayList<Object>)message;
              CreateMessage msgArg = (CreateMessage)args.get(0);
              if (msgArg == null) {
                throw new NullPointerException("msgArg unexpectedly null.");
              }
              TextureMessage output = api.create(msgArg);
              wrapped.put("result", output);
            }
            catch (Error | RuntimeException exception) {
              wrapped.put("error", wrapError(exception));
            }
            reply.reply(wrapped);
          });
        } else {
          channel.setMessageHandler(null);
        }
      }
      {
        BasicMessageChannel<Object> channel =
            new BasicMessageChannel<>(binaryMessenger, "dev.flutter.pigeon.TencentVideoPlayerApi.play", getCodec());
        if (api != null) {
          channel.setMessageHandler((message, reply) -> {
            Map<String, Object> wrapped = new HashMap<>();
            try {
              ArrayList<Object> args = (ArrayList<Object>)message;
              PlayMessage msgArg = (PlayMessage)args.get(0);
              if (msgArg == null) {
                throw new NullPointerException("msgArg unexpectedly null.");
              }
              api.play(msgArg);
              wrapped.put("result", null);
            }
            catch (Error | RuntimeException exception) {
              wrapped.put("error", wrapError(exception));
            }
            reply.reply(wrapped);
          });
        } else {
          channel.setMessageHandler(null);
        }
      }
      {
        BasicMessageChannel<Object> channel =
            new BasicMessageChannel<>(binaryMessenger, "dev.flutter.pigeon.TencentVideoPlayerApi.resume", getCodec());
        if (api != null) {
          channel.setMessageHandler((message, reply) -> {
            Map<String, Object> wrapped = new HashMap<>();
            try {
              ArrayList<Object> args = (ArrayList<Object>)message;
              TextureMessage msgArg = (TextureMessage)args.get(0);
              if (msgArg == null) {
                throw new NullPointerException("msgArg unexpectedly null.");
              }
              api.resume(msgArg);
              wrapped.put("result", null);
            }
            catch (Error | RuntimeException exception) {
              wrapped.put("error", wrapError(exception));
            }
            reply.reply(wrapped);
          });
        } else {
          channel.setMessageHandler(null);
        }
      }
      {
        BasicMessageChannel<Object> channel =
            new BasicMessageChannel<>(binaryMessenger, "dev.flutter.pigeon.TencentVideoPlayerApi.pause", getCodec());
        if (api != null) {
          channel.setMessageHandler((message, reply) -> {
            Map<String, Object> wrapped = new HashMap<>();
            try {
              ArrayList<Object> args = (ArrayList<Object>)message;
              TextureMessage msgArg = (TextureMessage)args.get(0);
              if (msgArg == null) {
                throw new NullPointerException("msgArg unexpectedly null.");
              }
              api.pause(msgArg);
              wrapped.put("result", null);
            }
            catch (Error | RuntimeException exception) {
              wrapped.put("error", wrapError(exception));
            }
            reply.reply(wrapped);
          });
        } else {
          channel.setMessageHandler(null);
        }
      }
      {
        BasicMessageChannel<Object> channel =
            new BasicMessageChannel<>(binaryMessenger, "dev.flutter.pigeon.TencentVideoPlayerApi.stop", getCodec());
        if (api != null) {
          channel.setMessageHandler((message, reply) -> {
            Map<String, Object> wrapped = new HashMap<>();
            try {
              ArrayList<Object> args = (ArrayList<Object>)message;
              TextureMessage msgArg = (TextureMessage)args.get(0);
              if (msgArg == null) {
                throw new NullPointerException("msgArg unexpectedly null.");
              }
              api.stop(msgArg);
              wrapped.put("result", null);
            }
            catch (Error | RuntimeException exception) {
              wrapped.put("error", wrapError(exception));
            }
            reply.reply(wrapped);
          });
        } else {
          channel.setMessageHandler(null);
        }
      }
      {
        BasicMessageChannel<Object> channel =
            new BasicMessageChannel<>(binaryMessenger, "dev.flutter.pigeon.TencentVideoPlayerApi.seekTo", getCodec());
        if (api != null) {
          channel.setMessageHandler((message, reply) -> {
            Map<String, Object> wrapped = new HashMap<>();
            try {
              ArrayList<Object> args = (ArrayList<Object>)message;
              PositionMessage msgArg = (PositionMessage)args.get(0);
              if (msgArg == null) {
                throw new NullPointerException("msgArg unexpectedly null.");
              }
              api.seekTo(msgArg);
              wrapped.put("result", null);
            }
            catch (Error | RuntimeException exception) {
              wrapped.put("error", wrapError(exception));
            }
            reply.reply(wrapped);
          });
        } else {
          channel.setMessageHandler(null);
        }
      }
      {
        BasicMessageChannel<Object> channel =
            new BasicMessageChannel<>(binaryMessenger, "dev.flutter.pigeon.TencentVideoPlayerApi.setLooping", getCodec());
        if (api != null) {
          channel.setMessageHandler((message, reply) -> {
            Map<String, Object> wrapped = new HashMap<>();
            try {
              ArrayList<Object> args = (ArrayList<Object>)message;
              LoopingMessage msgArg = (LoopingMessage)args.get(0);
              if (msgArg == null) {
                throw new NullPointerException("msgArg unexpectedly null.");
              }
              api.setLooping(msgArg);
              wrapped.put("result", null);
            }
            catch (Error | RuntimeException exception) {
              wrapped.put("error", wrapError(exception));
            }
            reply.reply(wrapped);
          });
        } else {
          channel.setMessageHandler(null);
        }
      }
      {
        BasicMessageChannel<Object> channel =
            new BasicMessageChannel<>(binaryMessenger, "dev.flutter.pigeon.TencentVideoPlayerApi.setVolume", getCodec());
        if (api != null) {
          channel.setMessageHandler((message, reply) -> {
            Map<String, Object> wrapped = new HashMap<>();
            try {
              ArrayList<Object> args = (ArrayList<Object>)message;
              VolumeMessage msgArg = (VolumeMessage)args.get(0);
              if (msgArg == null) {
                throw new NullPointerException("msgArg unexpectedly null.");
              }
              api.setVolume(msgArg);
              wrapped.put("result", null);
            }
            catch (Error | RuntimeException exception) {
              wrapped.put("error", wrapError(exception));
            }
            reply.reply(wrapped);
          });
        } else {
          channel.setMessageHandler(null);
        }
      }
      {
        BasicMessageChannel<Object> channel =
            new BasicMessageChannel<>(binaryMessenger, "dev.flutter.pigeon.TencentVideoPlayerApi.setPlaybackSpeed", getCodec());
        if (api != null) {
          channel.setMessageHandler((message, reply) -> {
            Map<String, Object> wrapped = new HashMap<>();
            try {
              ArrayList<Object> args = (ArrayList<Object>)message;
              PlaybackSpeedMessage msgArg = (PlaybackSpeedMessage)args.get(0);
              if (msgArg == null) {
                throw new NullPointerException("msgArg unexpectedly null.");
              }
              api.setPlaybackSpeed(msgArg);
              wrapped.put("result", null);
            }
            catch (Error | RuntimeException exception) {
              wrapped.put("error", wrapError(exception));
            }
            reply.reply(wrapped);
          });
        } else {
          channel.setMessageHandler(null);
        }
      }
      {
        BasicMessageChannel<Object> channel =
            new BasicMessageChannel<>(binaryMessenger, "dev.flutter.pigeon.TencentVideoPlayerApi.snapshot", getCodec());
        if (api != null) {
          channel.setMessageHandler((message, reply) -> {
            Map<String, Object> wrapped = new HashMap<>();
            try {
              ArrayList<Object> args = (ArrayList<Object>)message;
              TextureMessage msgArg = (TextureMessage)args.get(0);
              if (msgArg == null) {
                throw new NullPointerException("msgArg unexpectedly null.");
              }
              Result<SnapshotMessage> resultCallback = new Result<SnapshotMessage>() {
                public void success(SnapshotMessage result) {
                  wrapped.put("result", result);
                  reply.reply(wrapped);
                }
                public void error(Throwable error) {
                  wrapped.put("error", wrapError(error));
                  reply.reply(wrapped);
                }
              };

              api.snapshot(msgArg, resultCallback);
            }
            catch (Error | RuntimeException exception) {
              wrapped.put("error", wrapError(exception));
              reply.reply(wrapped);
            }
          });
        } else {
          channel.setMessageHandler(null);
        }
      }
      {
        BasicMessageChannel<Object> channel =
            new BasicMessageChannel<>(binaryMessenger, "dev.flutter.pigeon.TencentVideoPlayerApi.dispose", getCodec());
        if (api != null) {
          channel.setMessageHandler((message, reply) -> {
            Map<String, Object> wrapped = new HashMap<>();
            try {
              ArrayList<Object> args = (ArrayList<Object>)message;
              TextureMessage msgArg = (TextureMessage)args.get(0);
              if (msgArg == null) {
                throw new NullPointerException("msgArg unexpectedly null.");
              }
              api.dispose(msgArg);
              wrapped.put("result", null);
            }
            catch (Error | RuntimeException exception) {
              wrapped.put("error", wrapError(exception));
            }
            reply.reply(wrapped);
          });
        } else {
          channel.setMessageHandler(null);
        }
      }
    }
  }
  private static Map<String, Object> wrapError(Throwable exception) {
    Map<String, Object> errorMap = new HashMap<>();
    errorMap.put("message", exception.toString());
    errorMap.put("code", exception.getClass().getSimpleName());
    errorMap.put("details", null);
    return errorMap;
  }
}