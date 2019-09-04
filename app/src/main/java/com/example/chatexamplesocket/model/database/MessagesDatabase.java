package com.example.chatexamplesocket.model.database;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;

import com.example.chatexamplesocket.AppClass;
import com.example.chatexamplesocket.model.Message;

@Database(entities = Message.class,version = 1 ,exportSchema = false)
public abstract class MessagesDatabase extends RoomDatabase {
    private static MessagesDatabase INSTANCE;

    public abstract MessageDao dao();

    public static MessagesDatabase getInstance()
    {
        if(INSTANCE == null){
            INSTANCE = Room.databaseBuilder(AppClass.getObject().getApplicationContext(), MessagesDatabase.class,
                    "messages_database").allowMainThreadQueries().build();
        }
        return INSTANCE;
    }
}
