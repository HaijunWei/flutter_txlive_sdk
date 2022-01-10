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
public class LivePusherMessages {

  /** Generated class from Pigeon that represents data sent in messages. */
  public static class LivePusherMessage {
    private Long pusherId;
    public Long getPusherId() { return pusherId; }
    public void setPusherId(Long setterArg) { this.pusherId = setterArg; }

    Map<String, Object> toMap() {
      Map<String, Object> toMapResult = new HashMap<>();
      toMapResult.put("pusherId", pusherId);
      return toMapResult;
    }
    static LivePusherMessage fromMap(Map<String, Object> map) {
      LivePusherMessage fromMapResult = new LivePusherMessage();
      Object pusherId = map.get("pusherId");
      fromMapResult.pusherId = (pusherId == null) ? null : ((pusherId instanceof Integer) ? (Integer)pusherId : (Long)pusherId);
      return fromMapResult;
    }
  }

  /** Generated class from Pigeon that represents data sent in messages. */
  public static class StartMessage {
    private Long pusherId;
    public Long getPusherId() { return pusherId; }
    public void setPusherId(Long setterArg) { this.pusherId = setterArg; }

    private String url;
    public String getUrl() { return url; }
    public void setUrl(String setterArg) { this.url = setterArg; }

    Map<String, Object> toMap() {
      Map<String, Object> toMapResult = new HashMap<>();
      toMapResult.put("pusherId", pusherId);
      toMapResult.put("url", url);
      return toMapResult;
    }
    static StartMessage fromMap(Map<String, Object> map) {
      StartMessage fromMapResult = new StartMessage();
      Object pusherId = map.get("pusherId");
      fromMapResult.pusherId = (pusherId == null) ? null : ((pusherId instanceof Integer) ? (Integer)pusherId : (Long)pusherId);
      Object url = map.get("url");
      fromMapResult.url = (String)url;
      return fromMapResult;
    }
  }
  private static class LivePusherApiCodec extends StandardMessageCodec {
    public static final LivePusherApiCodec INSTANCE = new LivePusherApiCodec();
    private LivePusherApiCodec() {}
    @Override
    protected Object readValueOfType(byte type, ByteBuffer buffer) {
      switch (type) {
        case (byte)128:         
          return LivePusherMessage.fromMap((Map<String, Object>) readValue(buffer));
        
        case (byte)129:         
          return StartMessage.fromMap((Map<String, Object>) readValue(buffer));
        
        default:        
          return super.readValueOfType(type, buffer);
        
      }
    }
    @Override
    protected void writeValue(ByteArrayOutputStream stream, Object value)     {
      if (value instanceof LivePusherMessage) {
        stream.write(128);
        writeValue(stream, ((LivePusherMessage) value).toMap());
      } else 
      if (value instanceof StartMessage) {
        stream.write(129);
        writeValue(stream, ((StartMessage) value).toMap());
      } else 
{
        super.writeValue(stream, value);
      }
    }
  }

  /** Generated interface from Pigeon that represents a handler of messages from Flutter.*/
  public interface LivePusherApi {
    LivePusherMessage create();
    void startPush(StartMessage msg);
    void stopPush(LivePusherMessage msg);
    void dispose(LivePusherMessage msg);

    /** The codec used by LivePusherApi. */
    static MessageCodec<Object> getCodec() {
      return LivePusherApiCodec.INSTANCE;
    }

    /** Sets up an instance of `LivePusherApi` to handle messages through the `binaryMessenger`. */
    static void setup(BinaryMessenger binaryMessenger, LivePusherApi api) {
      {
        BasicMessageChannel<Object> channel =
            new BasicMessageChannel<>(binaryMessenger, "dev.flutter.pigeon.LivePusherApi.create", getCodec());
        if (api != null) {
          channel.setMessageHandler((message, reply) -> {
            Map<String, Object> wrapped = new HashMap<>();
            try {
              LivePusherMessage output = api.create();
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
            new BasicMessageChannel<>(binaryMessenger, "dev.flutter.pigeon.LivePusherApi.startPush", getCodec());
        if (api != null) {
          channel.setMessageHandler((message, reply) -> {
            Map<String, Object> wrapped = new HashMap<>();
            try {
              ArrayList<Object> args = (ArrayList<Object>)message;
              StartMessage msgArg = (StartMessage)args.get(0);
              if (msgArg == null) {
                throw new NullPointerException("msgArg unexpectedly null.");
              }
              api.startPush(msgArg);
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
            new BasicMessageChannel<>(binaryMessenger, "dev.flutter.pigeon.LivePusherApi.stopPush", getCodec());
        if (api != null) {
          channel.setMessageHandler((message, reply) -> {
            Map<String, Object> wrapped = new HashMap<>();
            try {
              ArrayList<Object> args = (ArrayList<Object>)message;
              LivePusherMessage msgArg = (LivePusherMessage)args.get(0);
              if (msgArg == null) {
                throw new NullPointerException("msgArg unexpectedly null.");
              }
              api.stopPush(msgArg);
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
            new BasicMessageChannel<>(binaryMessenger, "dev.flutter.pigeon.LivePusherApi.dispose", getCodec());
        if (api != null) {
          channel.setMessageHandler((message, reply) -> {
            Map<String, Object> wrapped = new HashMap<>();
            try {
              ArrayList<Object> args = (ArrayList<Object>)message;
              LivePusherMessage msgArg = (LivePusherMessage)args.get(0);
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