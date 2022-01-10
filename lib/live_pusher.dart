import 'dart:async';

import 'src/platform_live_pusher.dart';

final PlatformLivePusher _platformLivePusher = PlatformLivePusher.instance;

class LivePusherController {
  static const int kUninitializedPusherId = -1;
  int _pusherId = kUninitializedPusherId;
  int get textureId => _pusherId;

  Completer<void>? _creatingCompleter;

  bool _initialized = false;
  bool get initialized => _initialized;

  Future<void> initialize() async {
    if (_initialized) return;
    if (_creatingCompleter != null) {
      await _creatingCompleter!.future;
      return;
    }
    _creatingCompleter = Completer<void>();

    _pusherId = await _platformLivePusher.create() ?? kUninitializedPusherId;
    _creatingCompleter!.complete(null);
    _initialized = true;
  }

  Future<void> startPush(String url) async {
    await _platformLivePusher.startPush(_pusherId, url: url);
  }

  Future<void> stopPush() async {
    await _platformLivePusher.stopPush(_pusherId);
  }

  void dispose() async {
    await _platformLivePusher.dispose(_pusherId);
  }
}
