package com.sudowear.android;

import android.app.IntentService;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.wearable.Node;
import com.google.android.gms.wearable.NodeApi;
import com.google.android.gms.wearable.Wearable;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class MyIntentService extends IntentService {

    private static final String TAG = MyIntentService.class.getSimpleName();

    // TODO: Rename actions, choose action names that describe tasks that this
    // IntentService can perform, e.g. ACTION_FETCH_NEW_ITEMS
    private static final String ACTION_FOO = "com.sudowear.android.action.FOO";
    private static final String ACTION_BAZ = "com.sudowear.android.action.BAZ";

    // TODO: Rename parameters
    private static final String EXTRA_PARAM1 = "com.sudowear.android.extra.PARAM1";
    private static final String EXTRA_PARAM2 = "com.sudowear.android.extra.PARAM2";

    private GoogleApiClient mGoogleApiClient;

    /**
     * Starts this service to send a message to the connected wear device.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startSendMessage(Context context, String param1, String param2) {
        Intent intent = new Intent(context, MyIntentService.class);
        intent.setAction(ACTION_FOO);
        intent.putExtra(EXTRA_PARAM1, param1);
        intent.putExtra(EXTRA_PARAM2, param2);
        context.startService(intent);
    }

    public MyIntentService() {
        super("MyIntentService");
    }

    @Override
    protected void onHandleIntent(Intent intent) {
        Log.v(TAG, "onHandleIntent");

        if (intent != null) {

            final String action = intent.getAction();
            final Bundle extras = intent.getExtras();

            if (mGoogleApiClient == null) {
                mGoogleApiClient = buildApiClient();
            }
        }

        ConnectionResult result = mGoogleApiClient.blockingConnect();
        if (result.isSuccess()) {
            sendMessageToNode();
            mGoogleApiClient.disconnect();
        } else {
            Log.e(TAG, result.toString());
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void sendMessageToNode() {
        Log.v(TAG, "sendMessageToNode");

        NodeApi.GetConnectedNodesResult nodes = Wearable.NodeApi.getConnectedNodes(mGoogleApiClient).await();

        for(Node n : nodes.getNodes()) {
            Log.v(TAG, n.getDisplayName());
            Wearable.MessageApi.sendMessage(mGoogleApiClient, n.getId(), "/order/Pizza", null);
        }
    }

    /**
     * Builds a {@link GoogleApiClient} for {@link MyIntentService} to use
     */
    private GoogleApiClient buildApiClient() {
        return new GoogleApiClient.Builder(this)
//                    .addConnectionCallbacks(new GoogleApiClient.ConnectionCallbacks() {
//                        @Override
//                        public void onConnected(Bundle bundle) {
//                            sendMessageToNode(action, extras);
//                            mGoogleApiClient.disconnect();
//                        }
//
//                        @Override
//                        public void onConnectionSuspended(int i) {
//
//                        }
//                    })
                        .addApi(Wearable.API)
                        .build();
    }
}
