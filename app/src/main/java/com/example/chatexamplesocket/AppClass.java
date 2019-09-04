package com.example.chatexamplesocket;

import android.app.Application;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

public class AppClass extends Application {
    private static AppClass appClass;

    public static synchronized AppClass getObject() {

        return appClass;
    }
    private Socket mSocket;
    private static final String url = "https://socket-io-chat.now.sh/";
    @Override
    public void onCreate() {
        super.onCreate();
        appClass = this;
        try {
            mSocket = IO.socket(url);
        }catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }

    }
    public Socket getmSocket()
    {
        return mSocket;
    }


}
