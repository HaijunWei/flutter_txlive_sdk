import 'package:pigeon/pigeon.dart';

@ConfigurePigeon(PigeonOptions(
  dartOut: 'lib/src/live_base_messages.dart',
  objcHeaderOut: 'ios/Classes/LiveBaseMessages.h',
  objcSourceOut: 'ios/Classes/LiveBaseMessages.m',
  objcOptions: ObjcOptions(prefix: 'HJ'),
  javaOut:
      'android/src/main/kotlin/com/haijunwei/flutter_txlive_sdk/LiveBaseMessages.java',
  javaOptions: JavaOptions(package: 'com.haijunwei.flutter_txlive_sdk'),
))
class SetLicenceMessage {
  late String url;
  late String key;
}

@HostApi()
abstract class LiveBaseApi {
  void setLicence(SetLicenceMessage msg);
}
