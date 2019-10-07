package com.example.androidlabs;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import java.util.ArrayList;

public class ChatRoomActivity extends AppCompatActivity {
    ArrayList<Message> messages;
    ChatAdapter adapter;
    ListView list;
    Button sendButton;
    Button receiveButton;
    EditText chatText;
    public static final String ACTIVITY_NAME = "CHATROOM_ACTIVITY";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);
        sendButton = findViewById(R.id.sendButton);
        receiveButton = findViewById(R.id.receiveButton);
        chatText = findViewById(R.id.chatText);
        list = findViewById(R.id.messageView);
        messages = new ArrayList<Message>();
        adapter = new ChatAdapter(this, messages);
        list.setAdapter(adapter);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Message tempMessage = new Message(getChatText(), true);
                addNewMessage(tempMessage);
                clearChatText();



            }
        });

        receiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Message tempMessage = new Message(getChatText(), false);
               addNewMessage(tempMessage);
               clearChatText();

            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(ACTIVITY_NAME, "In Function onResume()");
    }

    @Override
    protected void onDestroy() {
        Log.e(ACTIVITY_NAME, "In Function onDestroy()");
        super.onDestroy();
    }

    @Override
    protected void onStart() {
        Log.e(ACTIVITY_NAME, "In Function onStart()");
        super.onStart();
    }

    @Override
    protected void onPause() {
        Log.e(ACTIVITY_NAME, "In Function onPause()");
        super.onPause();
    }

    @Override
    protected void onStop() {
        Log.e(ACTIVITY_NAME, "In Function onStop()");
        super.onStop();
    }
    void addNewMessage(Message m) {
        this.messages.add(m);
        adapter.notifyDataSetChanged();

    }

    public void clearChatText() {
        this.chatText.setText("");
    }

    public String getChatText() {
        return chatText.getText().toString();
    }
}
