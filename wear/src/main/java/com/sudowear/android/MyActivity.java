package com.sudowear.android;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.support.wearable.activity.ConfirmationActivity;
import android.support.wearable.view.WatchViewStub;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.util.List;

public class MyActivity extends Activity {

    private static final String TAG = MyActivity.class.getSimpleName();

    private final static int SPEECH_REQUEST_CODE = 1;
    private final static int CONFIRMATION_REQUEST_CODE = 2;

    private TextView mTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my);
        final WatchViewStub stub = (WatchViewStub) findViewById(R.id.watch_view_stub);
        stub.setOnLayoutInflatedListener(new WatchViewStub.OnLayoutInflatedListener() {
            @Override
            public void onLayoutInflated(WatchViewStub stub) {
                mTextView = (TextView) stub.findViewById(R.id.text);
                mTextView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        displaySpeechRecognizer();
                    }
                });
            }
        });
        displaySpeechRecognizer();
    }

    // Create an intent that can start the Speech Recognizer activity
    private void displaySpeechRecognizer() {
        Log.v(TAG, "displaySpeechRecognizer");
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL,
                RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        // Start the activity, the intent will be populated with the speech text
        startActivityForResult(intent, SPEECH_REQUEST_CODE);
    }


    // This callback is invoked when the Speech Recognizer returns.
    // This is where you process the intent and extract the speech text from the intent.
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
            Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == SPEECH_REQUEST_CODE) {
                List<String> results = data.getStringArrayListExtra(
                        RecognizerIntent.EXTRA_RESULTS);
                String spokenText = results.get(0);
                if (spokenText.toLowerCase().equals("make me a sandwich") ||
                        spokenText.toLowerCase().equals("a")) {

                    MyIntentService.startSendMessage(this, "", "");

                    Log.v(TAG, "Starting ConfirmationActivity");
                    Intent intent = new Intent(this, ConfirmationActivity.class);
                    intent.putExtra(ConfirmationActivity.EXTRA_ANIMATION_TYPE,
                            ConfirmationActivity.SUCCESS_ANIMATION);
                    intent.putExtra(ConfirmationActivity.EXTRA_MESSAGE, getString(R.string.dialog_success));
                    startActivityForResult(intent, CONFIRMATION_REQUEST_CODE);
                } else {
                    mTextView.setText("y u no make sense");
                    Intent i = new Intent(this, RetryActivity.class);
                    startActivity(i);
                }
            }
        } else if (resultCode == RESULT_CANCELED) {
            // Confirmation activity does not return OK
            if (requestCode == CONFIRMATION_REQUEST_CODE) {
                finish();
            }
        } else {
            Log.v(TAG, "When does this happen?");
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
