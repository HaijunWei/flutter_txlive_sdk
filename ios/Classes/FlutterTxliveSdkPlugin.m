#import "FlutterTxliveSdkPlugin.h"
#if __has_include(<flutter_txlive_sdk/flutter_txlive_sdk-Swift.h>)
#import <flutter_txlive_sdk/flutter_txlive_sdk-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "flutter_txlive_sdk-Swift.h"
#endif

@implementation FlutterTxliveSdkPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftFlutterTxliveSdkPlugin registerWithRegistrar:registrar];
}
@end
