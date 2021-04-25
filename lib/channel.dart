import 'package:flutter/services.dart';

typedef void EventListener(e);

class Channel {
  static const getPhonePermission = "getPhonePermission";
  static const getPhoneAndSmsPermission = "getPhoneAndSmsPermission";

  static const phoneCallEvent = "phoneCallEvent";

  static const _methodSuccess = "success";
  static const _methodChannel =
      const MethodChannel("team.whatever.sms_forwarder_app/methodChannel");
  static const _eventChannel =
      const EventChannel("team.whatever.sms_forwarder_app/eventChannel");
  static var _phoneCallEventListener =
      List<EventListener>.empty(growable: true);
  static var _isBroadcastRun = false;

  static Future<void> invokeMethod(method) async {
    final String result = await _methodChannel.invokeMethod(method);
    if (result != _methodSuccess) {
      throw ("Error when invoking method!");
    }
  }

  static void _broadcastEvent(event) {
    _phoneCallEventListener.forEach((e) {
      e(event);
    });
  }

  static registerListener(listener) {
    _phoneCallEventListener.add(listener);

    if (!_isBroadcastRun) {
      _eventChannel.receiveBroadcastStream().listen(_broadcastEvent);
      _isBroadcastRun = true;
    }
  }
}
