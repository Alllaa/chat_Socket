package com.example.chatexamplesocket.modelview;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.chatexamplesocket.model.Message;
import com.example.chatexamplesocket.model.SocketFuns;

import org.json.JSONObject;

import java.util.List;

public class ChatRoomVM extends ViewModel {

    private MutableLiveData<JSONObject> message = new MutableLiveData<>();
    private MutableLiveData<JSONObject> joined = new MutableLiveData<>();
    private MutableLiveData<JSONObject> left = new MutableLiveData<>();
    private MutableLiveData<JSONObject> typing = new MutableLiveData<>();
    private MutableLiveData<JSONObject> stopTyping = new MutableLiveData<>();

    private SocketFuns socketFuns;

    public ChatRoomVM()
    {
        socketFuns = SocketFuns.getInstance();
    }

    public void socketON(String user_name)
    {
        socketFuns.SocketOn(user_name);
    }
    public void socketOFF()
    {
        socketFuns.SocketOff();
    }

    public void emmitMessage(String event,String message){
        socketFuns.emitMessage(event,message);
    }
    public void emmit(String event)
    {
        socketFuns.emit(event);
    }
    public LiveData<JSONObject> newMessage()
    {
        message = (MutableLiveData<JSONObject>) socketFuns.newMessage();
        return message;
    }
    public LiveData<JSONObject> newJoined()
    {
        joined = (MutableLiveData<JSONObject>) socketFuns.newUserJoined();
        return joined;
    }
    public LiveData<JSONObject> oldUserLeft()
    {
        left = (MutableLiveData<JSONObject>) socketFuns.oldUserLeft();
        return left;
    }
    public LiveData<JSONObject> newTyping()
    {
        typing = (MutableLiveData<JSONObject>) socketFuns.newTyping();
        return typing;
    }
    public LiveData<JSONObject> stopTyping()
    {
        stopTyping = (MutableLiveData<JSONObject>) socketFuns.newStopTyping();
        return stopTyping;
    }

    public void insertMessages(List<Message> list ){
        socketFuns.insertMessages(list);
    }
    public List<Message> getMessages(){
        return socketFuns.getMessages();
    }

}
