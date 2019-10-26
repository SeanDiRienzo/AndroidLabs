package com.example.androidlabs;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.database.DatabaseUtils;

import java.util.ArrayList;
import java.util.Arrays;

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
        final SQLiteDatabase db;

        //get a database:
        MyDatabaseOpenHelper dbOpener = new MyDatabaseOpenHelper(this);
        db = dbOpener.getWritableDatabase();


        //query all the results from the database:
        String[] columns = {MyDatabaseOpenHelper.COL_ID, MyDatabaseOpenHelper.COL_MESSAGETEXT, MyDatabaseOpenHelper.COL_ISSENT};
        Cursor results = db.query(false, MyDatabaseOpenHelper.TABLE_NAME, columns, null, null, null, null, null, null);

        //find the column indices:
        int messageColumnIndex = results.getColumnIndex(MyDatabaseOpenHelper.COL_MESSAGETEXT);
        int issentColumnIndex = results.getColumnIndex(MyDatabaseOpenHelper.COL_ISSENT);
        int idColIndex = results.getColumnIndex(MyDatabaseOpenHelper.COL_ID);

        //iterate over the results, return true if there is a next item:
        while (results.moveToNext()) {
            String messageText = results.getString(messageColumnIndex);
            int isSent = (results.getInt(issentColumnIndex));
            long id = results.getLong(idColIndex);

            //add the new Contact to the array list:
            boolean isSentBool;
            if (isSent == 0) {
                isSentBool = false;

            } else {
                isSentBool = true;
            }
            messages.add(new Message(messageText, isSentBool, id));

        }
        printCursor(results, db.getVersion());

        adapter = new ChatAdapter(this, messages);
        list.setAdapter(adapter);

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //add to the database and get the new ID
                ContentValues newRowValues = new ContentValues();
                //put string name in the NAME column:
                newRowValues.put(MyDatabaseOpenHelper.COL_MESSAGETEXT, getChatText());
                //put string email in the EMAIL column:
                newRowValues.put(MyDatabaseOpenHelper.COL_ISSENT, true);
                //insert in the database:
                long newId = db.insert(MyDatabaseOpenHelper.TABLE_NAME, null, newRowValues);
                Message tempMessage = new Message(getChatText(), true, newId);
                addNewMessage(tempMessage);
                clearChatText();


            }
        });

        receiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //add to the database and get the new ID
                ContentValues newRowValues = new ContentValues();
                //put string name in the NAME column:
                newRowValues.put(MyDatabaseOpenHelper.COL_MESSAGETEXT, getChatText());
                //put string email in the EMAIL column:
                newRowValues.put(MyDatabaseOpenHelper.COL_ISSENT, false);
                //insert in the database:
                long newId = db.insert(MyDatabaseOpenHelper.TABLE_NAME, null, newRowValues);
                Message tempMessage = new Message(getChatText(), false, newId);
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

    public void printCursor(Cursor c, int version) {

        Log.i("Cursor Object", "Version : " + version + " Columns: " + c.getColumnCount() + ", Names : " + Arrays.toString(c.getColumnNames()) + ", Results " + c.getCount() + DatabaseUtils.dumpCursorToString(c) );
    }


}
