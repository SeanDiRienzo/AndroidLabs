package com.example.androidlabs;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.DatabaseUtils;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Arrays;

import static com.example.androidlabs.MyDatabaseOpenHelper.TABLE_NAME;

public class ChatRoomActivity extends AppCompatActivity {
    ArrayList<Message> messages;
    ChatAdapter adapter;
    ListView list;
    Button sendButton;
    Button receiveButton;
    EditText chatText;
    public static final String ACTIVITY_NAME = "CHATROOM_ACTIVITY";
    SQLiteDatabase db;
    public static final String ITEM_SELECTED = "ITEM";
    public static final String ITEM_POSITION = "POSITION";
    public static final String ITEM_ID = "ID";
    public static final int EMPTY_ACTIVITY = 345;
    MyDatabaseOpenHelper dbOpener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_chat_room);
        boolean isTablet = findViewById(R.id.fragmentLocation) != null; //check if the FrameLayout is loaded
        sendButton = findViewById(R.id.sendButton);
        receiveButton = findViewById(R.id.receiveButton);
        chatText = findViewById(R.id.chatText);
        list = findViewById(R.id.messageView);
        messages = new ArrayList<Message>();


        //get a database:
        dbOpener = new MyDatabaseOpenHelper(this);
        db = dbOpener.getWritableDatabase();


        //query all the results from the database:
        String[] columns = {MyDatabaseOpenHelper.COL_ID, MyDatabaseOpenHelper.COL_MESSAGETEXT, MyDatabaseOpenHelper.COL_ISSENT};
        Cursor results = db.query(false, TABLE_NAME, columns, null, null, null, null, null, null);

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
                long newId = db.insert(TABLE_NAME, null, newRowValues);
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
                long newId = db.insert(TABLE_NAME, null, newRowValues);
                Message tempMessage = new Message(getChatText(), false, newId);
                addNewMessage(tempMessage);
                clearChatText();

            }
        });

        list.setOnItemClickListener((list, item, position, id) -> {
            Bundle dataToPass = new Bundle();
            dataToPass.putString("message", messages.get(position).message);
            dataToPass.putInt("position", position);
            dataToPass.putLong("id", messages.get(position).id);

            if (isTablet) {
                MessageFragment dFragment = new MessageFragment(); //add a DetailFragment
                dFragment.setArguments(dataToPass); //pass it a bundle for information
                dFragment.setTablet(true);  //tell the fragment if it's running on a tablet or not
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(R.id.fragmentLocation, dFragment) //Add the fragment in FrameLayout

                        .commit(); //actually load the fragment.
            } else //isPhone
            {
                Intent nextActivity = new Intent(ChatRoomActivity.this, MessageContainer.class);
                nextActivity.putExtras(dataToPass); //send data to next activity
                startActivityForResult(nextActivity, EMPTY_ACTIVITY); //make the transition
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == EMPTY_ACTIVITY) {
            if (resultCode == RESULT_OK) //if you hit the delete button instead of back button
            {
                long id = data.getLongExtra("id", 0);
                int position = data.getIntExtra("position", 0);
                deleteMessageId((int) id, position);
            }
        }
    }

    public void deleteMessageId(long id, int position) {
        Log.i("Delete this message:", " id=" + id + "pos=" + position);
        messages.remove(position);
        adapter.notifyDataSetChanged();
        db.delete(MyDatabaseOpenHelper.TABLE_NAME, "_id =?", new String[]{String.valueOf(id)});
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

        Log.i("Cursor Object", "Version : " + version + " Columns: " + c.getColumnCount() + ", Names : " + Arrays.toString(c.getColumnNames()) + ", Results " + c.getCount() + DatabaseUtils.dumpCursorToString(c));
    }


}
