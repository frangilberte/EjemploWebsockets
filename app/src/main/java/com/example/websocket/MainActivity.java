package com.example.websocket;

import android.os.Build;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;


public class MainActivity extends ActionBarActivity {

    private WebSocketClient webSocket;
    private TextView textView;
    //private EditText editText;
    //private Button btnSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.messages);
        //editText = (EditText) findViewById(R.id.message);
        //btnSend = (Button) findViewById(R.id.btnSend);

        connectWebSocket();

        PlaceholderFragment placeholderFragment = new PlaceholderFragment();

        getSupportFragmentManager().beginTransaction()
            .add(R.id.container, placeholderFragment)
            .commit();



    }

    private void connectWebSocket() {
        final URI uri;
        try {
            uri = new URI("ws://websockethost:8080");
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }

        webSocket = new WebSocketClient(uri) {
            @Override
            public void onOpen(ServerHandshake handshakedata) {
                Log.i("Websocket", "Opened");
                webSocket.send("Hello from"+ Build.MANUFACTURER + " " + Build.MODEL);
            }

            @Override
            public void onMessage(String s) {
                final String message = s;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        textView.setText(textView.getText() + "\n" + message);
                    }
                });
            }

            @Override
            public void onClose(int code, String reason, boolean remote) {
                Log.i("Websocket", "Closed " + reason);
            }

            @Override
            public void onError(Exception ex) {
                Log.i("Websocket", "Error " + ex.getMessage());
            }
        };

        webSocket.connect();
    }

    public void sendMessage(View view){
        EditText editText = (EditText) findViewById(R.id.message);
        webSocket.send(editText.getText().toString());
        editText.setText("");
    }

}
