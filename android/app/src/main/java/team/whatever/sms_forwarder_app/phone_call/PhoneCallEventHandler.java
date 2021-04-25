package team.whatever.sms_forwarder_app.phone_call;

import android.content.Context;

import java.util.ArrayList;
import java.util.Date;

public class PhoneCallEventHandler extends PhoneCallReceiver {
    static ArrayList<PhoneCallCallback> phoneCallCallbacks;

    public PhoneCallEventHandler() {}

    public static void addPhoneCallCallback(PhoneCallCallback phoneCallCallback) {
        if (phoneCallCallbacks == null) {
            phoneCallCallbacks = new ArrayList<>();
        }
        phoneCallCallbacks.add(phoneCallCallback);
    }

    @Override
    protected void onIncomingCallStarted(Context ctx, String number, Date date) {
        long dateMillis = date.getTime();
        if (number != null) {
            for (PhoneCallCallback phoneCallCallback: phoneCallCallbacks) {
                phoneCallCallback.run(number, Long.toString(dateMillis));
            }
        }
    }
}
