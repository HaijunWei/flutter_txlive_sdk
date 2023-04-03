//
//  LiveBasePlugin.m
//  flutter_txlive_sdk
//
//  Created by haijun on 2023/4/3.
//

#import "HJLiveBasePlugin.h"
#import <TXLiteAVSDK_Professional/TXLiveBase.h>

@implementation HJLiveBasePlugin

+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar> *)registrar {
    HJLiveBasePlugin* instance = [HJLiveBasePlugin new];
    [registrar publish:instance];
    HJLiveBaseApiSetup(registrar.messenger, instance);
}

- (void)setLicenceMsg:(HJSetLicenceMessage *)msg error:(FlutterError * _Nullable __autoreleasing *)error {
    [TXLiveBase setLicenceURL:msg.url key:msg.key];
}

@end
