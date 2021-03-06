import 'package:pigeon/pigeon.dart';

@ConfigurePigeon(PigeonOptions(
  dartOut: 'lib/src/player_messages.dart',
  objcHeaderOut: 'ios/Classes/PlayerMessages.h',
  objcSourceOut: 'ios/Classes/PlayerMessages.m',
  objcOptions: ObjcOptions(prefix: 'HJ'),
  javaOut:
      'android/src/main/kotlin/com/haijunwei/flutter_txlive_sdk/PlayerMessages.java',
  javaOptions: JavaOptions(package: 'com.haijunwei.flutter_txlive_sdk'),
))
class TextureMessage {
  late int textureId;
}

enum PlayerType { vod, live }

enum LiveType { rtmp, flv }

class CreateMessage {
  late PlayerType type;
}

class PlayMessage {
  late int textureId;
  late LiveType liveType;
  String? uri;
  String? asset;
  String? packageName;
}

class LoopingMessage {
  late int textureId;
  late bool isLooping;
}

class VolumeMessage {
  late int textureId;
  late double volume;
}

class PlaybackSpeedMessage {
  late int textureId;
  late double speed;
}

class PositionMessage {
  late int textureId;
  late int position;
}

class BackgroundPlayMessage {
  late int textureId;
  late bool backgroundPlay;
}

class SnapshotMessage {
  late int textureId;
  late bool portrait;
}

class SnapshotResponseMessage {
  late String path;
  late int width;
  late int height;
}

@HostApi()
abstract class TencentVideoPlayerApi {
  TextureMessage create(CreateMessage msg);
  void play(PlayMessage msg);
  void resume(TextureMessage msg);
  void pause(TextureMessage msg);
  void stop(TextureMessage msg);
  void seekTo(PositionMessage msg);
  void setLooping(LoopingMessage msg);
  void setVolume(VolumeMessage msg);
  void setPlaybackSpeed(PlaybackSpeedMessage msg);
  void setBackgroundPlay(BackgroundPlayMessage msg);
  @async
  SnapshotResponseMessage snapshot(SnapshotMessage msg);
  void dispose(TextureMessage msg);
}
