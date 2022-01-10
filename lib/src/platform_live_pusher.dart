import 'package:flutter/material.dart';
import 'package:flutter_txlive_sdk/src/live_pusher_messages.dart';

class PlatformLivePusher {
  static final PlatformLivePusher _instance = PlatformLivePusher();
  static PlatformLivePusher get instance => _instance;

  final LivePusherApi _api = LivePusherApi();

  Future<int?> create() async {
    final response = await _api.create();
    return response.pusherId;
  }

  Future<void> startPush(
    int pusherId, {
    required String url,
  }) {
    return _api.startPush(
      StartMessage()
        ..pusherId = pusherId
        ..url = url,
    );
  }

  Future<void> stopPush(int pusherId) {
    return _api.stopPush(LivePusherMessage()..pusherId = pusherId);
  }

  Future<void> dispose(int pusherId) {
    return _api.dispose(LivePusherMessage()..pusherId = pusherId);
  }
}
