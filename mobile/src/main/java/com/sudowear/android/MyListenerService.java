package com.sudowear.android;

import android.util.Log;

import com.google.android.gms.wearable.MessageEvent;
import com.google.android.gms.wearable.WearableListenerService;

public class MyListenerService extends WearableListenerService {

    private static final String TAG = MyListenerService.class.getSimpleName();

    public MyListenerService() {
    }

    @Override
    public void onMessageReceived(MessageEvent messageEvent) {

        Log.v(TAG, "onMessageReceived");

    }
}
