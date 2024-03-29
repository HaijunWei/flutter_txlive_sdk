#import "PlayerMessages.h"
#import "HJVideoPlayerPlugin.h"
#import <stdatomic.h>
#import <libkern/OSAtomic.h>
#import <AVFAudio/AVFAudio.h>
#import <TXLiteAVSDK_Professional/TXVodPlayer.h>
#import <TXLiteAVSDK_Professional/TXLivePlayer.h>
#import <TXLiteAVSDK_Professional/V2TXLivePlayerObserver.h>

@interface HJVideoPlayer : NSObject <FlutterTexture, FlutterStreamHandler, TXVideoCustomProcessDelegate, TXLivePlayListener, TXVodPlayListener, V2TXLivePlayerObserver>

@property(nonatomic) int64_t textureId;
@property(nonatomic, weak, readonly) NSObject<FlutterTextureRegistry>* registry;

@property (nonatomic, assign) HJPlayerType type;
@property (nonatomic, strong) TXVodPlayer *vodPlayer;
@property (nonatomic, strong) TXLivePlayer *livePlayer;

@property (nonatomic) FlutterEventChannel* eventChannel;
@property (nonatomic) FlutterEventSink eventSink;


@property (nonatomic, readonly) bool disposed;
@property (nonatomic, readonly) bool isPlaying;

// 最新的一帧
@property (nonatomic) CVPixelBufferRef latestPixelBuffer;
// 旧的一帧
@property (nonatomic) CVPixelBufferRef lastBuffer;

@property (nonatomic, assign) BOOL needGetResolution;

@end

@implementation HJVideoPlayer

- (instancetype)initWithType:(HJPlayerType)type registry:(NSObject<FlutterTextureRegistry> *)registry {
    if (self = [super init]) {
        _type = type;
        _registry = registry;
        if (type == HJPlayerTypeVod) {
            _vodPlayer = [TXVodPlayer new];
            _vodPlayer.vodDelegate = self;
            _vodPlayer.videoProcessDelegate = self;
            _vodPlayer.enableHWAcceleration = YES;
        } else if (type == HJPlayerTypeLive) {
            _livePlayer = [TXLivePlayer new];
            _livePlayer.delegate = self;
            _livePlayer.videoProcessDelegate = self;
            [_livePlayer setVolume:100];
        }
        _disposed = false;
    }
    return self;
}

- (void)dispose {
    [self.vodPlayer stopPlay];
    [self.livePlayer stopPlay];
    [_eventChannel setStreamHandler:nil];
    _disposed = true;
}

- (CVPixelBufferRef _Nullable)copyPixelBuffer {
    CVPixelBufferRef pixelBuffer = _latestPixelBuffer;
    while (!OSAtomicCompareAndSwapPtrBarrier(pixelBuffer, nil,
                                             (void **)&_latestPixelBuffer)) {
        pixelBuffer = _latestPixelBuffer;
    }
    return pixelBuffer;
}

- (void)onTextureUnregistered:(NSObject<FlutterTexture>*)texture {
    dispatch_async(dispatch_get_main_queue(), ^{
        [self dispose];
    });
}

- (FlutterError * _Nullable)onCancelWithArguments:(id _Nullable)arguments {
    self.eventSink = nil;
    return nil;
}

- (FlutterError * _Nullable)onListenWithArguments:(id _Nullable)arguments eventSink:(nonnull FlutterEventSink)events {
    self.eventSink = events;
    return nil;
}

- (void)updatePlayingState {
    if (self.isPlaying) {
        [self.vodPlayer resume];
        [self.livePlayer resume];
    } else {
        [self.vodPlayer pause];
        [self.livePlayer pause];
    }
}

#pragma mark -

- (void)startPlayWithURL:(NSString *)url liveType:(HJLiveType)type {
    if (self.type == HJPlayerTypeVod) {
        [self.vodPlayer seek:0];
        [self.vodPlayer startVodPlay:url];
    } else {
        self.needGetResolution = true;
        if (type == HJLiveTypeRtmp) {
            [self.livePlayer startLivePlay:url type:PLAY_TYPE_LIVE_RTMP];
        } else {
            [self.livePlayer startLivePlay:url type:PLAY_TYPE_LIVE_FLV];
        }
    }
}

- (void)pause {
    _isPlaying = false;
    [self updatePlayingState];
}

- (void)resume {
    _isPlaying = true;
    [self updatePlayingState];
}

- (void)stop {
    _isPlaying = false;
    if (self.type == HJPlayerTypeVod) {
        [self.vodPlayer stopPlay];
    } else {
        [self.livePlayer stopPlay];
    }
}

- (void)seek:(int)time {
    [self.vodPlayer seek:time];
}

- (void)setLooping:(BOOL)looping {
    self.vodPlayer.loop = looping;
}

- (void)setPlaybackSpeed:(float)speed {
    if (self.type == HJPlayerTypeVod) {
        [self.vodPlayer setRate:speed];
    } else {
        [self.livePlayer setRate:speed];
    }
}

- (void)setVolume:(float)volume {
    if (self.type == HJPlayerTypeVod) {
        [self.vodPlayer setAudioPlayoutVolume:volume * 100];
    } else {
        [self.livePlayer setVolume:volume * 100];
    }
}

- (void)snapshot:(void (^)(NSString *, int width, int height))completion {
    UIImage *image = [self imageFromPixelBuffer:_lastBuffer];
    if (image == nil) {
        completion(@"", 0, 0);
        return;
    }
    NSString *tempDir = NSTemporaryDirectory();
    tempDir = [tempDir stringByAppendingPathComponent:[NSString stringWithFormat:@"hj_video_player_%ld.png", (NSInteger)[[NSDate date] timeIntervalSince1970]]];
    [UIImagePNGRepresentation(image) writeToFile:tempDir atomically:YES];
    completion(tempDir, image.size.width, image.size.height);
}

#pragma mark - TXVideoCustomProcessDelegate

- (BOOL)onPlayerPixelBuffer:(CVPixelBufferRef)pixelBuffer {
    if (_lastBuffer == nil) {
        _lastBuffer = CVPixelBufferRetain(pixelBuffer);
        CFRetain(pixelBuffer);
    } else if (_lastBuffer != pixelBuffer) {
        CVPixelBufferRelease(_lastBuffer);
        _lastBuffer = CVPixelBufferRetain(pixelBuffer);
        CFRetain(pixelBuffer);
    }

    CVPixelBufferRef newBuffer = pixelBuffer;

    CVPixelBufferRef old = self.latestPixelBuffer;
    while (!OSAtomicCompareAndSwapPtrBarrier(old, newBuffer,
                                             (void **)&_latestPixelBuffer)) {
        old = _latestPixelBuffer;
    }

    if (old && old != pixelBuffer) {
        CFRelease(old);
    }
    if (self.needGetResolution) {
        self.needGetResolution = NO;
        CGSize size = [self sizeFromPixelBuffer:pixelBuffer];
        self.eventSink(@{
            @"event": @"resolutionUpdate",
            @"width": @(size.width),
            @"height": @(size.height)
        });
    }
    
    if (_textureId >= 0) {
        [_registry textureFrameAvailable:_textureId];
    }
    
    return NO;
}

- (void)onNetStatus:(NSDictionary *)param {
}

- (void)onPlayEvent:(int)EvtID withParam:(NSDictionary *)param {
//    NSLog(@"--- evtId %d，param %@", EvtID, param);
    switch (EvtID) {
        case PLAY_EVT_PLAY_BEGIN:
            if (self.eventSink != nil) {
                self.eventSink(@{
                  @"event" : @"ready"
                });
            }
            break;
        case PLAY_EVT_PLAY_PROGRESS: // 视频进度变化
            if (self.eventSink != nil) {
                self.eventSink(@{
                  @"event" : @"progressUpdate",
                  @"position": @([self.livePlayer getCurrentRenderPts]),
                });
            }
            break;
        case PLAY_ERR_NET_DISCONNECT:
            if (self.eventSink != nil) {
                self.eventSink(@{
                  @"event" : @"ended"
                });
            }
            break;
        default:
            break;
    }
}

- (void)onNetStatus:(TXVodPlayer *)player withParam:(NSDictionary *)param {
//    NSLog(@"--- onNetStatus %@", param);
}

- (void)onPlayEvent:(TXVodPlayer *)player event:(int)EvtID withParam:(NSDictionary *)param {
//    NSLog(@"--- onPlayEvent evtId %d", EvtID);
//    NSLog(@"--- onPlayEvent evtId %d，param %@", EvtID, param);
    switch (EvtID) {
        case PLAY_EVT_RCV_FIRST_I_FRAME:
            [player pause];
            if (self.eventSink != nil) {
                self.eventSink(@{
                    @"event": @"ready"
                });
            }
            break;
        case PLAY_EVT_CHANGE_RESOLUTION: //视频分辨率发生变化（分辨率在 EVT_PARAM 参数中）
            if (self.eventSink != nil) {
                self.eventSink(@{
                    @"event": @"resolutionUpdate",
                    @"width": param[@"EVT_PARAM1"],
                    @"height": param[@"EVT_PARAM2"],
                });
            }
            break;
        case PLAY_EVT_PLAY_PROGRESS: // 视频进度变化
            if (self.eventSink != nil) {
                NSInteger duration = [param[@"EVT_PLAY_DURATION"] floatValue] * 1000;
                NSInteger position = [param[@"EVT_PLAY_PROGRESS"] floatValue] * 1000;
                self.eventSink(@{
                  @"event" : @"progressUpdate",
                  @"duration": @(duration),
                  @"position": @(position),
                  @"buffered": @((NSInteger)([param[@"PLAYABLE_DURATION"] floatValue] * 1000)),
                });
            }
            break;
        case PLAY_EVT_PLAY_END:
            if (self.eventSink != nil) {
                self.eventSink(@{@"event": @"ended"});
            }
            break;
        case -6003:
            if (self.eventSink != nil) {
                self.eventSink(@{@"event": @"failedToLoad"});
            }
            break;
        default:
            break;
        }
}

- (void)onPlayer:(TXVodPlayer *)player airPlayErrorDidOccur:(TX_VOD_PLAYER_AIRPLAY_ERROR_TYPE)errorType withParam:(NSDictionary *)param {
    
}


- (void)onPlayer:(TXVodPlayer *)player airPlayStateDidChange:(TX_VOD_PLAYER_AIRPLAY_STATE)airPlayState withParam:(NSDictionary *)param { 
    
}


- (void)onPlayer:(TXVodPlayer *)player pictureInPictureErrorDidOccur:(TX_VOD_PLAYER_PIP_ERROR_TYPE)errorType withParam:(NSDictionary *)param { 
    
}


- (void)onPlayer:(TXVodPlayer *)player pictureInPictureStateDidChange:(TX_VOD_PLAYER_PIP_STATE)pipState withParam:(NSDictionary *)param {
    
}

#pragma mark - Helpers

- (CGSize)sizeFromPixelBuffer:(CVPixelBufferRef)pixelBufferRef {
    return CGSizeMake(CVPixelBufferGetWidth(pixelBufferRef), CVPixelBufferGetHeight(pixelBufferRef));
}

- (UIImage *)imageFromPixelBuffer:(CVPixelBufferRef)pixelBufferRef {
    if (pixelBufferRef == NULL) return nil;
    CIImage *ciImage = [CIImage imageWithCVPixelBuffer:pixelBufferRef];

    CIContext *temporaryContext = [CIContext contextWithOptions:nil];
    CGImageRef videoImage = [temporaryContext createCGImage:ciImage fromRect:CGRectMake(0, 0, CVPixelBufferGetWidth(pixelBufferRef), CVPixelBufferGetHeight(pixelBufferRef))];
    UIImage *uiImage = [UIImage imageWithCGImage:videoImage];
    CGImageRelease(videoImage);
    return uiImage;
}

@end


@interface HJVideoPlayerPlugin () <HJTencentVideoPlayerApi, FlutterApplicationLifeCycleDelegate>

@property(readonly, weak, nonatomic) NSObject<FlutterTextureRegistry>* registry;
@property(readonly, weak, nonatomic) NSObject<FlutterBinaryMessenger>* messenger;
@property(readonly, strong, nonatomic) NSMutableDictionary* players;
@property(readonly, strong, nonatomic) NSObject<FlutterPluginRegistrar>* registrar;
@property(nonatomic, assign) BOOL backgroundPlay;

@end

@implementation HJVideoPlayerPlugin

+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar> *)registrar {
    HJVideoPlayerPlugin* instance = [[HJVideoPlayerPlugin alloc] initWithRegistrar:registrar];
    [registrar publish:instance];
    [registrar addApplicationDelegate:instance];
    HJTencentVideoPlayerApiSetup(registrar.messenger, instance);
}

- (instancetype)initWithRegistrar:(NSObject<FlutterPluginRegistrar> *)registrar {
    self = [super init];
    NSAssert(self, @"super init cannot be nil");
    _registry = [registrar textures];
    _messenger = [registrar messenger];
    _registrar = registrar;
    _players = [NSMutableDictionary dictionaryWithCapacity:1];
    return self;
}

- (void)detachFromEngineForRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
    for (NSNumber* textureId in _players.allKeys) {
        HJVideoPlayer* player = _players[textureId];
        [player dispose];
    }
    [_players removeAllObjects];
}

- (HJTextureMessage *)onPlayerSetup:(HJVideoPlayer *)player {
    int64_t textureId = [_registry registerTexture:player];
    player.textureId = textureId;
    FlutterEventChannel* eventChannel = [FlutterEventChannel
                                         eventChannelWithName:[NSString stringWithFormat:@"com.haijunwei/tencentVideoPlayer/videoEvents%lld", textureId]
                                         binaryMessenger:_messenger];
    [eventChannel setStreamHandler:player];
    player.eventChannel = eventChannel;
    _players[@(textureId)] = player;
    HJTextureMessage* result = [[HJTextureMessage alloc] init];
    result.textureId = @(textureId);
    return result;
}

#pragma mark - Messages

- (nullable HJTextureMessage *)createMsg:(nonnull HJCreateMessage *)msg error:(FlutterError * _Nullable __autoreleasing * _Nonnull)error {
    HJVideoPlayer *player = [[HJVideoPlayer alloc] initWithType:msg.type registry:_registry];
    return [self onPlayerSetup:player];
}

- (void)disposeMsg:(nonnull HJTextureMessage *)msg error:(FlutterError * _Nullable __autoreleasing * _Nonnull)error {
    HJVideoPlayer* player = _players[msg.textureId];
    [_registry unregisterTexture:msg.textureId.intValue];
    [_players removeObjectForKey:msg.textureId];
    dispatch_after(dispatch_time(DISPATCH_TIME_NOW, (int64_t)(1 * NSEC_PER_SEC)), dispatch_get_main_queue(), ^{
        if (!player.disposed) {
            [player dispose];
        }
    });
}

- (void)pauseMsg:(nonnull HJTextureMessage *)msg error:(FlutterError * _Nullable __autoreleasing * _Nonnull)error {
    HJVideoPlayer* player = _players[msg.textureId];
    [player pause];
}

- (void)playMsg:(nonnull HJPlayMessage *)msg error:(FlutterError * _Nullable __autoreleasing * _Nonnull)error {
    HJVideoPlayer* player = _players[msg.textureId];
    NSString *url;
    if (msg.asset) {
        NSString* assetPath;
        if (msg.packageName) {
            assetPath = [_registrar lookupKeyForAsset:msg.asset fromPackage:msg.packageName];
        } else {
            assetPath = [_registrar lookupKeyForAsset:msg.asset];
        }
        NSString* path = [[NSBundle mainBundle] pathForResource:assetPath ofType:nil];
        url = [NSURL URLWithString:path].absoluteString;
    } else {
        url = msg.uri;
    }
    [player startPlayWithURL:url liveType:msg.liveType];
}

- (void)resumeMsg:(nonnull HJTextureMessage *)msg error:(FlutterError * _Nullable __autoreleasing * _Nonnull)error {
    HJVideoPlayer* player = _players[msg.textureId];
    [player resume];
}

- (void)stopMsg:(nonnull HJTextureMessage *)msg error:(FlutterError * _Nullable __autoreleasing * _Nonnull)error {
    HJVideoPlayer* player = _players[msg.textureId];
    [player stop];
}

- (void)seekToMsg:(nonnull HJPositionMessage *)msg error:(FlutterError * _Nullable __autoreleasing * _Nonnull)error {
    HJVideoPlayer* player = _players[msg.textureId];
    [player seek:msg.position.intValue];
}

- (void)setLoopingMsg:(nonnull HJLoopingMessage *)msg error:(FlutterError * _Nullable __autoreleasing * _Nonnull)error {
    HJVideoPlayer* player = _players[msg.textureId];
    [player setLooping:msg.isLooping.boolValue];
}

- (void)setPlaybackSpeedMsg:(nonnull HJPlaybackSpeedMessage *)msg error:(FlutterError * _Nullable __autoreleasing * _Nonnull)error {
    HJVideoPlayer* player = _players[msg.textureId];
    [player setPlaybackSpeed:msg.speed.floatValue];
}

- (void)setVolumeMsg:(nonnull HJVolumeMessage *)msg error:(FlutterError * _Nullable __autoreleasing * _Nonnull)error {
    HJVideoPlayer* player = _players[msg.textureId];
    [player setVolume:msg.volume.floatValue];
}

- (void)setBackgroundPlayMsg:(HJBackgroundPlayMessage *)msg error:(FlutterError * _Nullable __autoreleasing *)error {
    self.backgroundPlay = [msg.backgroundPlay boolValue];
}

- (void)snapshotMsg:(HJSnapshotMessage *)msg completion:(void (^)(HJSnapshotResponseMessage * _Nullable, FlutterError * _Nullable))completion {
    HJVideoPlayer* player = _players[msg.textureId];
    [player snapshot:^(NSString *path, int width, int height) {
        HJSnapshotResponseMessage *message = [HJSnapshotResponseMessage new];
        message.path = path;
        message.width = @(width);
        message.height = @(height);
        completion(message, nil);
    }];
}


#pragma mark -

- (void)applicationWillResignActive:(UIApplication*)application {
    if (self.backgroundPlay) {
        [[UIApplication sharedApplication] beginReceivingRemoteControlEvents];
        [[AVAudioSession sharedInstance] setActive:YES error:nil];
    }
}

@end
