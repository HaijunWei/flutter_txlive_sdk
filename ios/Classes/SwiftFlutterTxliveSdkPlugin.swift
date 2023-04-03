import Flutter
import UIKit

public class SwiftFlutterTxliveSdkPlugin: NSObject, FlutterPlugin {
  public static func register(with registrar: FlutterPluginRegistrar) {
      HJVideoPlayerPlugin.register(with: registrar)
      HJLivePusherPlugin.register(with: registrar)
      LiveBasePlugin.register(with: registrar)
  }
}
