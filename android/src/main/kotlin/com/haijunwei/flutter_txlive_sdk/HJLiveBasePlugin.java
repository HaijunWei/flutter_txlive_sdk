package com.haijunwei.flutter_txlive_sdk;

import static com.haijunwei.flutter_txlive_sdk.LiveBaseMessages.*;

import androidx.annotation.NonNull;

import com.tencent.rtmp.TXLiveBase;

import io.flutter.embedding.engine.plugins.FlutterPlugin;

public class HJLiveBasePlugin implements FlutterPlugin, LiveBaseApi {
    private FlutterPluginBinding mFlutterPluginBinding;

    @Override
    public void setLicence(@NonNull SetLicenceMessage msg) {
        TXLiveBase.getInstance().setLicence(mFlutterPluginBinding.getApplicationContext(), msg.getUrl(), msg.getKey());
    }

    @Override
    public void onAttachedToEngine(@NonNull FlutterPluginBinding binding) {
        mFlutterPluginBinding = binding;
        LiveBaseApi.setup(binding.getBinaryMessenger(), this);
    }

    @Override
    public void onDetachedFromEngine(@NonNull FlutterPluginBinding binding) {

    }
}
