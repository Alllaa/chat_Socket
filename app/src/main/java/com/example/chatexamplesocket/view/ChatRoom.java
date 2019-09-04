package com.example.chatexamplesocket.view;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatexamplesocket.MessageAdapter;
import com.example.chatexamplesocket.R;
import com.example.chatexamplesocket.model.Message;
import com.example.chatexamplesocket.modelview.ChatRoomVM;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ChatRoom extends AppCompatActivity {

    private RecyclerView mMessagesView;
    private EditText mInputMessageView;
    private ImageButton img;
    private ChatRoomVM chatRoomVM = new ChatRoomVM();
    private String mUsername;
    private List<Message> mMessages = new ArrayList<Message>();
    private RecyclerView.Adapter mAdapter;
    private boolean mTyping = false;

    private Handler mTypingHandler = new Handler();
    Bundle bundle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        chatRoomVM = ViewModelProviders.of(this).get(ChatRoomVM.class);

        mInputMessageView = findViewById(R.id.message);
        img = findViewById(R.id.send);

        mMessages.clear();
        mMessages.addAll(chatRoomVM.getMessages());
        mMessagesView = findViewById(R.id.messages);
        mMessagesView.setLayoutManager(new LinearLayoutManager(ChatRoom.this));

        bundle = getIntent().getExtras();
        mUsername = bundle.getString("username");
        Log.d("LOL", mUsername);

        mAdapter = new MessageAdapter(this, mMessages);
        mMessagesView.setAdapter(mAdapter);

        mInputMessageView.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                if (null == mUsername) return;

                if (!mTyping) {
                    mTyping = true;
                    chatRoomVM.emmit("Typing");
                }

                mTypingHandler.removeCallbacks(onTypingTimeout);
                mTypingHandler.postDelayed(onTypingTimeout, 900);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptSend();
            }
        });

        chatRoomVM.newMessage().observe(this, new Observer<JSONObject>() {
            @Override
            public void onChanged(JSONObject jsonObject) {
                String userName = "";
                String message = "";

                try {
                    userName = jsonObject.getString("username");
                    message = jsonObject.getString("message");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                removeTyping(userName);
                addMessage(userName, message, false);
            }
        });

        chatRoomVM.newJoined().observe(this, new Observer<JSONObject>() {
            @Override
            public void onChanged(JSONObject jsonObject) {
                String userName = "";
                int numOfUser = 0;

                try {
                    userName = jsonObject.getString("userName");
                    numOfUser = jsonObject.getInt("numUsers");

                    Log.d("num of users",numOfUser+" ");
                    Log.d("users",userName);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                addLog("Joined " + userName);
                addParticipantsLog(numOfUser);

            }
        });

        chatRoomVM.oldUserLeft().observe(this, new Observer<JSONObject>() {
            @Override
            public void onChanged(JSONObject jsonObject) {
                String username = "";
                int numUsers = 0;
                try {
                    username = jsonObject.getString("username");
                    numUsers = jsonObject.getInt("numUsers");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                addLog("Left  " + username);
                addParticipantsLog(numUsers);
                removeTyping(username);
            }
        });

        chatRoomVM.newTyping().observe(this, new Observer<JSONObject>() {
            @Override
            public void onChanged(JSONObject jsonObject) {
                String username;
                try {
                    username = jsonObject.getString("username");
                } catch (JSONException e) {
                    Log.e("TAG", e.getMessage());
                    return;
                }
                addTyping(username);
            }
        });

        chatRoomVM.stopTyping().observe(this, new Observer<JSONObject>() {
            @Override
            public void onChanged(JSONObject jsonObject) {
                String username;
                try {
                    username = jsonObject.getString("username");
                    Log.d("TAG", username);
                } catch (JSONException e) {
                    Log.e("TAG", e.getMessage());
                    return;
                }
                removeTyping(username);
            }
        });

        chatRoomVM.socketON(mUsername);
    }

    private void attemptSend() {
        if (null == mUsername) return;

        mTyping = false;

        String message = mInputMessageView.getText().toString().trim();
        if (TextUtils.isEmpty(message)) {
            mInputMessageView.requestFocus();
            return;
        }

        mInputMessageView.setText("");
        addMessage(mUsername, message, true);

        // perform the sending message attempt.

        chatRoomVM.emmitMessage("new message", message);
    }


    private void removeTyping(String username) {
        for (int i = mMessages.size() - 1; i >= 0; i--) {
            Message message = mMessages.get(i);
            if (message.getType() == Message.TYPE_ACTION && message.getUsername().equals(username)) {
                mMessages.remove(i);
                mAdapter.notifyItemRemoved(i);
            }
        }
    }

    private void addMessage(String username, String message, boolean myMessage) {
        if (myMessage)
        {
            mMessages.add(new Message.Builder(Message.TYPE_MY_MESSAGE)
                    .username(username).message(message).build());
        }
        else
        {
            mMessages.add(new Message.Builder(Message.TYPE_MESSAGE)
                    .username(username).message(message).build());
        }



        mAdapter.notifyItemInserted(mMessages.size() - 1);
        scrollToBottom();
    }

    private void scrollToBottom() {
        mMessagesView.scrollToPosition(mAdapter.getItemCount() - 1);
    }


    private void addLog(String message) {
        mMessages.add(new Message.Builder(Message.TYPE_LOG)
                .message(message).build());
        mAdapter.notifyItemInserted(mMessages.size() - 1);
        scrollToBottom();
    }


    private void addParticipantsLog(int numUsers) {
        addLog(getResources().getQuantityString(R.plurals.message_participants, numUsers, numUsers));
    }


    private void addTyping(String username) {
        mMessages.add(new Message.Builder(Message.TYPE_ACTION)
                .username(username).build());
        mAdapter.notifyItemInserted(mMessages.size() - 1);
        scrollToBottom();
    }

    private Runnable onTypingTimeout = new Runnable() {
        @Override
        public void run() {
            if (!mTyping) return;

            mTyping = false;
            chatRoomVM.emmit("stop typing");
        }
    };

    @Override
    protected void onPause() {
        super.onPause();
        for (int i = mMessages.size() - 1; i >= 0; i--)
            if (mMessages.get(i).getType() == Message.TYPE_ACTION) mMessages.remove(i);

        chatRoomVM.insertMessages(mMessages);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        chatRoomVM.socketOFF();
    }
}
