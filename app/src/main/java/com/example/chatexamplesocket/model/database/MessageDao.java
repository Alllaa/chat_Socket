package com.example.chatexamplesocket.model.database;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import com.example.chatexamplesocket.model.Message;

import java.util.List;

@Dao
public interface MessageDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertMessages(List<Message> messages);


    @Query("SELECT * FROM chat")
    List<Message> getMessages();
}
