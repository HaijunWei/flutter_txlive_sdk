import 'src/live_base_messages.dart';

class FlutterTxliveSdk {
  static final LiveBaseApi _api = LiveBaseApi();

  static void setLicence(String url, String key) {
    _api.setLicence(SetLicenceMessage(url: url, key: key));
  }
}
