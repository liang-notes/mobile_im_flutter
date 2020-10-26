import 'dart:async';
import 'package:flutter/services.dart';

class MobileImPlugin {
  static MobileImPlugin _instance;
  final MethodChannel _methodChannel;
  static StreamController streamController = StreamController.broadcast();

  MobileImPlugin._(this._methodChannel);

  static MobileImPlugin getInstance() {
    if (_instance == null) {
      final MethodChannel methodChannel =
          const MethodChannel('mobile_im_plugin')
            ..setMethodCallHandler(_methodHandler);
      _instance = MobileImPlugin._(methodChannel);
    }
    return _instance;
  }

  Future<String> get platformVersion async {
    final String version =
        await _methodChannel.invokeMethod('getPlatformVersion');
    return version;
  }

  static Future _methodHandler(MethodCall call) async {
    switch (call.method) {
      case 'onReceiveMessage':
        streamController.add(call.arguments);
        break;
      default:
        throw new UnsupportedError('Unrecognized Event');
    }
  }

  Future<Null> init({
    String appKey = '5418023dfd98c579b6001741',
    String serverIP = 'rbcore.52im.net',
    int serverPort = 8901,
    bool debug = false,
  }) async {
    await _methodChannel.invokeMethod('init', {
      'appKey': appKey,
      'serverIP': serverIP,
      'serverPort': serverPort,
      'debug': debug,
    });
  }

  Future<int> login(
    String userName,
    String password,
  ) async {
    final int status = await _methodChannel.invokeMethod('login', {
      'username': userName,
      'password': password,
    });
    return status;
  }

  Future<int> logout() async {
    final int status = await _methodChannel.invokeMethod('logout');
    return status;
  }

  Future<int> send(String message, String toUserId) async {
    final int status = await _methodChannel.invokeMethod('send', {
      'message': message,
      'toUserId': toUserId,
    });
    return status;
  }
}
