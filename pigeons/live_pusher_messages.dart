import 'package:pigeon/pigeon.dart';

@ConfigurePigeon(PigeonOptions(
  dartOut: 'lib/src/live_pusher_messages.dart',
  objcHeaderOut: 'ios/Classes/LivePusherMessages.h',
  objcSourceOut: 'ios/Classes/LivePusherMessages.m',
  objcOptions: ObjcOptions(prefix: 'HJ'),
  javaOut:
      'android/src/main/kotlin/com/haijunwei/flutter_txlive_sdk/LivePusherMessages.java',
  javaOptions: JavaOptions(package: 'com.haijunwei.flutter_txlive_sdk'),
))
class LivePusherMessage {
  late int pusherId;
}

class StartMessage {
  late int pusherId;
  late String url;
}

@HostApi()
abstract class LivePusherApi {
  LivePusherMessage create();
  void startPush(StartMessage msg);
  void stopPush(LivePusherMessage msg);
  void dispose(LivePusherMessage msg);
}
