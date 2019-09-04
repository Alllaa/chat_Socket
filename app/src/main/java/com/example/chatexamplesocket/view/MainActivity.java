package com.example.chatexamplesocket.view;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProviders;

import com.example.chatexamplesocket.R;
import com.example.chatexamplesocket.modelview.ChatRoomVM;

import io.socket.client.Socket;

public class MainActivity extends AppCompatActivity {

    private EditText editUserName;
    private String userName;
    private Socket mSocket;
    ChatRoomVM chatRoomVM = new ChatRoomVM();
    private Button join;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        chatRoomVM = ViewModelProviders.of(this).get(ChatRoomVM.class);


        editUserName = findViewById(R.id.user_name);
        join = findViewById(R.id.join);


        join.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                logIn();
                Intent intent = new Intent(MainActivity.this, ChatRoom.class);
                intent.putExtra("username", userName);

                startActivity(intent);
            }
        });

    }

    private void logIn() {
        editUserName.setError(null);

        userName = editUserName.getText().toString().trim();

        if (TextUtils.isEmpty(userName)) {
            editUserName.setError("This field is required");
            editUserName.requestFocus();
            return;
        }

        chatRoomVM.emmitMessage("add user", userName);
    }




}
