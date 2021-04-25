import 'dart:developer' as developer;

import 'package:sms_forwarder/channel.dart';
import 'package:telephony/telephony.dart';

class PhoneCallNotifier {
  MessageHandler _messageHandler;

  void _requestPhonePermission() {
    Channel.invokeMethod(Channel.getPhonePermission);
  }

  void _listen() {
    Channel.registerListener((event) {
      List<String> splitted = event.toString().split("|");
      String type = splitted[0];
      String msg = splitted[1];

      if (type == Channel.phoneCallEvent) {
        splitted = msg.split(",");
        String phoneNumber = splitted[0];
        String dateMillis = splitted[1];

        developer.log(phoneNumber);
        developer.log(dateMillis);

        _messageHandler(_convertToSmsMessage(phoneNumber, dateMillis));
      }
    });
  }

  // Adapter function to convert to SmsMessage
  SmsMessage _convertToSmsMessage(String phoneNumber, String dateMillis) {
    return SmsMessage.fromMap({
      "_id": "-",
      "address": "PhoneCallNotifier",
      "body": "Incoming call from " + phoneNumber,
      "date": dateMillis,
    }, DEFAULT_SMS_COLUMNS);
  }

  void start(messageHandler) {
    _messageHandler = messageHandler;

    _requestPhonePermission();
    _listen();
  }
}
