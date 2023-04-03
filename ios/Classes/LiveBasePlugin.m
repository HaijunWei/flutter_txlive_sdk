//
//  LiveBasePlugin.m
//  flutter_txlive_sdk
//
//  Created by haijun on 2023/4/3.
//

#import "LiveBasePlugin.h"
#import <TXLiteAVSDK_Professional/TXLiveBase.h>

@implementation LiveBasePlugin

+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar> *)registrar {
    LiveBasePlugin* instance = [LiveBasePlugin new];
    [registrar publish:instance];
    HJLiveBaseApiSetup(registrar.messenger, instance);
}

- (void)setLicenceMsg:(HJSetLicenceMessage *)msg error:(FlutterError * _Nullable __autoreleasing *)error {
    [TXLiveBase setLicenceURL:msg.url key:msg.key];
}

@end
