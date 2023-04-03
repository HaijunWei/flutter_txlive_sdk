package com.haijunwei.flutter_txlive_sdk

import androidx.annotation.NonNull

import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodChannel

/** FlutterTxliveSdkPlugin */
class FlutterTxliveSdkPlugin : FlutterPlugin, ActivityAware {
    private var basePlugin: HJLiveBasePlugin = HJLiveBasePlugin()
    private var playerPlugin: HJVideoPlayerPlugin = HJVideoPlayerPlugin()

    override fun onAttachedToEngine(@NonNull flutterPluginBinding: FlutterPlugin.FlutterPluginBinding) {
        basePlugin.onAttachedToEngine(flutterPluginBinding)
        playerPlugin.onAttachedToEngine(flutterPluginBinding)
    }


    override fun onDetachedFromEngine(@NonNull binding: FlutterPlugin.FlutterPluginBinding) {
        basePlugin.onDetachedFromEngine(binding)
        playerPlugin.onDetachedFromEngine(binding)
    }

    override fun onAttachedToActivity(binding: ActivityPluginBinding) {
        playerPlugin.onAttachedToActivity(binding)
    }

    override fun onDetachedFromActivityForConfigChanges() {
        playerPlugin.onDetachedFromActivityForConfigChanges()
    }

    override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
        playerPlugin.onReattachedToActivityForConfigChanges(binding)
    }

    override fun onDetachedFromActivity() {
        playerPlugin.onDetachedFromActivity()
    }
}
