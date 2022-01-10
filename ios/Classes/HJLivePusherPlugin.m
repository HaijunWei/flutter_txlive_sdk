//
//  HJLivePusherPlugin.m
//  flutter_txlive_sdk
//
//  Created by haijun on 2021/12/25.
//

#import "HJLivePusherPlugin.h"
#import "LivePusherMessages.h"
#import <TXLiteAVSDK_Professional/V2TXLivePusher.h>

@interface HJLivePusher : NSObject

@property (nonatomic, assign) NSInteger pusherId;
@property (nonatomic, strong) V2TXLivePusher *pusher;
@property (nonatomic, readonly) bool disposed;

@end

@implementation HJLivePusher


- (instancetype)init {
    if (self = [super init]) {
        _pusherId = 1;
        _pusher = [[V2TXLivePusher alloc] initWithLiveMode:V2TXLiveMode_RTC];
    }
    return self;
}


- (void)startPush:(NSString *)url {
    int code = [_pusher startPush:url];
    NSLog(@"开始推流 %d", code);
}

- (void)stopPush {
    [_pusher stopPush];
}

- (void)dispose {
    [_pusher stopPush];
    _disposed = YES;
}

@end


@interface HJLivePusherPlugin () <HJLivePusherApi>

@property(readonly, weak, nonatomic) NSObject<FlutterTextureRegistry>* registry;
@property(readonly, weak, nonatomic) NSObject<FlutterBinaryMessenger>* messenger;
@property(readonly, strong, nonatomic) NSMutableDictionary* pushers;
@property(readonly, strong, nonatomic) NSObject<FlutterPluginRegistrar>* registrar;

@end

@implementation HJLivePusherPlugin

+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar> *)registrar {
    HJLivePusherPlugin* instance = [[HJLivePusherPlugin alloc] initWithRegistrar:registrar];
    [registrar publish:instance];
    [registrar addApplicationDelegate:instance];
    HJLivePusherApiSetup(registrar.messenger, instance);
}

- (instancetype)initWithRegistrar:(NSObject<FlutterPluginRegistrar> *)registrar {
    self = [super init];
    NSAssert(self, @"super init cannot be nil");
    _registry = [registrar textures];
    _messenger = [registrar messenger];
    _registrar = registrar;
    _pushers = [NSMutableDictionary dictionaryWithCapacity:1];
    return self;
}

#pragma mark -

- (nullable HJLivePusherMessage *)createWithError:(FlutterError * _Nullable __autoreleasing * _Nonnull)error {
    HJLivePusher *pusher = [HJLivePusher new];
    _pushers[@(pusher.pusherId)] = pusher;
    HJLivePusherMessage *msg = [HJLivePusherMessage new];
    msg.pusherId = @(pusher.pusherId);
    return msg;
}

- (void)startPushMsg:(nonnull HJStartMessage *)msg error:(FlutterError * _Nullable __autoreleasing * _Nonnull)error {
    HJLivePusher* pusher = _pushers[msg.pusherId];
    [pusher startPush:msg.url];
}

- (void)stopPushMsg:(nonnull HJLivePusherMessage *)msg error:(FlutterError * _Nullable __autoreleasing * _Nonnull)error {
    HJLivePusher* pusher = _pushers[msg.pusherId];
    [pusher stopPush];
}

- (void)disposeMsg:(nonnull HJLivePusherMessage *)msg error:(FlutterError * _Nullable __autoreleasing * _Nonnull)error {
    HJLivePusher* pusher = _pushers[msg.pusherId];
    [_pushers removeObjectForKey:msg.pusherId];
    dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(1 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
        if (!pusher.disposed) {
            [pusher dispose];
        }
    });
}



@end
