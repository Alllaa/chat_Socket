package com.example.chatexamplesocket.model;

import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.example.chatexamplesocket.AppClass;
import com.example.chatexamplesocket.model.database.MessagesDatabase;
import com.example.chatexamplesocket.view.ChatRoom;

import org.json.JSONObject;

import java.util.List;

import io.socket.client.Socket;
import io.socket.emitter.Emitter;


public class SocketFuns {

    private static SocketFuns socketFuns;
    AppClass sockApp = (AppClass) AppClass.getObject().getApplicationContext();
    Socket mSocket = sockApp.getmSocket();
    boolean isConnected = true;
    String mUsername ;
    private final Handler handler;

    private Emitter.Listener onNewMessage;
    private Emitter.Listener onUserJoined;
    private Emitter.Listener onUserLeft;
    private Emitter.Listener onTyping;
    private Emitter.Listener onStopTyping;

    final MutableLiveData<JSONObject> newMessage = new MutableLiveData<>();
    final MutableLiveData<JSONObject> userJoined = new MutableLiveData<>();
    final MutableLiveData<JSONObject> userLeft = new MutableLiveData<>();
    final MutableLiveData<JSONObject> typing = new MutableLiveData<>();
    final MutableLiveData<JSONObject> stopTyping = new MutableLiveData<>();

    public SocketFuns() {
        handler = new Handler(AppClass.getObject().getMainLooper());
    }

    public static synchronized SocketFuns getInstance() {
        if (socketFuns == null) {
            socketFuns = new SocketFuns();
        }
        return socketFuns;
    }
    private void runOnUiThread(Runnable r) {
        handler.post(r);
    }

    public void SocketOn(String user_name) {
        mUsername = user_name;
        mSocket.on(Socket.EVENT_CONNECT, onConnect);
        mSocket.on(Socket.EVENT_DISCONNECT, onDisconnect);
        mSocket.on(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.on(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
        mSocket.on("new message", onNewMessage);
        mSocket.on("user joined", onUserJoined);
        mSocket.on("user left", onUserLeft);
        mSocket.on("typing", onTyping);
        mSocket.on("stop typing", onStopTyping);
        mSocket.connect();
    }

    public void SocketOff() {
        mSocket.disconnect();
        mSocket.off(Socket.EVENT_CONNECT, onConnect);
        mSocket.off(Socket.EVENT_DISCONNECT, onDisconnect);
        mSocket.off(Socket.EVENT_CONNECT_ERROR, onConnectError);
        mSocket.off(Socket.EVENT_CONNECT_TIMEOUT, onConnectError);
        mSocket.off("new message", onNewMessage);
        mSocket.off("user joined", onUserJoined);
        mSocket.off("user left", onUserLeft);
        mSocket.off("typing", onTyping);
        mSocket.off("stop typing", onStopTyping);
    }

     private Emitter.Listener onConnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!isConnected) {
                        if (null != mUsername)
                            mSocket.emit("add user", mUsername);
                        Toast.makeText(AppClass.getObject().getApplicationContext(),
                                "Connected", Toast.LENGTH_LONG).show();
                        isConnected = true;
                    }
                }
            });
        }
    };

    private Emitter.Listener onDisconnect = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.i("ChatRoom", "diconnected");
                    isConnected = false;
                    Toast.makeText(AppClass.getObject().getApplicationContext(),
                            "Disconnected", Toast.LENGTH_LONG).show();
                }
            });
        }
    };
    private Emitter.Listener onConnectError = new Emitter.Listener() {
        @Override
        public void call(Object... args) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Log.e("ChatRoom", "Error connecting");
                    Toast.makeText(AppClass.getObject().getApplicationContext(),
                            "Failed to connect", Toast.LENGTH_LONG).show();
                }
            });
        }
    };


    public LiveData<JSONObject> newMessage()
    {
        onNewMessage = new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject data = (JSONObject) args[0];
                        newMessage.postValue(data);
                    }
                });
            }
        };
        return newMessage;
    }

    public LiveData<JSONObject> newUserJoined()
    {
        onUserJoined = new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject data = (JSONObject) args[0];
                        userJoined.postValue(data);
                    }
                });
            }
        };
        return userJoined;
    }
    public LiveData<JSONObject> oldUserLeft()
    {
        onUserLeft= new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject data = (JSONObject) args[0];
                        userLeft.postValue(data);
                    }
                });
            }
        };
        return userLeft;
    }

    public  LiveData<JSONObject> newTyping()
    {
        onTyping = new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject data = (JSONObject) args[0];
                        typing.postValue(data);
                    }
                });
            }
        };
        return typing;
    }
    public LiveData<JSONObject> newStopTyping()
    {
        onStopTyping = new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        JSONObject data = (JSONObject) args[0];
                        stopTyping.postValue(data);
                    }
                });
            }
        };
        return stopTyping;
    }
    public void emit(String event)
    {
        mSocket.emit(event);
    }
    public void emitMessage(String event, String message)
    {
        mSocket.emit(event,message);
    }

    public void insertMessages(List<Message> messages) {
        MessagesDatabase.getInstance().dao().insertMessages(messages);
    }

    public List<Message> getMessages() {
        List<Message> messages = MessagesDatabase.getInstance().dao().getMessages();

        return messages;
    }
}
