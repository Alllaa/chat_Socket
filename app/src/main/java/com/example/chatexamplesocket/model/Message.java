package com.example.chatexamplesocket.model;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "chat")
public class Message {


    public static final int TYPE_MESSAGE = 0;
    public static final int TYPE_LOG = 1;
    public static final int TYPE_ACTION = 2;
    public static final int TYPE_MY_MESSAGE = 3;



    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    private int id;

    @ColumnInfo(name = "type")
    private int type;

    @ColumnInfo(name = "message")
    private String message;

    @ColumnInfo(name = "user_name")
    private String username;

    public Message()
    {

    }
    public void setId(int id) {
        this.id = id;
    }
    public void setType(int type) {
        this.type = type;
    }
    public void setUsername(String username) {
        this.username = username;
    }
    public void setMessage(String message) {
        this.message = message;
    }

    public int getId() {
        return id;
    }
    public int getType() {
        return type;
    }
    public String getMessage() {
        return message;
    }
    public String getUsername() {
        return username;
    }



    public static class Builder {
        private final int mType;
        private String mUsername;
        private String mMessage;

        public Builder(int type) {
            mType = type;
        }

        public Builder username(String username) {
            mUsername = username;
            return this;
        }

        public Builder message(String message) {
            mMessage = message;
            return this;
        }

        public Message build() {
            Message message = new Message();
            message.type = mType;
            message.username = mUsername;
            message.message = mMessage;
            return message;
        }
    }
}
