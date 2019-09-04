package com.example.chatexamplesocket;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.chatexamplesocket.R;
import com.example.chatexamplesocket.model.Message;

import java.util.List;


public class MessageAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<Message> mMessages;

    public MessageAdapter(Context context, List<Message> messages) {
        mMessages = messages;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        int layout = -1;
        View v;
        switch (viewType) {
            case Message.TYPE_MY_MESSAGE:
                layout = R.layout.item_message_right;
                v = LayoutInflater
                        .from(parent.getContext())
                        .inflate(layout, parent, false);
                return new ViewHolderMymessage(v);
            case Message.TYPE_MESSAGE:
                layout = R.layout.item_message;
                v = LayoutInflater
                        .from(parent.getContext())
                        .inflate(layout, parent, false);
                return new ViewHolderMessage(v);
            case Message.TYPE_LOG:
                layout = R.layout.item_log;
                v = LayoutInflater
                        .from(parent.getContext())
                        .inflate(layout, parent, false);
                return new ViewHolderLog(v);
            case Message.TYPE_ACTION:
                layout = R.layout.item_action;
                v = LayoutInflater
                        .from(parent.getContext())
                        .inflate(layout, parent, false);
                return new ViewHolderActionTyping(v);
        }
        return null;
    }


    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder viewHolder, int position) {
        Message message = mMessages.get(position);

        switch (getItemViewType(position)){
            case Message.TYPE_MY_MESSAGE:
                ((ViewHolderMymessage) viewHolder).setMessage(message.getMessage());
                ((ViewHolderMymessage) viewHolder).setUsername(message.getUsername());
                break;
            case Message.TYPE_MESSAGE:
                ((ViewHolderMessage) viewHolder).setMessage(message.getMessage());
                ((ViewHolderMessage) viewHolder).setUsername(message.getUsername());
                break;
            case Message.TYPE_LOG:
                ((ViewHolderLog) viewHolder).setMessage(message.getMessage());
                break;
            case Message.TYPE_ACTION:
                ((ViewHolderActionTyping) viewHolder).setUsername(message.getUsername());
                break;
        }
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mMessages.get(position).getType();
    }

    public class ViewHolderMymessage extends RecyclerView.ViewHolder {
        private TextView mUsernameView;
        private TextView mMessageView;

        public ViewHolderMymessage(View itemView) {
            super(itemView);
            mUsernameView = (TextView) itemView.findViewById(R.id.username_my);
            mMessageView = (TextView) itemView.findViewById(R.id.message_my);
        }
        public void setUsername(String username) {
            if (null == mUsernameView) return;
            mUsernameView.setText(username);
        }
        public void setMessage(String message) {
            if (null == mMessageView) return;
            mMessageView.setText(message);
        }
    }

    public class ViewHolderMessage extends RecyclerView.ViewHolder {
        private TextView mUsernameView;
        private TextView mMessageView;

        public ViewHolderMessage(View itemView) {
            super(itemView);
            mUsernameView = (TextView) itemView.findViewById(R.id.username_user);
            mMessageView = (TextView) itemView.findViewById(R.id.message_user);
        }
        public void setUsername(String username) {
            if (null == mUsernameView) return;
            mUsernameView.setText(username);
        }
        public void setMessage(String message) {
            if (null == mMessageView) return;
            mMessageView.setText(message);
        }
    }

    public class ViewHolderActionTyping extends RecyclerView.ViewHolder {
        private TextView mUsernameView;

        public ViewHolderActionTyping(View itemView) {
            super(itemView);
            mUsernameView = (TextView) itemView.findViewById(R.id.username_type);
        }
        public void setUsername(String username) {
            if (null == mUsernameView) return;
            mUsernameView.setText(username);
        }
    }

    public class ViewHolderLog extends RecyclerView.ViewHolder {
        private TextView mMessageView;

        public ViewHolderLog(View itemView) {
            super(itemView);
            mMessageView = (TextView) itemView.findViewById(R.id.message_log);

        }
        public void setMessage(String message) {
            if (null == mMessageView) return;
            mMessageView.setText(message);
        }
    }
}
