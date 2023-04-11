import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter_txlive_sdk/flutter_txlive_sdk.dart';
import 'package:flutter_txlive_sdk/video_player.dart';

void main() {
  runApp(const MyApp());
}

class MyApp extends StatefulWidget {
  const MyApp({Key? key}) : super(key: key);

  @override
  State<MyApp> createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  late HJVideoPlayerController controller;
  @override
  void initState() {
    super.initState();
    FlutterTxliveSdk.setLicence(
        'https://license.vod2.myqcloud.com/license/v2/1257800131_1/v_cube.license',
        'e89f04a50cfc68eca8d929f9003f552a');
    controller = HJVideoPlayerController(PlayerType.vod);
    controller.initialize().then((value) {
      controller.play(VideoDataSource(
          url:
              'https://sample-videos.com/video123/mp4/720/big_buck_bunny_720p_30mb.mp4'));
    });
  }

  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('Plugin example app'),
        ),
        body: Center(
          child: AspectRatio(
            aspectRatio: 16 / 9,
            child: Container(
              color: Colors.black,
              child: HJVideoPlayer(controller: controller),
            ),
          ),
        ),
      ),
    );
  }
}
