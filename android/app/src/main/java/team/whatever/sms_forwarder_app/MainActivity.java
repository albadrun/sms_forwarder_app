package team.whatever.sms_forwarder_app;

import android.os.Build;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import io.flutter.embedding.android.FlutterActivity;
import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.plugin.common.EventChannel;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugins.GeneratedPluginRegistrant;

import team.whatever.sms_forwarder_app.phone_call.PhoneCallEventHandler;
import team.whatever.sms_forwarder_app.phone_call.PhoneCallCallback;

public class MainActivity extends FlutterActivity {
    private static final String GET_PHONE_PERMISSION = "getPhonePermission";
    private static final String GET_PHONE_AND_SMS_PERMISSION = "getPhoneAndSmsPermission";
    private static final String METHOD_SUCCESS = "success";
    private static final String METHOD = "team.whatever.sms_forwarder_app/methodChannel";

    private static final String PHONE_CALL_EVENT = "phoneCallEvent";
    private static final String EVENT = "team.whatever.sms_forwarder_app/eventChannel";

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public void configureFlutterEngine(@NonNull FlutterEngine flutterEngine) {
        GeneratedPluginRegistrant.registerWith(flutterEngine);

        new MethodChannel(flutterEngine.getDartExecutor().getBinaryMessenger(), METHOD)
                .setMethodCallHandler((call, result) -> {
                    if (call.method.equals(GET_PHONE_PERMISSION)) {
                        PermissionHandler permissionHandler = new PermissionHandler(MainActivity.this);
                        permissionHandler.checkOrRequestPermission(new String[]{
                                PermissionHandler.READ_PHONE_STATE,
                                PermissionHandler.READ_CALL_LOG,
                                PermissionHandler.PROCESS_OUTGOING_CALLS}
                        );
                        result.success(METHOD_SUCCESS);

                    } else if (call.method.equals(GET_PHONE_AND_SMS_PERMISSION)) {
                        PermissionHandler permissionHandler = new PermissionHandler(MainActivity.this);
                        permissionHandler.checkOrRequestPermission(new String[]{
                                PermissionHandler.READ_PHONE_STATE,
                                PermissionHandler.READ_CALL_LOG,
                                PermissionHandler.PROCESS_OUTGOING_CALLS,
                                PermissionHandler.READ_SMS,
                                PermissionHandler.SEND_SMS,
                                PermissionHandler.RECEIVE_SMS}
                        );
                        result.success(METHOD_SUCCESS);
                    }
                });

        new EventChannel(flutterEngine.getDartExecutor().getBinaryMessenger(), EVENT)
                .setStreamHandler(new EventChannel.StreamHandler() {
                    @Override
                    public void onListen(Object arguments, EventChannel.EventSink events) {
                        PhoneCallEventHandler.addPhoneCallCallback(new PhoneCallCallback() {
                            @Override
                            public void run(String phoneNumber, String date) {
                                String event = PHONE_CALL_EVENT + "|" + phoneNumber + "," + date;
                                events.success(event);
                            }
                        });
                    }

                    @Override
                    public void onCancel(Object arguments) {}
                });
    }
}
