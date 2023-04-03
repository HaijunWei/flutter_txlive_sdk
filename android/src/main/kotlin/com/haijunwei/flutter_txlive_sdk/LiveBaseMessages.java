// Autogenerated from Pigeon (v3.1.0), do not edit directly.
// See also: https://pub.dev/packages/pigeon

package com.haijunwei.flutter_txlive_sdk;

import android.util.Log;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
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
public class LiveBaseMessages {

  /** Generated class from Pigeon that represents data sent in messages. */
  public static class SetLicenceMessage {
    private @NonNull String url;
    public @NonNull String getUrl() { return url; }
    public void setUrl(@NonNull String setterArg) {
      if (setterArg == null) {
        throw new IllegalStateException("Nonnull field \"url\" is null.");
      }
      this.url = setterArg;
    }

    private @NonNull String key;
    public @NonNull String getKey() { return key; }
    public void setKey(@NonNull String setterArg) {
      if (setterArg == null) {
        throw new IllegalStateException("Nonnull field \"key\" is null.");
      }
      this.key = setterArg;
    }

    /** Constructor is private to enforce null safety; use Builder. */
    private SetLicenceMessage() {}
    public static final class Builder {
      private @Nullable String url;
      public @NonNull Builder setUrl(@NonNull String setterArg) {
        this.url = setterArg;
        return this;
      }
      private @Nullable String key;
      public @NonNull Builder setKey(@NonNull String setterArg) {
        this.key = setterArg;
        return this;
      }
      public @NonNull SetLicenceMessage build() {
        SetLicenceMessage pigeonReturn = new SetLicenceMessage();
        pigeonReturn.setUrl(url);
        pigeonReturn.setKey(key);
        return pigeonReturn;
      }
    }
    @NonNull Map<String, Object> toMap() {
      Map<String, Object> toMapResult = new HashMap<>();
      toMapResult.put("url", url);
      toMapResult.put("key", key);
      return toMapResult;
    }
    static @NonNull SetLicenceMessage fromMap(@NonNull Map<String, Object> map) {
      SetLicenceMessage pigeonResult = new SetLicenceMessage();
      Object url = map.get("url");
      pigeonResult.setUrl((String)url);
      Object key = map.get("key");
      pigeonResult.setKey((String)key);
      return pigeonResult;
    }
  }
  private static class LiveBaseApiCodec extends StandardMessageCodec {
    public static final LiveBaseApiCodec INSTANCE = new LiveBaseApiCodec();
    private LiveBaseApiCodec() {}
    @Override
    protected Object readValueOfType(byte type, ByteBuffer buffer) {
      switch (type) {
        case (byte)128:         
          return SetLicenceMessage.fromMap((Map<String, Object>) readValue(buffer));
        
        default:        
          return super.readValueOfType(type, buffer);
        
      }
    }
    @Override
    protected void writeValue(ByteArrayOutputStream stream, Object value)     {
      if (value instanceof SetLicenceMessage) {
        stream.write(128);
        writeValue(stream, ((SetLicenceMessage) value).toMap());
      } else 
{
        super.writeValue(stream, value);
      }
    }
  }

  /** Generated interface from Pigeon that represents a handler of messages from Flutter.*/
  public interface LiveBaseApi {
    void setLicence(@NonNull SetLicenceMessage msg);

    /** The codec used by LiveBaseApi. */
    static MessageCodec<Object> getCodec() {
      return LiveBaseApiCodec.INSTANCE;
    }

    /** Sets up an instance of `LiveBaseApi` to handle messages through the `binaryMessenger`. */
    static void setup(BinaryMessenger binaryMessenger, LiveBaseApi api) {
      {
        BasicMessageChannel<Object> channel =
            new BasicMessageChannel<>(binaryMessenger, "dev.flutter.pigeon.LiveBaseApi.setLicence", getCodec());
        if (api != null) {
          channel.setMessageHandler((message, reply) -> {
            Map<String, Object> wrapped = new HashMap<>();
            try {
              ArrayList<Object> args = (ArrayList<Object>)message;
              SetLicenceMessage msgArg = (SetLicenceMessage)args.get(0);
              if (msgArg == null) {
                throw new NullPointerException("msgArg unexpectedly null.");
              }
              api.setLicence(msgArg);
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
    errorMap.put("details", "Cause: " + exception.getCause() + ", Stacktrace: " + Log.getStackTraceString(exception));
    return errorMap;
  }
}